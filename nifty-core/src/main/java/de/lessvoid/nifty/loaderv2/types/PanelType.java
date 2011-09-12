package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class PanelType extends ElementType {
  public PanelType() {
    super();
  }

  public PanelType(final PanelType src) {
    super(src);
  }

  public PanelType copy() {
    return new PanelType(this);
  }

  public PanelType(final Attributes attributes) {
    super(attributes);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<panel>");
    setElementRendererCreator(new ElementRendererCreator() {
      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
        return nifty.getRootLayerFactory().createPanelRenderer();
      }
    });
  }

//  public String output(final int offset) {
//    return StringHelper.whitespace(offset) + "<panel> " + super.output(offset);
//  }

//  public ElementRendererCreator getElementRendererBuilder() {
//    return new ElementRendererCreator() {
//      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
//        return NiftyFactory.getPanelRenderer();
//      }
//    };
//  }
}
