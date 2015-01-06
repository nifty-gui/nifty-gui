package de.lessvoid.nifty.api.converter;

import de.lessvoid.nifty.api.NiftyColor;

public class NiftyStyleStringConverterNiftyColor implements NiftyStyleStringConverter<NiftyColor> {

  @Override
  public NiftyColor fromString(final String value) {
    return NiftyColor.fromString(value);
  }

  @Override
  public String toString(final NiftyColor value) {
    return value.toHexString();
  }
}
