package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseSecondaryReleaseEvent extends NiftyMouseBaseEvent {
  public NiftyMouseSecondaryReleaseEvent() {
    super();
  }

  public NiftyMouseSecondaryReleaseEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
