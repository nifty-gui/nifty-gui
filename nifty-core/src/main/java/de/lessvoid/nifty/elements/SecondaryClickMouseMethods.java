package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class SecondaryClickMouseMethods extends MouseClickMethods {
  public SecondaryClickMouseMethods(@Nonnull final Element element) {
    super(element);
  }

  @Override
  public boolean onClick(
      @Nonnull final Nifty nifty,
      final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedEvent(element, inputEvent));
    }
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedMovedEvent(element, inputEvent));
    }
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(@Nonnull final Nifty nifty) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryClickedEvent(element));
    }
    super.onActivate(nifty);
  }

  @Override
  public boolean onMouseRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseSecondaryReleaseEvent(element, mouseEvent));
    }
    return super.onMouseRelease(nifty, mouseEvent);
  }
}
