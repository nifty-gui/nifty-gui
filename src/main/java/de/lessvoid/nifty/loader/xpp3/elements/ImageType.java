package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImage.SubImageMode;
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
      final TimeProvider time,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    // create the image
    RenderImage image = nifty.getRenderDevice().createImage(filename, filter);

    // create the image renderer
    ImageRenderer imageRenderer = new ImageRenderer(image);

    // create a new element with the given renderer
    Element element = new Element(getId(), parent, screen, true, imageRenderer);

    // sub image enable?
    if (subImageSizeMode != null) {
      if (subImageSizeMode == SubImageSizeModeType.scale) {
        image.setSubImageMode(SubImageMode.Scale);
      } else if (subImageSizeMode == SubImageSizeModeType.resizeHint) {
        image.setSubImageMode(SubImageMode.ResizeHint);
      }
    }

    // resize hint available?
    if (resizeHint != null) {
      image.setResizeHint(resizeHint);
      image.setSubImageMode(SubImageMode.ResizeHint);
    }

    // set width and height to image width and height (for now)
    element.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
    element.setConstraintHeight(new SizeValue(image.getHeight() + "px"));

    super.addElementAttributes(
        element,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        time,
        inputControl,
        screenController);

    parent.add(element);
    return element;
  }

}
