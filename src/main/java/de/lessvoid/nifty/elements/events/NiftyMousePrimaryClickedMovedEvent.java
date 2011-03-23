package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;


public class NiftyMousePrimaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMousePrimaryClickedMovedEvent() {
    super();
  }

  public NiftyMousePrimaryClickedMovedEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
