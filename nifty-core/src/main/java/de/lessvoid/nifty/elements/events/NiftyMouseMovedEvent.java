package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseMovedEvent implements NiftyEvent {
  private Element element;
  private int mouseX;
  private int mouseY;

  public NiftyMouseMovedEvent(final Element element, final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
  }

  public Element getElement() {
    return element;
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }
}
