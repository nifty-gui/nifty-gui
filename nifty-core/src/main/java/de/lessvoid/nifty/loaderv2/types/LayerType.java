package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class LayerType extends ElementType {
  public LayerType() {
    super();
  }

  public LayerType(@Nonnull final LayerType src) {
    super(src);
  }

  @Override
  @Nonnull
  public LayerType copy() {
    return new LayerType(this);
  }

  public LayerType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<layer>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Override
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }
}
