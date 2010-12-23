package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;

/**
 * An EffectProcessor handles a single effect type. You can have multiple
 * effects of the same type and all these effects are handled by a single
 * EffectProcessor instance.
 *
 * @author void
 */
public class EffectProcessor {
  private Logger log = Logger.getLogger(EffectProcessor.class.getName());
  private List < Effect > allEffects = new ArrayList < Effect >();
  private List < Effect > activeEffects = new ArrayList < Effect >();
  private List <Effect> activeEffectsToRemove = new ArrayList < Effect >();

  private boolean active;
  private EndNotify listener;
  private NiftyRenderDeviceProxy renderDeviceProxy;

  private boolean neverStopRendering;
  private boolean processingActiveEffects;
  private boolean clearActiveEffectsRequested;

  public EffectProcessor(final boolean neverStopRenderingParam) {
    neverStopRendering = neverStopRenderingParam;
    active = false;
    renderDeviceProxy = new NiftyRenderDeviceProxy();
  }

  public void registerEffect(final Effect e) {
    allEffects.add(e);
  }

  public Set <RenderStateType> getRenderStatesToSave() {
    if (!active) {
      if (isNotNeverStopRendering()) {
        return new HashSet < RenderStateType >();
      }
    }

    renderDeviceProxy.reset();

    processingActiveEffects = true;
    for (Effect e : activeEffects) {
      if (e.isInherit() && (e.isActive() || e.isNeverStopRendering() || neverStopRendering)) {
        e.execute(renderDeviceProxy);
      }
    }
    resetProcessingActiveEffects();
    return renderDeviceProxy.getStates();
  }

  public void renderPre(final NiftyRenderEngine renderDevice) {
    if (!active) {
      if (isNotNeverStopRendering()) {
        return;
      }
    }

    processingActiveEffects = true;
    for (Effect e : activeEffects) {
      if (!e.isOverlay() && !e.isPost() && (e.isActive() || e.isNeverStopRendering() || neverStopRendering)) {
        e.update();
        if (!e.isOverlay() && !e.isPost() && (e.isActive() || e.isNeverStopRendering() || neverStopRendering)) {
          e.execute(renderDevice);
        }
      }
    }

    checkFinish();
  }

  public void renderPost(final NiftyRenderEngine renderDevice) {
    if (isNotNeverStopRendering()) {
      if (!active) {
        return;
      }
    }

    processingActiveEffects = true;
    for (Effect e : activeEffects) {
      if (!e.isOverlay() && e.isPost() && (e.isActive() || e.isNeverStopRendering() || neverStopRendering)) {
        e.update();
        if (!e.isOverlay() && e.isPost() && (e.isActive() || e.isNeverStopRendering() || neverStopRendering)) {
          e.execute(renderDevice);
        }
      }
    }

    checkFinish();
  }

  public void renderOverlay(final NiftyRenderEngine renderDevice) {
    if (!active) {
      if (isNotNeverStopRendering()) {
        return;
      }
    }

    processingActiveEffects = true;
    for (Effect e : activeEffects) {
      if (e.isOverlay() && (e.isActive() || e.isNeverStopRendering()|| neverStopRendering)) {
        e.update();
        if (e.isOverlay() && (e.isActive() || e.isNeverStopRendering()|| neverStopRendering)) {
          e.execute(renderDevice);
        }
      }
    }

    checkFinish();
  }

  public boolean isActive() {
    return active;
  }

  public void reset() {
    active = false;
    for (Effect e : activeEffects) {
      e.setActive(false);
    }
    if (!processingActiveEffects) {
      activeEffects.clear();
    } else {
      clearActiveEffectsRequested = true;
    }
  }

  public void reset(final String customKey) {
     activeEffectsToRemove.clear();

     for (Effect e : activeEffects) {
       if (e.customKeyMatches(customKey)) {
         e.setActive(false);
         activeEffectsToRemove.add(e);
       }
     }

     if (activeEffectsToRemove.size() == activeEffects.size()) {
       if (!processingActiveEffects) {
         activeEffects.clear();
       } else {
         clearActiveEffectsRequested = true;
       }
     } else {
       for (Effect e : activeEffectsToRemove) {
         activeEffects.remove(e);
       }
     }
  }

  public void activate(final EndNotify newListener, final String alternate) {
    listener = newListener;

    // activate effects
    for (Effect e : allEffects) {
      startEffect(e, alternate, null);
    }

    if (!activeEffects.isEmpty()) {
      active = true;
      clearActiveEffectsRequested = false;
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
      clearActiveEffectsRequested = false;
    }
  }

  public String getStateString() {
    if (activeEffects.isEmpty()) {
      return "no active effects";
    } else {
      StringBuffer data = new StringBuffer();
      for (Effect e : activeEffects) {
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
    if (alternate == null) {
      // no alternate key given

      // don't start this effect when it has an alternateKey set.
      if (e.isAlternateEnable()) {
        log.info("starting effect [" + e.getStateString() + "] canceled because alternateKey [" + alternate + "] and effect isAlternateEnable()");
        return;
      }
    } else {
      // we have an alternate key
      if (e.isAlternateDisable() && e.alternateDisableMatches(alternate)) {
        // don't start this effect. it has an alternateKey set and should be used for disable matches only
        log.info("starting effect [" + e.getStateString() + "] canceled because alternateKey [" + alternate + "] matches alternateDisableMatches()");
        return;
      }

      if (e.isAlternateEnable() && !e.alternateEnableMatches(alternate)) {
        // start with alternateEnable but names don't match ... don't start
        log.info("starting effect [" + e.getStateString() + "] canceled because alternateKey [" + alternate + "] does not match alternateEnableMatches()");
        return;
      }
    }

    if (customKey != null) {
      if (!e.customKeyMatches(customKey)) {
        log.info("starting effect [" + e.getStateString() + "] canceled because customKey [" + customKey + "] does not match key set at the effect");
        return;
      }
    }

    log.info("starting effect [" + e.getStateString() + "] with customKey [" + customKey + "]");
    e.start();
    if (!activeEffects.contains(e)) {
      log.info("adding effect as active");
      activeEffects.add(e);
    } else {
      log.info("NOT adding effect as active");
    }
  }

  private void processHoverInternal(final int x, final int y, final List < Effect > effectList) {
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

  private boolean isNotNeverStopRendering() {
    for (Effect e : activeEffects) {
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

  private void checkFinish() {
    if (active) {
      if (!isActive(activeEffects)) {
        // done!
        active = false;

        if (listener != null) {
          listener.perform();
        }
      }
    }
    resetProcessingActiveEffects();
  }

  private void resetProcessingActiveEffects() {
    if (processingActiveEffects) {
      if (clearActiveEffectsRequested) {
        activeEffects.clear();
        clearActiveEffectsRequested = false;
      }
      processingActiveEffects = false;
    }
  }

  private boolean isActive(final List < Effect >  effectList) {
    for (Effect e : effectList) {
      if (e.isActive()) {
        return true;
      }
    }
    return false;
  }
}
