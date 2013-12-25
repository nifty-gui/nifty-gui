package de.lessvoid.nifty.elements.events;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class NiftyMouseTertiaryClickedEvent extends NiftyMouseBaseEvent {
  public NiftyMouseTertiaryClickedEvent(final Element element) {
    super(element);
  }

  public NiftyMouseTertiaryClickedEvent(final Element element, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    super(element, mouseEvent);
  }
}
