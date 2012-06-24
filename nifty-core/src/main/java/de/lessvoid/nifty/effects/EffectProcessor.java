package de.lessvoid.nifty.effects;

import java.util.List;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public interface EffectProcessor {
  void registerEffect(final Effect e);
  void getRenderStatesToSave(final NiftyRenderDeviceProxy renderDeviceProxy);
  void renderPre(final NiftyRenderEngine renderDevice);
  void renderPost(final NiftyRenderEngine renderDevice);
  void renderOverlay(final NiftyRenderEngine renderDevice);
  boolean isActive();
  void saveActiveNeverStopRenderingEffects();
  void restoreNeverStopRenderingEffects();
  void reset();
  void reset(final String customKey);
  void activate(final EndNotify newListener, final String alternate, final String customKey);
  String getStateString();
  void setActive(final boolean newActive);
  void processHover(final int x, final int y);
  void processStartHover(final int x, final int y);
  void processEndHover(final int x, final int y);
  void processHoverDeactivate(final int x, final int y);
  void removeAllEffects();
  <T extends EffectImpl> List<Effect> getEffects(final Class<T> requestedClass);
}
