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

public class RootLayerFactory {
  public Element createRootLayer(
      final String id,
      final Nifty nifty,
      final Screen screen,
      final TimeProvider time) {
    Element layer = new Element(
        nifty,
        new ElementType(),
        id,
        null,
        createRootLayerLayoutPart(nifty),
        screen.getFocusHandler(),
        false,
        time,
        createPanelRenderer());
    return layer;
  }

  public ElementRenderer[] createPanelRenderer() {
    ElementRenderer[] renderer = new ElementRenderer[2];
    renderer[0] = new ImageRenderer();
    renderer[1] = new PanelRenderer();
    return renderer;
  }

  public LayoutPart createRootLayerLayoutPart(final Nifty nifty) {
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderEngine().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderEngine().getHeight());
    layerLayout.getBoxConstraints().setX(new SizeValue("0px"));
    layerLayout.getBoxConstraints().setY(new SizeValue("0px"));
    layerLayout.getBoxConstraints().setWidth(new SizeValue(nifty.getRenderEngine().getWidth() + "px"));
    layerLayout.getBoxConstraints().setHeight(new SizeValue(nifty.getRenderEngine().getHeight() + "px"));
    return layerLayout;
  }
}
