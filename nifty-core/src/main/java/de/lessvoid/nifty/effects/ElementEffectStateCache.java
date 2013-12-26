package de.lessvoid.nifty.effects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class ElementEffectStateCache {
  @Nonnull
  private final Map<EffectEventId, Boolean> states;

  public ElementEffectStateCache() {
    states = new EnumMap<EffectEventId, Boolean>(EffectEventId.class);
  }

  public boolean get(@Nonnull final EffectEventId effectEventId) {
    @Nullable Boolean value = states.get(effectEventId);
    if (value == null) {
      return false;
    }
    return value;
  }

  public void set(@Nonnull final EffectEventId eventId, final boolean effectActive) {
    states.put(eventId, effectActive);
  }
}
