package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * An EffectProcessor handles a single effect type. You can have multiple
 * effects of the same type and all these effects are handled by a single
 * EffectProcessor instance.
 *
 * @author void
 */
public class EffectProcessor {
  private Logger log = Logger.getLogger(EffectProcessor.class.getName());
  private List<Effect> allEffects = new ArrayList<Effect>();
  private ActiveEffects activeEffects = new ActiveEffects();
  private List<Effect> activeEffectsToRemove = new ArrayList<Effect>();

  private boolean active = false;
  private EndNotify listener;

  private boolean neverStopRendering;
  private boolean processingEffects;
  private boolean pendingEffectsRemove;

  public EffectProcessor(final boolean neverStopRenderingParam) {
    neverStopRendering = neverStopRenderingParam;
  }

  public void registerEffect(final Effect e) {
    allEffects.add(e);
  }

  public void getRenderStatesToSave(final NiftyRenderDeviceProxy renderDeviceProxy) {
    if (isInactive()) {
      return;
    }

    renderDeviceProxy.reset();

    processingEffects = true;
    for (int i=0; i<activeEffects.getActive().size(); i++) {
      Effect e = activeEffects.getActive().get(i);
      if (e.isInherit() && (isActive(e))) {
        e.execute(renderDeviceProxy);
      }
    }
    checkPendingEffectsRemove();
  }

  public void renderPre(final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActivePre());
  }

  public void renderPost(final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActivePost());
  }

  public void renderOverlay(final NiftyRenderEngine renderDevice) {
    renderActive(renderDevice, activeEffects.getActiveOverlay());
  }

  private void renderActive(final NiftyRenderEngine renderDevice, final List<Effect> effects) {
    if (isInactive()) {
      return;
    }

    processingEffects = true;
    for (int i=0; i<effects.size(); i++) {
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

  public boolean isActive() {
    return active;
  }

  public void reset() {
    active = false;
    for (int i=0; i<activeEffects.getActive().size(); i++) {
      Effect e = activeEffects.getActive().get(i);
      e.setActive(false);
    }
    if (!processingEffects) {
      activeEffects.clear();
    } else {
      pendingEffectsRemove = true;
    }
  }

  public void reset(final String customKey) {
     activeEffectsToRemove.clear();
     for (int i=0; i<activeEffects.getActive().size(); i++) {
       Effect e = activeEffects.getActive().get(i);
       if (e.customKeyMatches(customKey)) {
         e.setActive(false);
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

  public void activate(final EndNotify newListener, final String alternate, final String customKey) {
    listener = newListener;

    // activate effects
    for (Effect e : allEffects) {
      startEffect(e, alternate, customKey);
    }

    if (!activeEffects.isEmpty()) {
      active = true;
      pendingEffectsRemove = false;
    }
  }

  public String getStateString() {
    if (activeEffects.isEmpty()) {
      return "no active effects";
    } else {
      StringBuffer data = new StringBuffer();
      for (Effect e : activeEffects.getActive()) {
        if (data.length() != 0) {
          data.append(", ");
        }
        data.append(e.getStateString());
      }
      return data.toString();
    }
  }

  public void setActive(final boolean newActive) {
    active = newActive;
    if (!active) {
      reset();
    }
  }

  public void processHover(final int x, final int y) {
    processHoverInternal(x, y, allEffects);
  }

  private void startEffect(final Effect e, final String alternate, final String customKey) {
    if (!e.start(alternate, customKey)) {
      return;
    }

    if (!activeEffects.contains(e)) {
      log.info("adding effect as active");
      activeEffects.add(e);
    } else {
      log.info("NOT adding effect as active because it's already registered as active");
    }
  }

  private void processHoverInternal(final int x, final int y, final List <Effect> effectList) {
    for (Effect e : effectList) {
      if (e.isHoverEffect()) {
        if (!e.isActive()) {
          if (e.isInsideFalloff(x, y)) {
            startEffect(e, null, null);
            setActive(true);
          }
        }
        if (e.isActive()) {
          if (!e.isInsideFalloff(x, y)) {
            e.setActive(false);
            activeEffects.remove(e);
          } else {
            e.hoverDistance(x, y);
          }
        }
      }
    }
  }

  public void removeAllEffects() {
    allEffects.clear();
    activeEffects.clear();
  }

  /**
   * Return a List of all Effects that use the given EffectImpl.
   * @param <T>
   * @param requestedClass
   * @return
   */
  public <T extends EffectImpl> List<Effect> getEffects(final Class<T> requestedClass) {
    List<Effect> result = new ArrayList<Effect>();
    for (int i=0; i<allEffects.size(); i++) {
      Effect effect = allEffects.get(i);
      T effectImpl = effect.getEffectImpl(requestedClass);
      if (effectImpl != null) {
        result.add(effect);
      }
    }
    return result;
  }

  private void checkFinish() {
    if (active) {
      if (!activeEffects.containsActiveEffects()) {
        // done!
        active = false;

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
    for (Effect e : activeEffects.getActive()) {
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

  private boolean isActive(final Effect e) {
    return e.isActive() || e.isNeverStopRendering() || neverStopRendering;
  }
}
