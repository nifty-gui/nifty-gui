package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * An EffectProcessorImpl handles a single effect type. You can have multiple
 * effects of the same type and all these effects are handled by a single
 * EffectProcessorImpl instance.
 *
 * @author void
 */
public class EffectProcessorImpl implements EffectProcessor {
  @Nonnull
  private static final Logger log = Logger.getLogger(EffectProcessorImpl.class.getName());
  @Nonnull
  private final Notify notify;
  @Nonnull
  private final List<Effect> allEffects = new ArrayList<Effect>();
  @Nonnull
  private final ActiveEffects activeEffects = new ActiveEffects();
  @Nonnull
  private final List<Effect> activeEffectsToRemove = new ArrayList<Effect>();
  @Nonnull
  private final List<Effect> pushedEffects = new ArrayList<Effect>();

  private boolean active = false;
  @Nullable
  private EndNotify listener;

  private final boolean neverStopRendering;
  private boolean processingEffects;
  private boolean pendingEffectsRemove;

  public EffectProcessorImpl(@Nonnull final Notify notify, final boolean neverStopRenderingParam) {
    this.notify = notify;
    this.neverStopRendering = neverStopRenderingParam;
  }

  @Override
  public void registerEffect(@Nonnull final Effect e) {
    allEffects.add(e);
  }

  @Override
  public void renderPre(@Nonnull final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActivePre());
  }

  @Override
  public void renderPost(@Nonnull final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActivePost());
  }

  @Override
  public void renderOverlay(@Nonnull final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActiveOverlay());
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void saveActiveNeverStopRenderingEffects() {
    pushedEffects.clear();

    for (int i = 0; i < activeEffects.getActive().size(); i++) {
      Effect e = activeEffects.getActive().get(i);
      if (e.isNeverStopRendering()) {
        pushedEffects.add(e);
      }
    }

    reset();
  }

  @Override
  public void restoreNeverStopRenderingEffects() {
    for (int i = 0; i < pushedEffects.size(); i++) {
      Effect e = pushedEffects.get(i);
      activate(listener, e.getAlternate(), e.getCustomKey());
    }
    pushedEffects.clear();
  }

  @Override
  public void reset() {
    internalSetActive(false);
    for (int i = 0; i < activeEffects.getActive().size(); i++) {
      Effect e = activeEffects.getActive().get(i);
      e.deactivate();
    }
    if (!processingEffects) {
      activeEffects.clear();
    } else {
      pendingEffectsRemove = true;
    }
  }

  @Override
  public void reset(@Nonnull final String customKey) {
    activeEffectsToRemove.clear();
    for (int i = 0; i < activeEffects.getActive().size(); i++) {
      Effect e = activeEffects.getActive().get(i);
      if (e.customKeyMatches(customKey)) {
        e.deactivate();
        activeEffectsToRemove.add(e);
      }
    }

    if (activeEffectsToRemove.size() == activeEffects.size()) {
      if (!processingEffects) {
        activeEffects.clear();
      } else {
        pendingEffectsRemove = true;
      }
    } else {
      for (Effect e : activeEffectsToRemove) {
        activeEffects.remove(e);
      }
    }
  }

  @Override
  public void activate(
      @Nullable final EndNotify newListener,
      @Nullable final String alternate,
      @Nullable final String customKey) {
    listener = newListener;

    // activate effects
    for (int i = 0; i < allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      startEffect(e, alternate, customKey);
    }

    if (!activeEffects.isEmpty()) {
      internalSetActive(true);
      pendingEffectsRemove = false;
    }
  }

  @Nonnull
  @Override
  public String getStateString() {
    if (activeEffects.isEmpty()) {
      return "no active effects";
    } else {
      StringBuilder data = new StringBuilder();

      List<Effect> effects = activeEffects.getActive();
      for (int i = 0; i < effects.size(); i++) {
        Effect e = effects.get(i);
        if (data.length() != 0) {
          data.append(", ");
        }
        data.append(e.getStateString());
      }
      return data.toString();
    }
  }

  @Override
  public void setActive(final boolean newActive) {
    internalSetActive(newActive);
    if (!active) {
      reset();
    }
  }

  @Override
  public void processHover(final int x, final int y) {
    for (int i = 0; i < allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      if (e.isHoverEffect()) {
        if (!e.isActive()) {
          if (e.isInsideFalloff(x, y)) {
            startEffect(e, null, null);
            setActive(true);
          }
        }
        if (e.isActive()) {
          if (!e.isInsideFalloff(x, y)) {
            e.deactivate();
            activeEffects.remove(e);
          } else {
            e.hoverDistance(x, y);
          }
        }
      }
    }
  }

  @Override
  public void processStartHover(final int x, final int y) {
    for (int i = 0; i < allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      if (e.isHoverEffect()) {
        if (!e.isActive()) {
          if (e.isInsideFalloff(x, y) && !e.getCustomFlag()) {
            startEffect(e, null, null);
            setActive(true);
            e.setCustomFlag(true);
          }
        }
        if (!e.isInsideFalloff(x, y) && e.getCustomFlag()) {
          e.setCustomFlag(false);
          if (e.isActive()) {
            e.deactivate();
            activeEffects.remove(e);
          }
        }
      }
    }
  }

  @Override
  public void processEndHover(final int x, final int y) {
    for (int i = 0; i < allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      if (e.isHoverEffect()) {
        if (!e.isActive()) {
          if (e.isInsideFalloff(x, y)) {
            e.setCustomFlag(true);
          }
          if (!e.isInsideFalloff(x, y) && e.getCustomFlag()) {
            startEffect(e, null, null);
            setActive(true);
            e.setCustomFlag(false);
          }
        }
        if (e.isActive()) {
          if (e.isInsideFalloff(x, y) && !e.getCustomFlag()) {
            e.setCustomFlag(true);
            e.deactivate();
            activeEffects.remove(e);
          }
        }
      }
    }
  }

  @Override
  public void processHoverDeactivate(final int x, final int y) {
    for (int i = 0; i < allEffects.size(); i++) {
      Effect e = allEffects.get(i);
      if (e.isHoverEffect()) {
        if (e.isActive()) {
          if (!e.isInsideFalloff(x, y)) {
            e.deactivate();
            activeEffects.remove(e);
          }
        }
      }
    }
  }

  @Override
  public void removeAllEffects() {
    allEffects.clear();
    activeEffects.clear();
  }

  /**
   * Return a List of all Effects that use the given EffectImpl.
   *
   * @param <T>
   * @param requestedClass
   * @return
   */
  @Nonnull
  @Override
  public <T extends EffectImpl> List<Effect> getEffects(@Nonnull final Class<T> requestedClass) {
    List<Effect> result = new ArrayList<Effect>();
    for (int i = 0; i < allEffects.size(); i++) {
      Effect effect = allEffects.get(i);
      T effectImpl = effect.getEffectImpl(requestedClass);
      if (effectImpl != null) {
        result.add(effect);
      }
    }
    return result;
  }

  public interface Notify {
    void effectProcessorStateChanged(boolean active);
  }

  private void renderActive(@Nonnull final NiftyRenderEngine renderDevice, @Nonnull final List<Effect> effects) {
    if (isInactive()) {
      return;
    }

    processingEffects = true;
    for (int i = 0; i < effects.size(); i++) {
      Effect e = effects.get(i);
      if (isActive(e)) {
        e.update();
        if (isActive(e)) {
          e.execute(renderDevice);
        }
      }
    }

    checkFinish();
    checkPendingEffectsRemove();
  }

  private void startEffect(
      @Nonnull final Effect e,
      @Nullable final String alternate,
      @Nullable final String customKey) {
    if (!e.start(alternate, customKey)) {
      return;
    }

    if (!activeEffects.contains(e)) {
      log.fine("adding effect as active");
      activeEffects.add(e);
    } else {
      log.fine("NOT adding effect as active because it's already registered as active");
    }
  }

  private void checkFinish() {
    if (active) {
      if (!activeEffects.containsActiveEffects()) {
        // done!
        internalSetActive(false);

        if (listener != null) {
          listener.perform();
        }
      }
    }
  }

  private void checkPendingEffectsRemove() {
    if (processingEffects) {
      if (pendingEffectsRemove) {
        activeEffects.clear();
        pendingEffectsRemove = false;
      }
      processingEffects = false;
    }
  }

  private boolean isInactive() {
    return !active && isNotNeverStopRendering();
  }

  private boolean isNotNeverStopRendering() {
    List<Effect> effects = activeEffects.getActive();
    for (int i = 0; i < effects.size(); i++) {
      Effect e = effects.get(i);
      if (e.isNeverStopRendering()) {
        return false;
      }
    }
    if (neverStopRendering) {
      return false;
    } else {
      return true;
    }
  }

  private boolean isActive(@Nonnull final Effect e) {
    return e.isActive() || e.isNeverStopRendering() || neverStopRendering;
  }

  private void internalSetActive(final boolean newActive) {
    boolean oldActive = active;
    this.active = newActive;
    if (newActive != oldActive) {
      notify.effectProcessorStateChanged(newActive);
    }
  }
}
