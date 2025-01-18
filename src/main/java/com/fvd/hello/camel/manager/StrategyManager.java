package com.fvd.hello.camel.manager;

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

  private final Instance<XmlProcessorStrategy<?>> xmlProcessors;

  private <T> void applyStrategy(T elem) {
    xmlProcessors.stream()
      .filter(xmlProcessorStrategy -> xmlProcessorStrategy.isApplicable(elem))
      .findFirst()
      .orElseThrow(RuntimeException::new)
      .processStrategy(elem);
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    applyStrategy(exchange.getMessage().getBody());
  }

  public Class<?>[] getClazzes() {
    return xmlProcessors.stream().map(XmlProcessorStrategy::getClazz).toArray(Class<?>[]::new);
  }
}
