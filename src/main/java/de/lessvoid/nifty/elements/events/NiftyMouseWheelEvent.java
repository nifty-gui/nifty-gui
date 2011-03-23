package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseWheelEvent implements NiftyEvent<Void> {
  private int mouseWheel;

  public NiftyMouseWheelEvent(final NiftyMouseInputEvent source) {
    this.mouseWheel = source.getMouseWheel();
  }

  public int getMouseWheel() {
    return mouseWheel;
  }
}
