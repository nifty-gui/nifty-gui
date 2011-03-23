package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMousePrimaryReleaseEvent extends NiftyMouseBaseEvent {
  public NiftyMousePrimaryReleaseEvent() {
    super();
  }

  public NiftyMousePrimaryReleaseEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
