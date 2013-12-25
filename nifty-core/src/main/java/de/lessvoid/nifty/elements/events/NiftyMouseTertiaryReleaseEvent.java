package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class NiftyMouseTertiaryReleaseEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryReleaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
