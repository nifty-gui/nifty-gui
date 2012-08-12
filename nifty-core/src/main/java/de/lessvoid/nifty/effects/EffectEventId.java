package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.effects.EffectProcessorImpl.Notify;



/**
 * possible effect event id's that you can attach effects too.
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

  EffectProcessor createEffectProcessor(final Notify notify) {
    return new EffectProcessorImpl(notify, neverStopRendering);
  }
}
