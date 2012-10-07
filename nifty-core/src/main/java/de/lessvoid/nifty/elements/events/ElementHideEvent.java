package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;

public class ElementHideEvent implements NiftyEvent<Void> {
  private Element element;

  public ElementHideEvent(final Element element) {
    this.element = element;
  }

  public Element getElement() {
    return element;
  }
}
