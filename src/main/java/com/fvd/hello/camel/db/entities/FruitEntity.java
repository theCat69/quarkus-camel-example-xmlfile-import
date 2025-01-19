package com.fvd.hello.camel.db.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FruitEntity extends AbstractBaseEntity {
  private String name;
  private String price;
}
