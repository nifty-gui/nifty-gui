package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.PopupType;

public class PopupCreator extends ControlAttributes {
  public PopupCreator() {
    setAutoId(NiftyIdCreator.generate());
  }

  public PopupCreator(final String id) {
    setId(id);
  }

  @Override
  public ElementType createType() {
    return new PopupType(attributes);
  }
}
