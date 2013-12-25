package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class PopupType extends ElementType {
  public PopupType() {
    super();
  }

  public PopupType(@Nonnull final PopupType src) {
    super(src);
  }

  @Override
  @Nonnull
  public PopupType copy() {
    return new PopupType(this);
  }

  public PopupType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<popup>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Nonnull
      @Override
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }
}
