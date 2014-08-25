package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryClickedMovedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryMultiClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseTertiaryReleaseEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TertiaryClickMouseMethods extends MouseClickMethods {
  public TertiaryClickMouseMethods(@Nonnull final Element element) {
    super(element);
  }

  @Override
  public boolean onClick(
      @Nonnull final Nifty nifty,
      @Nullable final String onClickAlternateKey,
      @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedEvent(element, inputEvent));
    }
    return super.onClick(nifty, onClickAlternateKey, inputEvent);
  }

  @Override
  public boolean onMultiClick(
          @Nonnull final Nifty nifty,
          @Nullable final String onClickAlternateKey,
          @Nonnull final NiftyMouseInputEvent inputEvent,
          int clickCount) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseTertiaryMultiClickedEvent(element, inputEvent,clickCount));
    }
    element.startEffectWithoutChildren(EffectEventId.onClick);
    return super.onMultiClick(nifty, onClickAlternateKey, inputEvent, clickCount);
  }

  @Override
  public boolean onClickMouseMove(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent inputEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedMovedEvent(element, inputEvent));
    }
    return super.onClickMouseMove(nifty, inputEvent);
  }

  @Override
  public boolean onRelease(@Nonnull final Nifty nifty, @Nonnull final NiftyMouseInputEvent mouseEvent) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseTertiaryReleaseEvent(element, mouseEvent));
    }
    return super.onRelease(nifty, mouseEvent);
  }

  @Override
  public void clickAndRelease(@Nonnull final Nifty nifty) {
    String id = element.getId();
    if (id != null) {
      nifty.publishEvent(id, new NiftyMouseTertiaryClickedEvent(element));
      nifty.publishEvent(id, new NiftyMouseTertiaryReleaseEvent(element));
    }
    super.clickAndRelease(nifty);
  }
}
