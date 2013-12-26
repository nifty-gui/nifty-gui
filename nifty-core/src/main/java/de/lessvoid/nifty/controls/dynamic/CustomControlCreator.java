package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class CustomControlCreator extends ControlAttributes {
  public CustomControlCreator(@Nonnull final ControlType source) {
    super(source);
  }

  public CustomControlCreator(@Nonnull final String name) {
    setAutoId();
    setName(name);
  }

  public CustomControlCreator(@Nonnull final String id, @Nonnull final String name) {
    setId(id);
    setName(name);
  }

  public void parameter(@Nonnull final String name, @Nonnull final String value) {
    set(name, value);
  }

  @Nonnull
  public Element create(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    return nifty.addControl(screen, parent, getStandardControl());
  }

  @Nonnull
  @Override
  public ElementType createType() {
    return new ControlType(getAttributes());
  }
}
