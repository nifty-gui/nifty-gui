package de.lessvoid.nifty.controls.imageSelect;

import java.util.ArrayList;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * ImageSelectControl.
 * @author void
 */
public class ImageSelectControl implements Controller {

  /**
   * nifty instance.
   */
  private Nifty nifty;

  /**
   * element.
   */
  private Element element;

  /**
   * images.
   */
  private ArrayList < NiftyImage > images;

  /**
   * currentImageIndex.
   */
  private int currentImageIndex;

  /**
   * Bind this controller to the given element.
   * @param niftyParam niftyParam
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty niftyParam,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    element = newElement;

    images = createImages(nifty.getRenderDevice(), properties.getProperty("imageList"));
    currentImageIndex = 0;
    updateVisuals();
  }

  /**
   * update visuals.
   */
  private void updateVisuals() {
    if (images.isEmpty()) {
      return;
    }

    NiftyImage currentImage = images.get(currentImageIndex);
    element.findElementByName("image").getRenderer(ImageRenderer.class).setImage(currentImage);
    element.findElementByName("image").setConstraintWidth(new SizeValue(currentImage.getWidth() + "px"));
    element.findElementByName("image").setConstraintHeight(new SizeValue(currentImage.getHeight() + "px"));
    element.findElementByName("image").layoutElements();

    if (currentImageIndex == 0) {
      element.findElementByName("back").hide();
    } else {
      element.findElementByName("back").show();
    }

    if (currentImageIndex == (images.size() - 1)) {
      element.findElementByName("forward").hide();
    } else {
      element.findElementByName("forward").show();
    }
  }

  /**
   * create NiftyImage list from given property.
   * @param renderDevice renderDevice
   * @param property property
   * @return NiftyImage list.
   */
  private ArrayList < NiftyImage > createImages(final NiftyRenderEngine renderDevice, final String property) {
    ArrayList < NiftyImage > imageList = new ArrayList < NiftyImage >();
    if (property != null && property.length() > 0) {
      String[] imageStrings = property.split(",");
      for (String imageString : imageStrings) {
        imageList.add(renderDevice.createImage(imageString, false));
      }
    }
    return imageList;
  }

  /**
   * Called when the screen is started.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
  }

  /**
   * This controller gets the focus.
   * @param getFocus get focus (true) or loose focus (false)
   */
  public void onFocus(final boolean getFocus) {
  }

  /**
   * input event.
 * @param inputEvent the NiftyInputEvent to process
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  /**
   * back click.
   */
  public void backClick() {
    if (currentImageIndex > 0) {
      currentImageIndex--;
      updateVisuals();
    }
  }

  /**
   * forward click.
   */
  public void forwardClick() {
    if (currentImageIndex < images.size() - 1) {
      currentImageIndex++;
      updateVisuals();
    }
  }
}
