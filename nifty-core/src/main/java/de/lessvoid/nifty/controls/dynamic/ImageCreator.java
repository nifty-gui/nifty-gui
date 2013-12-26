package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.ImageType;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class ImageCreator extends ControlAttributes {
  public ImageCreator() {
    setAutoId();
  }

  public ImageCreator(@Nonnull final String id) {
    setId(id);
  }

  @Nonnull
  public Element create(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    return nifty.addControl(screen, parent, new StandardControl() {
      @Nonnull
      @Override
      public Element createControl(
          @Nonnull final Nifty nifty,
          @Nonnull final Screen screen,
          @Nonnull final Element parent) {
        return createImage(nifty, screen, parent);
      }
    });
  }

  @Override
  @Nonnull
  public ElementType createType() {
    return new ImageType(getAttributes());
  }
}
