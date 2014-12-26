package de.lessvoid.nifty.api.annotation;

public class NiftyCssStringConverterDefault implements NiftyCssStringConverter<Object> {

  @Override
  public Object fromString(String value) {
    return value;
  }

  @Override
  public String toString(Object value) {
    return value.toString();
  }
}
