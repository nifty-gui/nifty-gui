package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseTertiaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryClickedMovedEvent(final Element element, final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
