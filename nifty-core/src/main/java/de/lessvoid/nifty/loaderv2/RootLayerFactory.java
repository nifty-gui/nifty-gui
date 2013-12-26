package de.lessvoid.nifty.loaderv2;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;

public class RootLayerFactory {
  @Nonnull
  public Element createRootLayer(
      @Nonnull final String id,
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final TimeProvider time) {
    return new Element(
        nifty,
        new ElementType(),
        id,
        null,
        createRootLayerLayoutPart(nifty),
        screen.getFocusHandler(),
        false,
        time,
        createPanelRenderer());
  }

  @Nonnull
  public ElementRenderer[] createPanelRenderer() {
    ElementRenderer[] renderer = new ElementRenderer[2];
    renderer[0] = new ImageRenderer();
    renderer[1] = new PanelRenderer();
    return renderer;
  }

  @Nonnull
  public LayoutPart createRootLayerLayoutPart(@Nonnull final Nifty nifty) {
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderEngine().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderEngine().getHeight());
    layerLayout.getBoxConstraints().setX(SizeValue.px(0));
    layerLayout.getBoxConstraints().setY(SizeValue.px(0));
    layerLayout.getBoxConstraints().setWidth(SizeValue.px(nifty.getRenderEngine().getWidth()));
    layerLayout.getBoxConstraints().setHeight(SizeValue.px(nifty.getRenderEngine().getHeight()));
    return layerLayout;
  }
}
