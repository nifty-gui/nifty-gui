package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseSecondaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseSecondaryClickedMovedEvent() {
    super();
  }

  public NiftyMouseSecondaryClickedMovedEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
