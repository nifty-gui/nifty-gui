package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseWheelEvent implements NiftyEvent {
  private Element element;
  private int mouseWheel;

  public NiftyMouseWheelEvent(final Element element, final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseWheel = source.getMouseWheel();
  }

  public Element getElement() {
    return element;
  }

  public int getMouseWheel() {
    return mouseWheel;
  }
}
