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
    publishEvent(nifty, new NiftyMousePrimaryClickedEvent(element, inputEvent));
    element.startEffectWithoutChildren(EffectEventId.onClick);
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onClickMouseMove(final Nifty nifty, final NiftyMouseInputEvent inputEvent) {
    publishEvent(nifty, new NiftyMousePrimaryClickedMovedEvent(element, inputEvent));
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(final Nifty nifty) {
    publishEvent(nifty, new NiftyMousePrimaryClickedEvent(element));
    super.onActivate(nifty);
  }

  @Override
  public boolean onMouseRelease(final Nifty nifty, final NiftyMouseInputEvent mouseEvent) {
    publishEvent(nifty, new NiftyMousePrimaryReleaseEvent(element, mouseEvent));
    boolean result = super.onMouseRelease(nifty, mouseEvent);
    element.stopEffectWithoutChildren(EffectEventId.onClick);
    element.getFocusHandler().lostMouseFocus(element);
    return result;
  }

  private void publishEvent(final Nifty nifty, final NiftyEvent<?> event) {
    nifty.publishEvent(element.getId(), event);
  }
}
