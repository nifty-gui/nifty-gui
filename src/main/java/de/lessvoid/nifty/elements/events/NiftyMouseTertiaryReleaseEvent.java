package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseTertiaryReleaseEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryReleaseEvent() {
    super();
  }

  public NiftyMouseTertiaryReleaseEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
