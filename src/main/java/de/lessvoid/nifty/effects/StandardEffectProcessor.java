package de.lessvoid.nifty.effects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderState;

/**
 * An BaseEffectProcessor handles a single effect type. You can have multiple
 * effects of the same type and all these effects are handled by a single
 * EffectProcessor instance.
 *
 * @author void
 */
public class StandardEffectProcessor implements EffectProcessor {

  /**
   * the list of all pre effects registered in this processor.
   */
  private List < Effect > effects = new ArrayList < Effect >();

  /**
   * the list of active effects.
   */
  private List < Effect > activeEffects = new ArrayList < Effect >();

  /**
   * flag if this effect is active.
   */
  private boolean active;

  /**
   * the listener we should inform when something happened.
   */
  private EndNotify listener;

  /**
   * RenderDeviceProxy to trick stuff.
   */
  private RenderDeviceProxy renderDeviceProxy;

  /**
   * never stop rendering.
   */
  private boolean neverStopRendering;

  /**
   * create and initialize a new instance.
   * @param neverEndsValue flag if this event never ends
   */
  public StandardEffectProcessor(final boolean neverEndsValue) {
    this.neverStopRendering = neverEndsValue;
    this.active = false;
    this.renderDeviceProxy = new RenderDeviceProxy();
  }

  /**
   * register pre effect.
   * @param e the effect to register.
   */
  public final void registerEffect(final Effect e) {
    effects.add(e);
  }

  /**
   * render pre.
   * @param renderDevice the renderDevice
   */
  public final void renderPre(final RenderEngine renderDevice) {
    if (!neverStopRendering) {
      if (!active) {
        return;
      }
    }

    for (Effect e : activeEffects) {
      if (!e.isPost() && (e.isActive() || neverStopRendering)) {
        e.update();
        e.execute(renderDevice);
      }
    }

    checkFinish();
  }

  /**
   * render post.
   * @param renderDevice the RenderDevice
   */
  public final void renderPost(final RenderEngine renderDevice) {
    if (!neverStopRendering) {
      if (!active) {
        return;
      }
    }

    for (Effect e : activeEffects) {
      if (e.isPost() && (e.isActive() || neverStopRendering)) {
        e.update();
        e.execute(renderDevice);
      }
    }

    checkFinish();
  }

  /**
   * Should return a set of all RenderStates that should be saved before doing
   * any rendering.
   * @return set of RenderState to save
   */
  public Set < RenderState > getRenderStatesToSave() {
    if (!neverStopRendering) {
      if (!active) {
        return new HashSet < RenderState >();
      }
    }

    renderDeviceProxy.reset();

    for (Effect e : activeEffects) {
      if (e.isInherit() && (e.isActive() || neverStopRendering)) {
        e.execute(renderDeviceProxy);
      }
    }

    return renderDeviceProxy.getStates();
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
    this.active = false;
    for (Effect e : activeEffects) {
      e.setActive(false);
    }
    activeEffects.clear();
  }

  /**
   * activate an effect for an element.
   * @param newListener the EndNotify
   * @param alternate alternate key to use
   */
  public final void activate(final EndNotify newListener, final String alternate) {
    this.listener = newListener;

    // activate effects
    for (Effect e : effects) {
      startEffect(e, alternate);
    }

    if (!activeEffects.isEmpty()) {
      this.active = true;
    }
  }

  /**
   * Helper method to start an effect.
   * @param e Effect to start
   * @param alternate alternate key to use
   */
  protected void startEffect(final Effect e, final String alternate) {
    if (alternate == null) {
      if (e.alternateKey() != null && e.isAlternateEnable()) {
        // don't start this effect. it has an alternateKey set and should be used for enable only
        return;
      }
    } else {
      if (e.alternateKey() != null && !e.isAlternateEnable()) {
        // don't start this effect. it has an alternateKey set and should be used for disable only
        return;
      }

      if (e.isAlternateEnable() && !alternate.equals(e.alternateKey())) {
        // start with alternateEnable but names don't match ... don't start
        return;
      }
    }

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
    return effects;
  }

  /**
   * set activate.
   * @param newActivate new state
   */
  protected void setActive(final boolean newActivate) {
    this.active = newActivate;
  }

  /**
   * get state string.
   * @return String with state information
   */
  public String getStateString() {
    if (activeEffects.isEmpty()) {
      return "active: " + active;
    } else {
      StringBuffer data = new StringBuffer();
      for (Effect e : activeEffects) {
        if (data.length() != 0) {
          data.append(", ");
        }
        data.append(e.getStateString());
      }
      return "active: " + active + ", " + data.toString();
    }
  }

  /**
   * get list of active effects.
   * @return active effect list
   */
  public List < Effect > getActiveEffects() {
    return activeEffects;
  }
}
