package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImageSubImageMode;
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
   * subImageSizeMode.
   * @optional
   */
  private SubImageSizeModeType subImageSizeMode;

  /**
   * resizeHint.
   * @optional
   */
  private String resizeHint;

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
   * set subImageSizeMode.
   * @param subImageSizeModeParam mode
   */
  public void setSubImageSizeMode(final SubImageSizeModeType subImageSizeModeParam) {
    this.subImageSizeMode = subImageSizeModeParam;
  }

  /**
   * set resize hint.
   * @param resizeHintParam resize hint
   */
  public void setResizeHint(final String resizeHintParam) {
    this.resizeHint = resizeHintParam;
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
    RenderImage image = null;
    if (filename != null) {
      image = nifty.getRenderDevice().createImage(filename, filter);
    }

    // create the image renderer
    ImageRenderer imageRenderer = new ImageRenderer(image);

    // create a new element with the given renderer
    Element element = new Element(getAttributes().getId(), parent, screen, true, imageRenderer);

    // sub image enable?
    if (image != null && subImageSizeMode != null) {
      if (subImageSizeMode == SubImageSizeModeType.scale) {
        image.setSubImageMode(RenderImageSubImageMode.SCALE());
      } else if (subImageSizeMode == SubImageSizeModeType.resizeHint) {
        image.setSubImageMode(RenderImageSubImageMode.RESIZE());
      }
    }

    // resize hint available?
    if (image != null && resizeHint != null) {
      image.setResizeHint(resizeHint);
      image.setSubImageMode(RenderImageSubImageMode.RESIZE());
    }
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
      element.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
      element.setConstraintHeight(new SizeValue(image.getHeight() + "px"));
    } else {
      element.setConstraintWidth(new SizeValue("1px"));
      element.setConstraintHeight(new SizeValue("1px"));
    }

    parent.add(element);
    return element;
  }
}
