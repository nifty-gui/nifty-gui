package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class LabelType extends TextType {
  public LabelType() {
    super();
  }

  public LabelType(final Attributes attributes) {
    initFromAttributes(attributes);
  }

  protected void makeFlat() {
    super.makeFlat();
    setTagName("<label>");
    setElementRendererCreator(new ElementRendererCreator() {
      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
        TextRenderer textRenderer = new TextRenderer();
        ElementRenderer[] panelRenderer = nifty.getRootLayerFactory().createPanelRenderer();
        ElementRenderer[] renderer = new ElementRenderer[panelRenderer.length + 1];
        for (int i = 0; i < panelRenderer.length; i++) {
          renderer[i] = panelRenderer[i];
        }
        renderer[panelRenderer.length] = textRenderer;
        return renderer;
      }
    });
  }

//  public String output(final int offset) {
//    return StringHelper.whitespace(offset) + "<label> " + super.output(offset);
//  }

  public void initFromAttributes(final Attributes attributesParam) {
    super.initFromAttributes(attributesParam);
    if (getAttributes().get("style") == null) {
      getAttributes().set("style", "nifty-label");
    }
  }
}
