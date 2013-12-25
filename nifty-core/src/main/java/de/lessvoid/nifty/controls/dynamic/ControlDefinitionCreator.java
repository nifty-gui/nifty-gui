package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.ElementType;

import javax.annotation.Nonnull;

public class ControlDefinitionCreator extends ControlAttributes {
  public ControlDefinitionCreator(@Nonnull final String name) {
    setName(name);
  }

  @Nonnull
  @Override
  public ElementType createType() {
    return new ControlDefinitionType(getAttributes());
  }
}
