package de.lessvoid.nifty.effects;

import java.util.Hashtable;
import java.util.Map;

public class ElementEffectStateCache {
  private Map<EffectEventId, Boolean> states = new Hashtable<EffectEventId, Boolean>();

  public ElementEffectStateCache() {
    states.put(EffectEventId.onStartScreen, false);
    states.put(EffectEventId.onEndScreen, false);
    states.put(EffectEventId.onFocus, false);
    states.put(EffectEventId.onGetFocus, false);
    states.put(EffectEventId.onLostFocus, false);
    states.put(EffectEventId.onClick, false);
    states.put(EffectEventId.onHover, false);
    states.put(EffectEventId.onStartHover, false);
    states.put(EffectEventId.onEndHover, false);
    states.put(EffectEventId.onActive, false);
    states.put(EffectEventId.onCustom, false);
    states.put(EffectEventId.onShow, false);
    states.put(EffectEventId.onHide, false);
    states.put(EffectEventId.onEnabled, false);
    states.put(EffectEventId.onDisabled, false);
  }

  public boolean get(final EffectEventId effectEventId) {
    return states.get(effectEventId);
  }

  public void set(final EffectEventId eventId, final boolean effectActive) {
    states.put(eventId, effectActive);
  }
}
