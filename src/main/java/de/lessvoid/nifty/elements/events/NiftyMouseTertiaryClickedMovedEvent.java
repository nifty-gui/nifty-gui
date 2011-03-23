package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseTertiaryClickedMovedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryClickedMovedEvent() {
    super();
  }

  public NiftyMouseTertiaryClickedMovedEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
