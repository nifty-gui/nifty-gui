package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;


public class NiftyMousePrimaryClickedEvent extends NiftyMouseBaseEvent {
  public NiftyMousePrimaryClickedEvent() {
    super();
  }

  public NiftyMousePrimaryClickedEvent(final NiftyMouseInputEvent mouseEvent) {
    super(mouseEvent);
  }
}
