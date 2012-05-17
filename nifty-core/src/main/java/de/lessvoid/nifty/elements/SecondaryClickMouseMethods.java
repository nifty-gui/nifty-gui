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
    publishEvent(nifty, new NiftyMouseSecondaryClickedEvent(element, inputEvent));
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMouseSecondaryClickedMovedEvent(element, inputEvent));
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(final Nifty nifty) {
    publishEvent(nifty, new NiftyMouseSecondaryClickedEvent(element));
    super.onActivate(nifty);
  }

  @Override
  public boolean onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    publishEvent(nifty, new NiftyMouseSecondaryReleaseEvent(element, mouseEvent));
    return super.onMouseRelease(nifty, mouseEvent);
  }

  private void publishEvent(final Nifty nifty, final NiftyEvent event) {
    nifty.publishEvent(element.getId(), event);
  }
}
