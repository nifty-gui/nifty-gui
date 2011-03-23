package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class SecondaryClickMouseMethods extends MouseClickMethods {
  public SecondaryClickMouseMethods(final Element element) {
    super(element);
  }

  @Override
  public boolean onClick(final Nifty nifty, final String onClickAlternateKey, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMouseSecondaryClickedEvent(inputEvent));
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public void onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMouseSecondaryClickedMovedEvent(inputEvent));
    super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(final Nifty nifty) {
    publishEvent(nifty, new NiftyMouseSecondaryClickedEvent());
    super.onActivate(nifty);
  }

  @Override
  public void onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    publishEvent(nifty, new NiftyMouseSecondaryReleaseEvent(mouseEvent));
    super.onMouseRelease(nifty, mouseEvent);
  }

  private void publishEvent(final Nifty nifty, final NiftyEvent<?> event) {
    nifty.publishEvent(element.getId(), event);
  }
}
