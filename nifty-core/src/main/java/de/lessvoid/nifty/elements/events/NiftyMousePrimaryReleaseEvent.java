package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class NiftyMousePrimaryReleaseEvent extends NiftyMouseBaseEvent {
  public NiftyMousePrimaryReleaseEvent(final Element element) {
    super(element);
  }

  public NiftyMousePrimaryReleaseEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
