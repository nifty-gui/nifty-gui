package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class PrimaryClickMouseMethods extends MouseClickMethods {
  public PrimaryClickMouseMethods(final Element element) {
    super(element);
  }

  @Override
  public void onInitialClick() {
    if (element.isFocusable()) {
      element.getFocusHandler().requestExclusiveMouseFocus(element);
      element.getFocusHandler().setKeyFocus(element);
    }
  }

  @Override
  public boolean onClick(final Nifty nifty, final String onClickAlternateKey, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMousePrimaryClickedEvent(inputEvent));
    element.startEffect(EffectEventId.onClick);
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public void onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMousePrimaryClickedMovedEvent(inputEvent));
    super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(final Nifty nifty) {
    publishEvent(nifty, new NiftyMousePrimaryClickedEvent());
    super.onActivate(nifty);
  }

  @Override
  public void onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    publishEvent(nifty, new NiftyMousePrimaryReleaseEvent(mouseEvent));
    element.stopEffect(EffectEventId.onClick);
    element.getFocusHandler().lostMouseFocus(element);
  }

  private void publishEvent(final Nifty nifty, final NiftyEvent<?> event) {
    nifty.publishEvent(element.getId(), event);
  }
}
