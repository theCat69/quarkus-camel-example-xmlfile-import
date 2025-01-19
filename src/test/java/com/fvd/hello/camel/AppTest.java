package com.fvd.hello.camel;

import com.fvd.hello.camel.db.entities.FruitEntity;
import com.fvd.hello.camel.db.entities.VegetableEntity;
import com.fvd.hello.camel.db.repositories.FruitRepository;
import com.fvd.hello.camel.db.repositories.VegetableRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.assertj.core.groups.Tuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@RequiredArgsConstructor
class AppTest {

  static final String TEST_INFILES = "test/infiles/";

  @ConfigProperty(name = "folders.out.path")
  String folderOut;
  @ConfigProperty(name = "folders.in.path")
  String folderIn;
  @ConfigProperty(name = "folders.work.path")
  String folderWork;
  @ConfigProperty(name = "folders.failed.path")
  String folderFailed;
  @ConfigProperty(name = "filter.lastmodified.seconds.beforeintegrate")
  Integer secondsSinceLastModified;

  final FruitRepository fruitRepository;
  final VegetableRepository vegetableRepository;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    Files.createDirectories(Path.of(folderIn));
  }

  @AfterEach
  void tearDown() {
    Stream.of(folderIn, folderOut, folderWork, folderFailed)
      .forEach(folder -> FileUtils.deleteQuietly(new File(folder)));
    QuarkusTransaction.requiringNew().call(() -> {
      fruitRepository.deleteAll();
      vegetableRepository.deleteAll();
      return 0;
    });
  }

  @Test
  void fileRoute_withInvalidAndValidFile_shouldProcessSuccessFullyValidAndRejectInvalid() {
    //given
    var inFiles = List.of(
      "valid/fruit.xml",
      "valid/vegetable.xml",
      "invalid/hello.xml"
    );
    //when
    copyInputFiles(inFiles);
    //then
    await().atMost(20, TimeUnit.SECONDS)
      .pollDelay(500, TimeUnit.MILLISECONDS).untilAsserted(() -> {
        assertThat(inFiles).allMatch(fName -> {
          var name = fName.split("/")[1];
          if (fName.contains("invalid")) {
            return Files.exists(Path.of(folderFailed + File.separator + name));
          } else {
            return Files.exists(Path.of(folderOut + File.separator + name));
          }
        });

        assertThat(FileUtils.isEmptyDirectory(new File(folderIn))).isTrue();
        assertThat(FileUtils.isEmptyDirectory(new File(folderWork))).isTrue();

        QuarkusTransaction.requiringNew().call(() -> {
          assertThat(vegetableRepository.listAll()).hasSize(1)
            .extracting(VegetableEntity::getName, VegetableEntity::getPrice)
            .containsExactly(Tuple.tuple("Salad", "9.99"));
          assertThat(fruitRepository.listAll()).hasSize(1)
            .extracting(FruitEntity::getName, FruitEntity::getPrice)
            .containsExactly(Tuple.tuple("Banana", "9.99"));
          return 0;
        });
      });
  }

  @Test
  void fileRoute_withNewlyCreatedFile_shouldFilterFileForAtLeastFilterTime() {
    //given
    var inFiles = List.of(
      "valid/fruit.xml",
      "valid/vegetable.xml"
    );
    //when
    writeInputFiles(inFiles);
    //then
    await().atLeast(secondsSinceLastModified, TimeUnit.SECONDS)
      .atMost(20, TimeUnit.SECONDS)
      .pollDelay(500, TimeUnit.MILLISECONDS).untilAsserted(() -> {
        assertThat(inFiles).allMatch(fName -> {
          var name = fName.split("/")[1];
          return Files.exists(Path.of(folderOut + File.separator + name));
        });

        assertThat(FileUtils.isEmptyDirectory(new File(folderIn))).isTrue();
        assertThat(FileUtils.isEmptyDirectory(new File(folderWork))).isTrue();

        QuarkusTransaction.requiringNew().call(() -> {
          assertThat(vegetableRepository.listAll()).hasSize(1)
            .extracting(VegetableEntity::getName, VegetableEntity::getPrice)
            .containsExactly(Tuple.tuple("Salad", "9.99"));
          assertThat(fruitRepository.listAll()).hasSize(1)
            .extracting(FruitEntity::getName, FruitEntity::getPrice)
            .containsExactly(Tuple.tuple("Banana", "9.99"));
          return 0;
        });

      });
  }

  private URL getResource(String path) {
    return Objects.requireNonNull(this.getClass().getClassLoader().getResource(path));
  }

  private void copyInputFiles(List<String> fileNames) {
    fileNames.forEach(fName -> {
      try {
        var exResource = getResource(TEST_INFILES + fName);
        var name = fName.split("/")[1];
        Files.copy(Path.of(exResource.toURI()), Path.of(folderIn + File.separator + name));
      } catch (IOException | URISyntaxException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private InputStream getResourceAsStream(String path) {
    return Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path));
  }

  private void writeInputFiles(List<String> fileNames) {
    fileNames.forEach(fName -> {
      try (var isResource = getResourceAsStream(TEST_INFILES + fName)) {
        var name = fName.split("/")[1];
        File targetFile = new File(folderIn + File.separator + name);
        FileUtils.copyInputStreamToFile(isResource, targetFile);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

}
