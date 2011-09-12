package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class PopupType extends ElementType {
  public PopupType() {
    super();
  }

  public PopupType(final PopupType src) {
    super(src);
  }

  public PopupType copy() {
    return new PopupType(this);
  }

  public PopupType(final Attributes attributes) {
    super(attributes);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<popup>");
    setElementRendererCreator(new ElementRendererCreator() {
      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }
}
