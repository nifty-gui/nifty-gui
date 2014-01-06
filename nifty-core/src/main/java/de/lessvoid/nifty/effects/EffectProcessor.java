package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface EffectProcessor {
  void registerEffect(@Nonnull Effect e);

  void renderPre(@Nonnull NiftyRenderEngine renderDevice);

  void renderPost(@Nonnull NiftyRenderEngine renderDevice);

  void renderOverlay(@Nonnull NiftyRenderEngine renderDevice);

  boolean isActive();

  void saveActiveNeverStopRenderingEffects();

  void restoreNeverStopRenderingEffects();

  void reset();

  void reset(@Nonnull String customKey);

  void activate(@Nullable EndNotify newListener, @Nullable String alternate, @Nullable String customKey);

  @Nullable
  String getStateString();

  void setActive(boolean newActive);

  void processHover(int x, int y);

  void processStartHover(int x, int y);

  void processEndHover(int x, int y);

  void processHoverDeactivate(int x, int y);

  void removeAllEffects();

  @Nonnull
  <T extends EffectImpl> List<Effect> getEffects(@Nonnull Class<T> requestedClass);
}
