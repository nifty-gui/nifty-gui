package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.TextType;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class TextCreator extends ControlAttributes {
  public TextCreator(@Nonnull final String text) {
    setAutoId();
    setText(text);
  }

  public TextCreator(@Nonnull final String id, @Nonnull final String text) {
    setId(id);
    setText(text);
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
        return createText(nifty, screen, parent);
      }
    });
  }

  @Nonnull
  @Override
  public ElementType createType() {
    return new TextType(getAttributes());
  }

  public void setWrap(final boolean wrap) {
    getAttributes().set("wrap", wrap ? "true" : "false");
  }
}
