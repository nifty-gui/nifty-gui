package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ImageType.
 * @author void
 */
public class ImageType extends ElementType {

  /**
   * filename.
   * @required
   */
  private String filename;

  /**
   * filter.
   * @optional
   */
  private boolean filter;

  /**
   * create it.
   * @param filenameParam filename
   */
  public ImageType(final String filenameParam) {
    this.filename = filenameParam;
  }

  /**
   * set filter.
   * @param filterParam filter
   */
  public void setFilter(final Boolean filterParam) {
    this.filter = filterParam;
  }

  /**
   * create element.
   * @param parent parent
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param styleHandler styleHandler
   * @param time time
   * @param inputControl input control
   * @param screenController screenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    // create the image
    NiftyImage image = null;
    if (filename != null) {
      image = nifty.getRenderDevice().createImage(filename, filter);
    }

    // create the image renderer
    ImageRenderer imageRenderer = new ImageRenderer(image);

    // create a new element with the given renderer
    Element element = new Element(getAttributes().getId(), parent, screen, true, imageRenderer);
    super.addElementAttributes(
        element,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        styleHandler,
        time,
        inputControl,
        screenController);

    // set width and height to image width and height (for now)
    image = imageRenderer.getImage();
    if (image != null) {
      if (element.getConstraintWidth() == null) {
        element.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
      }
      if (element.getConstraintHeight() == null) {
        element.setConstraintHeight(new SizeValue(image.getHeight() + "px"));
      }
    }

    parent.add(element);
    return element;
  }
}
