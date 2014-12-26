package de.lessvoid.nifty.api.annotation;

import de.lessvoid.nifty.api.NiftyColor;

public class NiftyCssStringConverterNiftyColor implements NiftyCssStringConverter<NiftyColor> {

  @Override
  public NiftyColor fromString(final String value) {
    return NiftyColor.fromString(value);
  }

  @Override
  public String toString(final NiftyColor value) {
    return value.toHexString();
  }
}
