package de.lessvoid.nifty.loader.xpp3.elements.helper;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Helper class to create Nifty objects.
 * @author void
 */
public final class NiftyCreator {
  /**
   * helper class can't be instantiated.
   */
  private NiftyCreator() {
  }

  /**
   * Create layer.
   * @param id id
   * @param nifty nifty
   * @param screen screen
   * @return element
   */
  public static Element createLayer(
      final String id,
      final de.lessvoid.nifty.Nifty nifty,
      final Screen screen) {
    // create box
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderDevice().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderDevice().getHeight());
    layerLayout.getBoxConstraints().setX(new SizeValue("0px"));
    layerLayout.getBoxConstraints().setY(new SizeValue("0px"));
    layerLayout.getBoxConstraints().setWidth(new SizeValue(nifty.getRenderDevice().getWidth() + "px"));
    layerLayout.getBoxConstraints().setHeight(new SizeValue(nifty.getRenderDevice().getHeight() + "px"));

    // create element
    Element layer = new Element(
        id,
        null,
        layerLayout,
        screen,
        false,
        new PanelRenderer());
    return layer;
  }

  /**
   * create panel.
   * @param id id
   * @param nifty nifty
   * @param screen screen
   * @param parent parent
   * @param visibleToMouse TODO
   * @return element
   */
  public static Element createPanel(
      final String id,
      final de.lessvoid.nifty.Nifty nifty,
      final Screen screen,
      final Element parent,
      final boolean visibleToMouse) {
    PanelRenderer renderer = new PanelRenderer();

    Element panel = new Element(
        id,
        parent,
        screen,
        visibleToMouse,
        renderer);
    return panel;
  }
}
