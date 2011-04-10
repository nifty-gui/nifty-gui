package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class TertiaryClickMouseMethods extends MouseClickMethods {
  public TertiaryClickMouseMethods(final Element element) {
    super(element);
  }

  @Override
  public boolean onClick(final Nifty nifty, final String onClickAlternateKey, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMouseTertiaryClickedEvent(inputEvent));
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMouseTertiaryClickedMovedEvent(inputEvent));
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(final Nifty nifty) {
    publishEvent(nifty, new NiftyMouseTertiaryClickedEvent());
    super.onActivate(nifty);
  }

  @Override
  public boolean onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    publishEvent(nifty, new NiftyMouseTertiaryReleaseEvent(mouseEvent));
    return super.onMouseRelease(nifty, mouseEvent);
  }

  private void publishEvent(final Nifty nifty, final NiftyEvent<?> event) {
    nifty.publishEvent(element.getId(), event);
  }
}
