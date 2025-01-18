package com.fvd.hello.camel.manager;

import com.fvd.hello.camel.model.Shit;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ShitProcessorStrategy extends XmlProcessorStrategy<Shit> {

  @Override
  protected void innerProcess(Shit shit) {
    var id = shit.getId();
    log.info("Shit id {}", id);
  }

  @Override
  public Class<Shit> getClazz() {
    return Shit.class;
  }

}
