package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.newdawn.slick.util.Log;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
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

  private boolean active;
  private EndNotify listener;
  private NiftyRenderDeviceProxy renderDeviceProxy;

  private boolean neverStopRendering;
  private Falloff hoverFalloff;
  private boolean hoverEffect;
  private boolean processingActiveEffects;
  private boolean clearActiveEffectsRequested;

  /**
   * create and initialize a new instance.
   * @param neverStopRenderingParam flag if this event never ends
   */
  public EffectProcessor(final boolean neverStopRenderingParam) {
    neverStopRendering = neverStopRenderingParam;
    active = false;
    renderDeviceProxy = new NiftyRenderDeviceProxy();
    hoverEffect = false;
  }

  /**
   * register pre effect.
   * @param e the effect to register.
   */
  public final void registerEffect(final Effect e) {
    allEffects.add(e);
  }

  /**
   * render pre.
   * @param renderDevice the renderDevice
   */
  public final void renderPre(final NiftyRenderEngine renderDevice) {
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

  /**
   * render post.
   * @param renderDevice the RenderDevice
   */
  public final void renderPost(final NiftyRenderEngine renderDevice) {
    if (isNotNeverStopRendering()) {
      if (!active) {
        return;
      }
    }

    processingActiveEffects = true;
    for (Effect e : activeEffects) {
      if (!e.isOverlay() && e.isPost() && (e.isActive() || e.isNeverStopRendering()|| neverStopRendering)) {
        e.update();
        if (!e.isOverlay() && e.isPost() && (e.isActive() || e.isNeverStopRendering()|| neverStopRendering)) {
          e.execute(renderDevice);
        }
      }
    }

    checkFinish();
  }

  /**
   * Should return a set of all RenderStates that should be saved before doing
   * any rendering.
   * @return set of RenderState to save
   */
  public Set < RenderStateType > getRenderStatesToSave() {
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

  /**
   * checks if all effects are done and changes the active flag accordingly.
   */
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

  /**
   * checks if the given list if effects contains at least one active effect.
   * @param effectList the effectList to check for active entries.
   * @return true, if at least one effect ist still active and false otherwise.
   */
  private boolean isActive(final List < Effect >  effectList) {
    for (Effect e : effectList) {
      if (e.isActive()) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks if the effect is active.
   * @return true, when active and false otherwise
   */
  public final boolean isActive() {
    return active;
  }

  /**
   * reset.
   */
  public final void reset() {
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

  /**
   * activate an effect for an element.
   * @param newListener the EndNotify
   * @param alternate alternate key to use
   */
  public final void activate(final EndNotify newListener, final String alternate) {
    listener = newListener;

    // activate effects
    for (Effect e : allEffects) {
      startEffect(e, alternate);
    }

    if (!activeEffects.isEmpty()) {
      active = true;
      clearActiveEffectsRequested = false;
    }
  }

  /**
   * Helper method to start an effect.
   * @param e Effect to start
   * @param alternate alternate key to use
   */
  protected void startEffect(final Effect e, final String alternate) {
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

    log.info("starting effect [" + e.getStateString() + "]");
    e.start();
    if (!activeEffects.contains(e)) {
      activeEffects.add(e);
    }
  }

  /**
   * get effects list.
   * @return effect list
   */
  protected List < Effect > getEffects() {
    return allEffects;
  }

  /**
   * set activate.
   * @param newActive new state
   */
  protected void setActive(final boolean newActive) {
    active = newActive;
    if (!active) {
      reset();
    }
  }

  /**
   * get state string.
   * @return String with state information
   */
  public String getStateString() {
    if (activeEffects.isEmpty()) {
      return "";
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

  /**
   * get list of active effects.
   * @return active effect list
   */
  public List < Effect > getActiveEffects() {
    return activeEffects;
  }

  /**
   * process hover effect.
   * @param x mouse x position
   * @param y mouse y position
   */
  public void processHover(final int x, final int y) {
    processHoverInternal(x, y, getEffects());
  }

  /**
   * process hover effects.
   * @param x mouse x position
   * @param y mouse y position
   * @param effectList the effect list to check
   */
  private void processHoverInternal(
      final int x,
      final int y,
      final List < Effect > effectList) {
    for (Effect e : effectList) {
      if (e.isHoverEffect()) {
        if (!e.isActive()) {
          if (e.isInsideFalloff(x, y)) {
            startEffect(e, null);
            setActive(true);
          }
        }
        if (e.isActive()) {
          if (!e.isInsideFalloff(x, y)) {
            e.setActive(false);
            getActiveEffects().remove(e);
          } else {
            e.hoverDistance(x, y);
          }
        }
      }
    }
  }

  public boolean isHoverEffect() {
    return hoverEffect;
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

  public void setHoverEffect() {
    hoverEffect = true;
  }

  public void setFalloff(final Falloff falloffParam) {
    hoverFalloff = falloffParam;
  }

  public Falloff getFalloff() {
    return hoverFalloff;
  }
}
