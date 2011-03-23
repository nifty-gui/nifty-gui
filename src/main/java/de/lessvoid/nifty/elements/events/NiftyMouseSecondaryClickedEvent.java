package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseSecondaryClickedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseSecondaryClickedEvent() {
    super();
  }

  public NiftyMouseSecondaryClickedEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
