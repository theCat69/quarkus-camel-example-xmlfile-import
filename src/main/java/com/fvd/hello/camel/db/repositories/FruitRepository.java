package com.fvd.hello.camel.db.repositories;

import com.fvd.hello.camel.db.entities.FruitEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitRepository implements PanacheRepository<FruitEntity> {
}
