package com.fvd.hello.camel.manager;

public class NoApplicableStrategyException extends RuntimeException {
  public NoApplicableStrategyException(Object elem) {
    super("No applicable strategy for : " + elem.getClass().getName());
  }
}
