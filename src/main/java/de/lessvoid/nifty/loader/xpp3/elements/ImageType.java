package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

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
  public ImageType(final TypeContext typeContext,
      final String filenameParam) {
    super(typeContext);
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
   * @param screen screen
   * @param inputControl input control
   * @param screenController screenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Screen screen,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    // create the image
    NiftyImage image = null;
    if (filename != null) {
      image = typeContext.nifty.getRenderDevice().createImage(filename, filter);
    }

    // create the image renderer
    ImageRenderer imageRenderer = new ImageRenderer(image);

    // create a new element with the given renderer
    Element element = new Element(this, getAttributes().getId(), parent, screen, true, typeContext.time, imageRenderer);
    super.addElementAttributes(
        element,
        screen,
        screenController,
        inputControl);

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
