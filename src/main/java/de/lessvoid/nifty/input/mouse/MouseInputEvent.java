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
    return "mouseX = " + mouseX + ", mouseY = " + mouseY + ", leftButton = " + leftButton;
  }
}
