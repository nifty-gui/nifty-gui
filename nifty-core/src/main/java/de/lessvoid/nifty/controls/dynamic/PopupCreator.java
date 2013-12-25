package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.PopupType;

import javax.annotation.Nonnull;

public class PopupCreator extends ControlAttributes {
  public PopupCreator() {
    setAutoId();
  }

  public PopupCreator(@Nonnull final String id) {
    setId(id);
  }

  @Nonnull
  @Override
  public ElementType createType() {
    return new PopupType(getAttributes());
  }
}
