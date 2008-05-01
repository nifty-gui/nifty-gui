package de.lessvoid.nifty.loader.xpp3.elements.helper;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.ColorType;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Helper class to create Nifty objects.
 * @author void
 */
public final class NiftyCreator {

  /**
   * the logger.
   */
  private static Logger log = Logger.getLogger(NiftyCreator.class.getName());

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
   * @param backgroundImage backgroundImage
   * @param backgroundColor backgroundColor
   * @return element
   */
  public static Element createLayer(
      final String id,
      final de.lessvoid.nifty.Nifty nifty,
      final Screen screen,
      final String backgroundImage,
      final Color backgroundColor) {
    // create box
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderDevice().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderDevice().getHeight());

    // create element
    Element layer = new Element(
        id,
        null,
        layerLayout,
        screen,
        false,
        createPanelRenderer(nifty.getRenderDevice(), backgroundColor, backgroundImage));
    return layer;
  }

  /**
   * create panel.
   * @param id id
   * @param nifty nifty
   * @param screen screen
   * @param parent parent
   * @param backgroundImage backgroundImage
   * @param backgroundColor backgroundColor
   * @param visibleToMouse TODO
   * @return element
   */
  public static Element createPanel(
      final String id,
      final de.lessvoid.nifty.Nifty nifty,
      final Screen screen,
      final Element parent,
      final String backgroundImage,
      final Color backgroundColor,
      final boolean visibleToMouse) {
    PanelRenderer renderer = NiftyCreator.createPanelRenderer(
        nifty.getRenderDevice(),
        backgroundColor,
        backgroundImage);

    Element panel = new Element(
        id,
        parent,
        screen,
        visibleToMouse,
        renderer);
    return panel;
  }

  /**
   * create panel renderer.
   * @param r RenderDevice
   * @param backgroundColor backgroundColor
   * @param backgroundImage backgroundImage
   * @return PanelRenderer PanelRenderer
   */
  public static PanelRenderer createPanelRenderer(
      final RenderDevice r,
      final Color backgroundColor,
      final String backgroundImage) {
    PanelRenderer panelRenderer = new PanelRenderer();
    if (backgroundColor != null) {
      panelRenderer.setBackgroundColor(backgroundColor);
    }
    if (backgroundImage != null) {
      panelRenderer.setBackgroundImage(r.createImage(backgroundImage, true));
    }
    return panelRenderer;
  }

  /**
   * create text renderer.
   * @param nifty nifty
   * @param colorType color
   * @param text text
   * @param font font
   * @return TextRenderer
   */
  public static TextRenderer createTextRenderer(
      final Nifty nifty,
      final ColorType colorType,
      final String text,
      final String font) {
    TextRenderer textRenderer;
    if (colorType.isValid()) {
      textRenderer = new TextRenderer(
          nifty.getRenderDevice().createFont(font),
          text,
          colorType.createColor());
    } else {
      textRenderer = new TextRenderer(
          nifty.getRenderDevice().createFont(font),
          text);
    }
    return textRenderer;
  }

}
