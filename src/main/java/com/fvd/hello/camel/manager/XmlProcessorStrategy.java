package com.fvd.hello.camel.manager;

public abstract class XmlProcessorStrategy<T> {

  public void processStrategy(Object elem) {
    innerProcess(castToElement(elem));
  }

  protected abstract void innerProcess(T elem);

  public abstract Class<T> getClazz();

  public <E> boolean isApplicable(E applicant) {
    return getClazz().isInstance(applicant);
  }

  private T castToElement(Object o) {
    return getClazz().cast(o);
  }

}
