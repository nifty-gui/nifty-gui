package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;


public class NiftyMousePrimaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMousePrimaryClickedMovedEvent(final Element element, final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
