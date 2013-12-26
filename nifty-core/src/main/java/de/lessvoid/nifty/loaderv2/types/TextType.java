package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class TextType extends ElementType {
  public TextType() {
    super();
  }

  public TextType(@Nonnull final TextType src) {
    super(src);
  }

  @Override
  @Nonnull
  public TextType copy() {
    return new TextType(this);
  }

  public TextType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  protected void makeFlat() {
    super.makeFlat();
    setTagName("<text>");
    setElementRendererCreator(new ElementRendererCreator() {
      @Override
      @Nonnull
      public ElementRenderer[] createElementRenderer(@Nonnull final Nifty nifty) {
        TextRenderer textRenderer = new TextRenderer(nifty);
        ElementRenderer[] panelRenderer = nifty.getRootLayerFactory().createPanelRenderer();
        ElementRenderer[] renderer = new ElementRenderer[panelRenderer.length + 1];
        System.arraycopy(panelRenderer, 0, renderer, 0, panelRenderer.length);
        renderer[panelRenderer.length] = textRenderer;
        return renderer;
      }
    });
  }

  //  public String output(final int offset) {
  //    return StringHelper.whitespace(offset) + "<text> " + super.output(offset);
  //  }

  //  public ElementRendererCreator getElementRendererBuilder() {
  //    return new ElementRendererCreator() {
  //      public ElementRenderer[] createElementRenderer(final Nifty nifty) {
  //        TextRenderer textRenderer = new TextRenderer();
  //        ElementRenderer[] panelRenderer = NiftyFactory.getPanelRenderer();
  //        ElementRenderer[] renderer = new ElementRenderer[panelRenderer.length + 1];
  //        for (int i = 0; i < panelRenderer.length; i++) {
  //          renderer[i] = panelRenderer[i];
  //        }
  //        renderer[panelRenderer.length] = textRenderer;
  //        return renderer;
  //      }
  //    };
  //  }
}
