package de.lessvoid.nifty.api.input;

public class NiftyPointerEvent {
  private int x;
  private int y;
  private int z; // mouse wheel
  private int button;
  private boolean buttonDown;

  public int getX() {
    return x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  public int getZ() {
    return z;
  }

  public void setZ(final int z) {
    this.z = z;
  }

  public int getButton() {
    return button;
  }

  public void setButton(final int button) {
    this.button = button;
  }

  public boolean isButtonDown() {
    return buttonDown;
  }

  public void setButtonDown(final boolean buttonDown) {
    this.buttonDown = buttonDown;
  }

  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("x ").append("[").append(x).append("] ");
    out.append("y ").append("[").append(y).append("] ");
    out.append("z ").append("[").append(z).append("] ");
    out.append("button ").append("[").append(button).append("] ");
    out.append("buttonDown ").append("[").append(buttonDown).append("]");
    return out.toString();
  }
}
