package com.fvd.hello.camel.imports;

import com.fvd.hello.camel.imports.exception.NoApplicableStrategyException;
import io.quarkus.test.junit.QuarkusTest;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
record StrategyManagerTest(StrategyManager strategyManager) {

  @Test
  void strategyManager_withElementWithNoCorrespondingStrategy_shouldThrow() {
    //given
    var elementWithNoStrategy = ElementWithNoStrategy.builder()
      .id(1)
      .price("12.44")
      .build();
    //when & then
    assertThatThrownBy(() -> strategyManager.applyStrategy(elementWithNoStrategy))
      .isInstanceOf(NoApplicableStrategyException.class)
      .hasMessageContaining("ElementWithNoStrategy");
  }

  @Builder
  static class ElementWithNoStrategy {
    int id;
    String price;
  }

}