package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseBaseEvent implements NiftyEvent<Void> {
  private int mouseX;
  private int mouseY;
  private int mouseWheel;

  public NiftyMouseBaseEvent() {
    this.mouseX = 0;
    this.mouseY = 0;
    this.mouseWheel = 0;
  }

  public NiftyMouseBaseEvent(final NiftyMouseInputEvent source) {
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
    this.mouseWheel = source.getMouseWheel();
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

  public int getMouseWheel() {
    return mouseWheel;
  }
}
