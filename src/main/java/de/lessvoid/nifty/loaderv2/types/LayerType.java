package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.NiftyFactory;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class LayerType extends ElementType {
  public LayerType() {
    super();
  }

  public LayerType(final LayerType src) {
    super(src);
  }

  public LayerType copy() {
    return new LayerType(this);
  }

  public LayerType(final Attributes attributes) throws Exception {
    super(attributes);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<layer>");
    setElementRendererCreator(new ElementRendererCreator() {
      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
        return NiftyFactory.getPanelRenderer();
      }
    });
  }

//  public String output(final int offset) {
//    return StringHelper.whitespace(offset) + "<layer> " + super.output(offset + 1);
//  }

//  public ElementRendererCreator getElementRendererBuilder() {
//    return new ElementRendererCreator() {
//      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
//        return NiftyFactory.getPanelRenderer();
//      }
//    };
//  }
}
