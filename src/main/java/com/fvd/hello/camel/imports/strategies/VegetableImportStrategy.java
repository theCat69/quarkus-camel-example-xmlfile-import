package com.fvd.hello.camel.imports.strategies;

import com.fvd.hello.camel.db.entities.VegetableEntity;
import com.fvd.hello.camel.db.repositories.VegetableRepository;
import com.fvd.hello.camel.imports.ImportStrategy;
import com.fvd.hello.camel.model.Vegetable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class VegetableImportStrategy extends ImportStrategy<Vegetable> {

  private final VegetableRepository vegetableRepository;

  @Override
  @Transactional(REQUIRES_NEW)
  public void innerProcess(Vegetable vegetable) {
    VegetableEntity vegetableEntity = VegetableEntity.builder()
      .name(vegetable.getName())
      .price(vegetable.getPrice())
      .build();
    vegetableRepository.persist(vegetableEntity);
  }

  @Override
  public Class<Vegetable> getClazz() {
    return Vegetable.class;
  }

}
