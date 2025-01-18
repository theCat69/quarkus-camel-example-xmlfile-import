package com.fvd.hello.camel.imports.strategies;

import com.fvd.hello.camel.imports.ImportStrategy;
import com.fvd.hello.camel.model.Fruit;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class FruitImportStrategy extends ImportStrategy<Fruit> {

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
