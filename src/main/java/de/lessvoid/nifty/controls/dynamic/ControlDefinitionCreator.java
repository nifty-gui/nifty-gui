package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.ElementType;

public class ControlDefinitionCreator extends ControlAttributes {
  public ControlDefinitionCreator(final String name) {
    setName(name);
  }

  @Override
  public ElementType createType() {
    return new ControlDefinitionType(attributes);
  }
}
