package com.fvd.hello.camel;

import com.fvd.hello.camel.imports.StrategyManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.UnmarshalException;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;

@ApplicationScoped
@RequiredArgsConstructor
public class Routes extends RouteBuilder {

  private final StrategyManager strategyManager;

  @ConfigProperty(name = "folders.out.path")
  String folderOut;
  @ConfigProperty(name = "folders.work.path")
  String folderWork;
  @ConfigProperty(name = "folders.failed.path")
  String folderFailed;

  @Override
  public void configure() throws Exception {
    DataFormat jaxb = new JaxbDataFormat(JAXBContext.newInstance(strategyManager.getClazzes()));

    onException(UnmarshalException.class)
      .log(LoggingLevel.ERROR, "Cannot unmarshall file :${header.CamelFilePath}" +
        " it was moved to {{folders.failed.path}} directory");

    from("file://{{folders.in.path}}?initialDelay=1000&delay=1000" +
      "&preMove=" + getAbsolutePath(folderWork) +
      "&move=" + getAbsolutePath(folderOut) +
      "&moveFailed=" + getAbsolutePath(folderFailed) +
      "&recursive=true")
      .log("File : ${header.CamelFileName} at ${header.CamelFileLastModified}")
      .unmarshal(jaxb)
      .log("Unmarshalled")
      .process(strategyManager)
      .log("done");

  }

  private String getAbsolutePath(String folder) {
    return Path.of(folder).toAbsolutePath().toString();
  }
}