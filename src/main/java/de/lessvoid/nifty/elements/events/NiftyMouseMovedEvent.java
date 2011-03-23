package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseMovedEvent implements NiftyEvent<Void> {
  private int mouseX;
  private int mouseY;

  public NiftyMouseMovedEvent(final NiftyMouseInputEvent source) {
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }
}
