package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImage.SubImageMode;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * ImageType.
 * @author void
 */
public class ImageType implements XmlElementProcessor {

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  /**
   * parent.
   */
  private Element parent;

  /**
   * effects.
   */
  private Map < String, Class < ? > > registeredEffects;

  /**
   * ScreenController.
   */
  private ScreenController screenController;

  /**
   * ImageType.
   * @param niftyParam nifty
   * @param screenParam screenParam
   * @param parentParam parentParam
   * @param registeredEffectsParam registeredEffectsParam
   * @param screenControllerParam ScreenController
   */
  public ImageType(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element parentParam,
      final Map < String, Class < ? > > registeredEffectsParam,
      final ScreenController screenControllerParam) {
    nifty = niftyParam;
    screen = screenParam;
    parent = parentParam;
    this.registeredEffects = registeredEffectsParam;
    this.screenController = screenControllerParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    // create the actual image
    boolean filter = true;
    if (attributes.isSet("filter")) {
      filter = "true".equals(attributes.get("filter"));
    }

    // create the image
    RenderImage image = nifty.getRenderDevice().createImage(attributes.get("filename"), filter);

    // create the image renderer
    ImageRenderer imageRenderer = new ImageRenderer(image);

    // create a new element with the given renderer
    Element element = new Element(attributes.get("id"), parent, screen, true, imageRenderer);

    // set absolute x position when given
    if (attributes.isSet("x")) {
      element.setConstraintX(new SizeValue(attributes.get("x") + "px"));
    }

    // set absolute y position when given
    if (attributes.isSet("y")) {
      element.setConstraintY(new SizeValue(attributes.get("y") + "px"));
    }

    // sub image enable?
    if (attributes.isSet("subImageSizeMode")) {
      if (attributes.get("subImageSizeMode").equals("scale")) {
        image.setSubImageMode(SubImageMode.Scale);
      } else if (attributes.get("subImageSizeMode").equals("resizeHint")) {
        image.setSubImageMode(SubImageMode.ResizeHint);
      }
    }

    // resize hint available?
    if (attributes.isSet("resizeHint")) {
      image.setResizeHint(attributes.get("resizeHint"));
      image.setSubImageMode(SubImageMode.ResizeHint);
    }

    // set width and height to image width and height (for now)
    element.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
    element.setConstraintHeight(new SizeValue(image.getHeight() + "px"));

    NiftyCreator.processElementAttributes(nifty, element, attributes);
    parent.add(element);

    ElementType.processChildElements(xmlParser, nifty, screen, element, registeredEffects, screenController);
  }
}
