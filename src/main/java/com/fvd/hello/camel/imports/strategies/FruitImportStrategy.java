package com.fvd.hello.camel.imports.strategies;

import com.fvd.hello.camel.db.entities.FruitEntity;
import com.fvd.hello.camel.db.repositories.FruitRepository;
import com.fvd.hello.camel.imports.ImportStrategy;
import com.fvd.hello.camel.pivot.Fruit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class FruitImportStrategy extends ImportStrategy<Fruit> {

  private final FruitRepository fruitRepository;

  @Override
  @Transactional(REQUIRES_NEW)
  protected void innerProcess(Fruit fruit) {
    FruitEntity fruitEntity = FruitEntity.builder()
      .name(fruit.getName())
      .price(fruit.getPrice())
      .build();
    fruitRepository.persist(fruitEntity);
  }

  @Override
  public Class<Fruit> getClazz() {
    return Fruit.class;
  }

}
