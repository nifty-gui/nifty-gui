package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;

import javax.annotation.Nonnull;

public class ElementShowEvent implements NiftyEvent {
  @Nonnull
  private final Element element;

  public ElementShowEvent(@Nonnull final Element element) {
    this.element = element;
  }

  @Nonnull
  public Element getElement() {
    return element;
  }
}
