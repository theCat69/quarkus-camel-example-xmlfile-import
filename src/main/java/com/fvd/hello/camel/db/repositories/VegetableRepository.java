package com.fvd.hello.camel.db.repositories;

import com.fvd.hello.camel.db.entities.VegetableEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VegetableRepository implements PanacheRepository<VegetableEntity> {
}
