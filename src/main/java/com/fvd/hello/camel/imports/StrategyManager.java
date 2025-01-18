package com.fvd.hello.camel.imports;

import com.fvd.hello.camel.imports.exception.NoApplicableStrategyException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@ApplicationScoped
@Getter
@RequiredArgsConstructor
public class StrategyManager implements Processor {

  private final Instance<ImportStrategy<?>> importStrategies;

  <T> void applyStrategy(T elem) {
    importStrategies.stream()
      .filter(importStrategy -> importStrategy.isApplicable(elem))
      .findFirst()
      .orElseThrow(() -> new NoApplicableStrategyException(elem))
      .processStrategy(elem);
  }

  @Override
  public void process(Exchange exchange) {
    applyStrategy(exchange.getMessage().getBody());
  }

  public Class<?>[] getClazzes() {
    return importStrategies.stream().map(ImportStrategy::getClazz).toArray(Class<?>[]::new);
  }
}
