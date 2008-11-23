package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * A slider control.
 * @author void
 */
public class Scrollbar implements Controller {

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
  private float viewMinValue;

  /**
   * max value of scrollbar.
   */
  private float viewMaxValue;

  /**
   * current value of scrollbar.
   */
  private float currentValue;

  /**
   * snap to whole numbers (true) or not (false).
   */
  private boolean snap;

  /**
   * world min.
   */
  private float worldMinValue;

  /**
   * world max.
   */
  private float worldMaxValue;

  /**
   * start mouse y.
   */
  private int startMouseY;

  /**
   * page size.
   */
  private float pageSize;

  /**
   * current value.
   */
  private int currentValueView;

  /**
   * Bind this controller to the given element.
   * @param nifty nifty
   * @param newElement the new element to set
   * @param properties all attributes of the xml tag we're connected to
   * @param newListener listener
   */
  public void bind(
      final Nifty nifty,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    this.element = newElement;
    this.listener = newListener;

    this.viewMinValue = Float.parseFloat(properties.getProperty("viewMinValue", "0.0"));
    this.viewMaxValue = Float.parseFloat(properties.getProperty("viewMaxValue", "1.0"));
    this.worldMinValue = Float.parseFloat(properties.getProperty("worldMinValue", "0.0"));
    this.worldMaxValue = Float.parseFloat(properties.getProperty("worldMaxValue", "1.0"));
    this.currentValue = Float.parseFloat(properties.getProperty("currentValue", "0.0"));
    this.snap = Boolean.parseBoolean(properties.getProperty("snap", "false"));
  }

  /**
   * On start screen event.
   * @param newScreen screen
   */
  public void onStartScreen(final Screen newScreen) {
    this.screen = newScreen;

    this.scrollPos = element.findElementByName("scrollposition");
    this.element = element.findElementByName("scrollbar");

    calcPageSize();
    changeSliderPos(currentValue);
  }

  /**
   * calc page size.
   */
  private void calcPageSize() {
    float worldSize = (worldMaxValue - worldMinValue);
    float pages = worldSize / (viewMaxValue - viewMinValue);

    if (pages < 1.0f) {
      pages = 1.0f;
    }

    pageSize = worldSize / pages;
    scrollPos.setConstraintHeight(new SizeValue((int) (element.getHeight() / pages) + "px"));
  }

  /**
   * click.
   * @param mouseX the mouse x position
   * @param mouseY the mouse y position
   */
  public void click(final int mouseX, final int mouseY) {
    // check if we're above or below the scrollPos
    if (mouseY < scrollPos.getY()) {
      changeSliderPos(currentValue - pageSize);
    }

    if (mouseY > scrollPos.getY() + scrollPos.getHeight()) {
      changeSliderPos(currentValue + pageSize);
    }
  }

  /**
   * mouse move start.
   * @param mouseX x
   * @param mouseY y
   */
  public void mouseMoveStart(final int mouseX, final int mouseY) {
    startMouseY = mouseY - scrollPos.getY();
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
   * up click.
   * @param mouseX x
   * @param mouseY y
   */
  public void upClick(final int mouseX, final int mouseY) {
    currentValue--;
    changeSliderPos(currentValue);
  }

  /**
   * down click.
   * @param mouseX x
   * @param mouseY y
   */
  public void downClick(final int mouseX, final int mouseY) {
    currentValue++;
    changeSliderPos(currentValue);
  }

  /**
   * @param y TODO
   */
  private void changeSliderPosFromMouse(final int y) {
    int newPos = y - element.getY() - startMouseY;

    if (newPos < 0) {
      newPos = 0;
    }

    if (newPos > element.getHeight() - scrollPos.getHeight()) {
      newPos = element.getHeight() - scrollPos.getHeight();
    }

    scrollPos.setConstraintX(new SizeValue(0 + "px"));
    scrollPos.setConstraintY(new SizeValue(newPos + "px"));

    listener.onChangeNotify();
    screen.layoutLayers();

    currentValue = viewToWorld(newPos);
  }

  /**
   * view to world.
   * @param viewValue view value
   * @return view to world
   */
  private float viewToWorld(final float viewValue) {
    return ((viewValue - 0) / (element.getHeight() - 0)) * (worldMaxValue - worldMinValue) + worldMinValue;
  }

  /**
   * world to view.
   * @param worldValue word value
   * @return world to view
   */
  private float worldToView(final float worldValue) {
    return ((worldValue - worldMinValue) / (worldMaxValue - worldMinValue)) * (element.getHeight() - 0) + 0;
  }

  /**
   * Set the slider position from the given scroll bar value.
   * @param worldValue the current value of the scroll bar must be in range [minValue, maxValue]
   */
  private void changeSliderPos(final float worldValue) {
    if (scrollPos == null) {
      return;
    }

    this.currentValue = worldValue;
    if (currentValue < 0) {
      currentValue = 0;
    }

    int s = element.getHeight() - scrollPos.getHeight();
    float a = viewToWorld(s);
    if (currentValue > a) {
      currentValue = a;
    }

    float newPos = worldToView(currentValue);

    scrollPos.setConstraintX(new SizeValue(0 + "px"));
    scrollPos.setConstraintY(new SizeValue((int) newPos + "px"));

    listener.onChangeNotify();
    screen.layoutLayers();

    currentValueView = (int) newPos;
  }

  /**
   * Get the current value of this scroll bar.
   * @return current value of scroll bar
   */
  public float getCurrentValue() {
    return currentValue;
  }

  /**
   * input event.
   * @param inputEvent event
   */
  public void inputEvent(final NiftyInputEvent inputEvent) {
  }

  /**
   * on focus.
   * @param getFocus focus
   */
  public void onFocus(final boolean getFocus) {
  }
}
