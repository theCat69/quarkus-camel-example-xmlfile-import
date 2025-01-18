package com.fvd.hello.camel.imports.strategies;

import com.fvd.hello.camel.imports.ImportStrategy;
import com.fvd.hello.camel.model.Vegetable;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class VegetableImportStrategy extends ImportStrategy<Vegetable> {

  @Override
  protected void innerProcess(Vegetable vegetable) {
    var id = vegetable.getId();
    log.info("Vegetable id {}", id);
  }

  @Override
  public Class<Vegetable> getClazz() {
    return Vegetable.class;
  }

}
