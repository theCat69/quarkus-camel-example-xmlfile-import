package com.fvd.hello.camel.manager;

import com.fvd.hello.camel.model.Fruit;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class FruitProcessorStrategy extends XmlProcessorStrategy<Fruit> {

  @Override
  protected void innerProcess(Fruit fruit) {
    var id = fruit.getId();
    log.info("Fruit id {}", id);
  }

  @Override
  public Class<Fruit> getClazz() {
    return Fruit.class;
  }

}
