package com.fvd.hello.camel.pivot;


import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
@RegisterForReflection
public class Fruit {

  @XmlAttribute
  int id;

  @XmlElement(name = "n")
  String name;

  String price;

}