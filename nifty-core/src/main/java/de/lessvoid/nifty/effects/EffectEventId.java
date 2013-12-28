package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.effects.EffectProcessorImpl.Notify;

import javax.annotation.Nonnull;

/**
 * This enum contains all the ID's of effect events known.
 *
 * @author void
 */
public enum EffectEventId {
  onStartScreen(false),
  onEndScreen(true),
  onFocus(true),
  onGetFocus(false),
  onLostFocus(false),
  onClick(false),
  onHover(true),
  onStartHover(false),
  onEndHover(false),
  onActive(true),
  onCustom(false),
  onHide(true),
  onShow(false),
  onEnabled(true),
  onDisabled(true);

  private final boolean neverStopRendering;

  private EffectEventId(final boolean neverStopRendering) {
    this.neverStopRendering = neverStopRendering;
  }

  @Nonnull
  EffectProcessor createEffectProcessor(@Nonnull final Notify notify) {
    return new EffectProcessorImpl(notify, neverStopRendering);
  }
}
