package de.lessvoid.nifty.loader.xpp3.elements.helper;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loader.xpp3.elements.AttributesType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

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
   * get panel renderer.
   * @param nifty nifty
   * @param attributes attributes type
   * @return ElementRenderer array
   */
  public static ElementRenderer[] getPanelRenderer(final Nifty nifty, final AttributesType attributes) {
    // build list of renderer
    List < ElementRenderer > renderer = new ArrayList < ElementRenderer >();

    // create the image
    renderer.add(new ImageRenderer(null));

    // create the image renderer
    renderer.add(new PanelRenderer());
    return renderer.toArray(new ElementRenderer[0]);
  }

  /**
   * Create layer.
   * @param root root element
   * @param elementType element type
   * @param id id
   * @param nifty nifty
   * @param screen screen
   * @param attributes attributes
   * @param time TimeProvider
   * @return element
   */
  public static Element createLayer(
      final Element root,
      final ElementType elementType,
      final String id,
      final Nifty nifty,
      final Screen screen,
      final AttributesType attributes,
      final TimeProvider time) {
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
        nifty,
        elementType,
        id,
        root,
        layerLayout,
        screen,
        false,
        time,
        getPanelRenderer(nifty, attributes));
    return layer;
  }
}
