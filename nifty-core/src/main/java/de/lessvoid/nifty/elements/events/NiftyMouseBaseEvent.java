package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class NiftyMouseBaseEvent implements NiftyEvent {
  private final Element element;
  private final int mouseX;
  private final int mouseY;
  private final int mouseWheel;

  public NiftyMouseBaseEvent(final Element element) {
    this.element = element;
    this.mouseX = 0;
    this.mouseY = 0;
    this.mouseWheel = 0;
  }

  public NiftyMouseBaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
    this.mouseWheel = source.getMouseWheel();
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

  public int getMouseWheel() {
    return mouseWheel;
  }
}
