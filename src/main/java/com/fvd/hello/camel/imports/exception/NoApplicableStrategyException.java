package com.fvd.hello.camel.imports.exception;

public class NoApplicableStrategyException extends RuntimeException {
  public NoApplicableStrategyException(Object elem) {
    super("No applicable strategy for : " + elem.getClass().getName());
  }
}
