package de.lessvoid.nifty.controls.scrollbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A slider control.
 * @author void
 */
public class SimpleSlider implements Controller {

  /**
   * the screen.
   */
  private Screen screen;

  /**
   * The element we're connected to.
   */
  private Element element;

  /**
   * The ControllerEventListener to use.
   */
  private ControllerEventListener listener;

  /**
   * The Element that shows the current position.
   */
  private Element scrollPos;

  /**
   * min value of scrollbar.
   */
  private float minValue;

  /**
   * max value of scrollbar.
   */
  private float maxValue;

  /**
   * current value of scrollbar.
   */
  private float currentValue;

  /**
   * snap to whole numbers (true) or not (false).
   */
  private boolean snap;

  private boolean fill;

  private boolean flip;

  /**
   * Bind this controller to the given element.
   * @param newScreen the new nifty to set
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
    this.element = newElement;
    this.listener = newListener;

    this.minValue = Float.parseFloat(properties.getProperty("minValue", "0.0"));
    this.maxValue = Float.parseFloat(properties.getProperty("maxValue", "1.0"));
    this.currentValue = Float.parseFloat(properties.getProperty("currentValue", "0.0"));
    this.snap = Boolean.parseBoolean(properties.getProperty("snap", "false"));
    this.fill = Boolean.parseBoolean(properties.getProperty("fill", "false"));
    this.flip = Boolean.parseBoolean(properties.getProperty("flip", "false"));
  }

  /**
   * On start screen event.
   */
  public void onStartScreen(final Screen screenParam) {
    this.screen = screenParam;
    this.scrollPos = element.findElementByName("scrollposition");
    changeSliderPos(currentValue);
  }

  /**
   * click.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
  public void click(final int mouseX, final int mouseY) {
    changeSliderPosFromMouse(mouseY);
  }

  /**
   * mouseMove.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
  public void mouseMove(final int mouseX, final int mouseY) {
    changeSliderPosFromMouse(mouseY);
  }

  /**
   * @param y TODO
   */
  private void changeSliderPosFromMouse(final int y) {
    int mousePosY = y - element.getY();
    if (mousePosY < 0) {
      mousePosY = 1;
    }

    if (mousePosY >= element.getHeight()) {
      mousePosY = element.getHeight() - 1;
    }

    float newValue = (mousePosY) / (float) element.getHeight() * (maxValue - minValue);
    changeSliderPos(newValue);
  }

  /**
   * Set the slider position from the given scroll bar value.
   * @param newValue the current value of the scroll bar must be in range [minValue, maxValue]
   */
  private void changeSliderPos(float newValue) {
    if (scrollPos == null) {
      return;
    }

    if (flip) {
      newValue = (maxValue - minValue) - newValue;
    }

    this.currentValue = newValue;

    if (snap) {
      this.currentValue = (int) newValue;
    }

    float t = currentValue / (maxValue - minValue);
    float newPos = element.getHeight() * t;

    if (!fill) {
      if (!snap) {
        newPos -= scrollPos.getHeight() / 2;
      }
    }

    if (newPos < 0) {
      newPos = 0;
    }

    if (!fill) {
      if ((newPos + scrollPos.getHeight()) > element.getHeight()) {
        newPos = element.getHeight() - scrollPos.getHeight();
      }
    }

    if (!fill) {
      scrollPos.setConstraintX(new SizeValue(0 + "px"));
      scrollPos.setConstraintY(new SizeValue((int) newPos + "px"));
    } else {
      scrollPos.setConstraintX(new SizeValue("0px"));

      if (snap) {
        t = (currentValue + 1) / (maxValue - minValue);
        if (t > 1.0f) {
          t = 1.0f;
        }
        newPos = element.getHeight() * t;
      }

      int y = (int) (element.getHeight() - newPos);
      scrollPos.setConstraintY(new SizeValue(y + "px"));
      int height = (int) (newPos);
      scrollPos.setConstraintHeight(new SizeValue(height + "px"));

      ImageRenderer imageRenderer = scrollPos.getRenderer(ImageRenderer.class);
//      imageRenderer.getImage().setSubImage(
//          0,
//          (int) (y / (float) element.getHeight() * imageRenderer.getImage().getHeight()),
//          scrollPos.getConstraintWidth().getValueAsInt(0),
//          (int) ((height) / (float) element.getHeight() * imageRenderer.getImage().getHeight())
//          );
    }

    listener.onChangeNotify();
    screen.layoutLayers();
  }

  /**
   * Get the current value of this scroll bar.
   * @return current value of scroll bar
   */
  public float getCurrentValue() {
    return currentValue;
  }

  public void onFocus(final boolean getFocus) {
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  
}

