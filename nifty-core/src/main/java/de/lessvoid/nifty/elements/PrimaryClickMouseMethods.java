package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;

public class PrimaryClickMouseMethods extends MouseClickMethods {
  public PrimaryClickMouseMethods(@Nonnull final Element element) {
    super(element);
  }

  @Override
  public void onInitialClick() {
    element.getFocusHandler().requestExclusiveMouseFocus(element);
    if (element.isFocusable()) {
      element.getFocusHandler().setKeyFocus(element);
    }
  }

  @Override
  public boolean onClick(
      @Nonnull final Nifty nifty,
      final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMousePrimaryClickedEvent(element, inputEvent));
    }
    element.startEffectWithoutChildren(EffectEventId.onClick);
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMousePrimaryClickedMovedEvent(element, inputEvent));
    }
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public void onActivate(@Nonnull final Nifty nifty) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMousePrimaryClickedEvent(element));
    }
    super.onActivate(nifty);
  }

  @Override
  public boolean onMouseRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMousePrimaryReleaseEvent(element, mouseEvent));
    }
    boolean result = super.onMouseRelease(nifty, mouseEvent);
    element.stopEffectWithoutChildren(EffectEventId.onClick);
    element.getFocusHandler().lostMouseFocus(element);
    return result;
  }
}
