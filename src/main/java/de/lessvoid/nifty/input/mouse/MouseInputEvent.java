package de.lessvoid.nifty.input.mouse;

/**
 * MouseInputEvent.
 * @author void
 */
public class MouseInputEvent {
  /**
   * mouse x.
   */
  private int mouseX;

  /**
   * mouse y.
   */
  private int mouseY;

  /**
   * left button.
   */
  private boolean leftButton;

  /**
   * @param newMouseX mouse x
   * @param newMouseY mouse y
   * @param newLeftButton leftButton
   */
  public MouseInputEvent(final int newMouseX, final int newMouseY, final boolean newLeftButton) {
    this.mouseX = newMouseX;
    this.mouseY = newMouseY;
    this.leftButton = newLeftButton;
  }

  /**
   * @return the mouseX
   */
  public int getMouseX() {
    return mouseX;
  }

  /**
   * @return the mouseY
   */
  public int getMouseY() {
    return mouseY;
  }

  /**
   * @return the leftButton
   */
  public boolean isLeftButton() {
    return leftButton;
  }

  public String toString() {
    return "mouseX = " + mouseX + ", mouseY = " + mouseY + ", leftButton = " + leftButton + ", initialLeftButtonDown = " + initialLeftButtonDown;
  }

  /**
   * This is a workaround and needs to be cleaned up. This class MouseInputEvent really should only
   * transport the event from the InputSystem into Nifty and needs to be translated into a
   * NiftyMouseInputEvent in Nifty which does not yet exsist. This needs to be fixed post Nifty 1.2
   * but will work for now.
   * NOTE: This method is called by Nifty. If you write a InputSystem implementation for Nifty
   * you can simply ignore it.
   */
  private boolean initialLeftButtonDown = false;

  public void setInitialLeftButtonDown() {
    initialLeftButtonDown = true;
  }

  public boolean isInitialLeftButtonDown() {
    return initialLeftButtonDown;
  }
}
