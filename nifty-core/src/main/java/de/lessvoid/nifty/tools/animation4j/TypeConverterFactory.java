package de.lessvoid.nifty.tools.animation4j;

public final class TypeConverterFactory {
  private final static ColorTypeConverter colorTypeConverter = new ColorTypeConverter();

  public final static ColorTypeConverter getColorTypeConverter() {
    return colorTypeConverter;
  }
}
