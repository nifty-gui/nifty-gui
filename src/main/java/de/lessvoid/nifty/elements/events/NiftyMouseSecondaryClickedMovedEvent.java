package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseSecondaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseSecondaryClickedMovedEvent(final Element element, final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
