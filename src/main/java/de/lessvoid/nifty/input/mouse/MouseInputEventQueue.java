package de.lessvoid.nifty.input.mouse;


/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private boolean lastMouseDown = false;
  private boolean hadAnyEvents = false;

  public void reset() {
    lastMouseDown = false;
  }

  public void begin() {
    hadAnyEvents = false;
  }

  public boolean canProcess(final MouseInputEvent mouseEvent) {
    hadAnyEvents = true;

    int mouseX = mouseEvent.getMouseX();
    int mouseY = mouseEvent.getMouseY();
    boolean leftButton = mouseEvent.isLeftButton();

    if (mouseX != lastMouseX || mouseY != lastMouseY || leftButton != lastMouseDown) {
      return true;
    }
    return false;
  }

  public void process(final MouseInputEvent mouse) {
    lastMouseX = mouse.getMouseX();
    lastMouseY = mouse.getMouseY();
    lastMouseDown = mouse.isLeftButton();
  }

  public boolean hasLastMouseDownEvent() {
    return !hadAnyEvents && lastMouseDown;
  }

  public MouseInputEvent getLastMouseDownEvent() {
    return new MouseInputEvent(lastMouseX, lastMouseY, lastMouseDown);
  }
}
