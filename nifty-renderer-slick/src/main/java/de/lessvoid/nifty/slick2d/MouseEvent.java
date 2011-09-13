package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.NiftyInputConsumer;

public class MouseEvent {
  private int mouseX;
  private int mouseY;
  private int mouseWheel;
  private int button;
  private boolean buttonDown;

  public MouseEvent(final int mouseX, final int mouseY, final boolean mouseDown, final int mouseButton) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.buttonDown = mouseDown;
    this.button = mouseButton;
    this.mouseWheel = 0;
  }

  public void processMouseEvents(final NiftyInputConsumer inputEventConsumer) {
    inputEventConsumer.processMouseEvent(mouseX, mouseY, mouseWheel, button, buttonDown);
  }
}
