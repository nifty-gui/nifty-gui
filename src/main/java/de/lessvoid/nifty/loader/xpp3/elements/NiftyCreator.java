package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.logging.Logger;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.loader.xpp3.Attributes;
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
   * @param nifty nifty
   * @param screen screen
   * @param attributes attributes
   * @return element
   */
  public static Element createLayer(
      final de.lessvoid.nifty.Nifty nifty,
      final Screen screen,
      final Attributes attributes) {
    // create box
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderDevice().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderDevice().getHeight());

    // create element
    Element layer = new Element(
        attributes.get("id"),
        null,
        layerLayout,
        screen,
        false,
        createPanelRenderer(nifty.getRenderDevice(), attributes));
    return layer;
  }

  /**
   * create panel.
   * @param r RenderDevice
   * @param attributes attributes
   * @return PanelRenderer PanelRenderer
   */
  public static PanelRenderer createPanelRenderer(
      final RenderDevice r,
      final Attributes attributes) {
    PanelRenderer panelRenderer = new PanelRenderer();

    if (attributes.isSet("backgroundColor")) {
      panelRenderer.setBackgroundColor(new Color(attributes.get("backgroundColor")));
    }

    if (attributes.isSet("backgroundImage")) {
      panelRenderer.setBackgroundImage(r.createImage(attributes.get("backgroundImage"), true));
    }

    return panelRenderer;
  }

  /**
   * process element attributes.
   * @param nifty nifty
   * @param element element
   * @param attributes attributes
   */
  public static void processElementAttributes(
      final de.lessvoid.nifty.Nifty nifty,
      final Element element,
      final Attributes attributes) {
    // attach to the given screen
    element.bindToScreen(nifty);

    // visible
    if (attributes.isSet("visible")) {
      if ("true".equals(attributes.get("visible").toString())) {
        element.show();
      } else {
        element.hide();
      }
    }

    // height
    if (attributes.isSet("height")) {
      SizeValue height = new SizeValue(attributes.get("height"));
      element.setConstraintHeight(height);
    }

    // width
    if (attributes.isSet("width")) {
      SizeValue width = new SizeValue(attributes.get("width"));
      element.setConstraintWidth(width);
    }

    // horizontal align
    if (attributes.isSet("align")) {
      element.setConstraintHorizontalAlign(HorizontalAlign.valueOf(attributes.get("align").toString()));
    }

    // vertical align
    if (attributes.isSet("valign")) {
      element.setConstraintVerticalAlign(VerticalAlign.valueOf(attributes.get("valign").toString()));
    }

    // child clip
    if (attributes.isSet("childClip")) {
      element.setClipChildren(attributes.get("childClip").equals("true"));
    }

    // visibleToMouse
    if (attributes.isSet("visibleToMouse")) {
      element.setVisibleToMouseEvents(attributes.get("visibleToMouse").equals("true"));
    }

    // childLayout
    if (attributes.isSet("childLayout")) {
      String layout = attributes.get("childLayout");
      if (layout.equals("absolute")) {
        element.setLayoutManager(new AbsolutePositionLayout());
      } else if (layout.equals("vertical")) {
        element.setLayoutManager(new VerticalLayout());
      } else if (layout.equals("center")) {
        element.setLayoutManager(new CenterLayout());
      } else if (layout.equals("horizontal")) {
        element.setLayoutManager(new HorizontalLayout());
      } else if (layout.equals("overlay")) {
        element.setLayoutManager(new OverlayLayout());
      } else {
        log.warning("unsupported layout: " + layout.toString());
      }
    }
  }
}
