package de.lessvoid.nifty.effects;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Falloff.HoverFalloffConstraint;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

/**
 * An effect can be active or not and is always attached to one element. It has a TimeInterpolator that manages the life
 * time of the effect. The actual effect implementation is done be EffectImpl implementations.
 *
 * @author void
 */
public class Effect {
  private static Logger log = Logger.getLogger(Effect.class.getName());
  private EffectEventId effectEventId;
  private boolean active;
  private Element element;
  private TimeInterpolator timeInterpolator;
  private EffectImpl effectImpl;
  private EffectProperties parameter;
  private Object[] controllerArray;
  private boolean post;
  private boolean overlay;
  private String alternateEnable;
  private String alternateDisable;
  private String alternate;
  private String customKey;
  private boolean inherit;
  private Nifty nifty;
  private boolean hoverEffect;
  private boolean infiniteEffect;
  private Falloff falloff;
  private EffectEvents effectEvents;
  private boolean neverStopRendering;
  private boolean customFlag;

  public Effect(
      final Nifty niftyParam,
      final boolean inheritParam,
      final boolean postParam,
      final boolean overlayParam,
      final String alternateEnableParam,
      final String alternateDisableParam,
      final String customKeyParam,
      final boolean neverStopRenderingParam,
      final EffectEventId effectEventIdParam) {
    nifty = niftyParam;
    inherit = inheritParam;
    post = postParam;
    overlay = overlayParam;
    active = false;
    alternateEnable = alternateEnableParam;
    alternateDisable = alternateDisableParam;
    alternate = null;
    customKey = customKeyParam;
    effectEventId = effectEventIdParam;
    hoverEffect = false;
    infiniteEffect = false;
    neverStopRendering = neverStopRenderingParam;
    effectEvents = new EffectEvents();
    customFlag = false;
  }

  public void enableHover(final Falloff falloffParameter) {
    hoverEffect = true;
    falloff = falloffParameter;
  }

  public void disableHover() {
    hoverEffect = false;
    falloff = null;
  }

  public void disableInfinite() {
    if (infiniteEffect) {
      infiniteEffect = false;

      if (timeInterpolator != null) {
        timeInterpolator.initialize(parameter, infiniteEffect);
      }
    }
  }

  public void enableInfinite() {
    if (!infiniteEffect) {
      infiniteEffect = true;

      if (timeInterpolator != null) {
        timeInterpolator.initialize(parameter, infiniteEffect);
      }
    }
  }

  public void init(
      final Element elementParam,
      final EffectImpl effectImplParam,
      final EffectProperties parameterParam,
      final TimeProvider timeParam,
      final Collection<Object> controllers) {
    element = elementParam;
    effectImpl = effectImplParam;
    parameter = parameterParam;
    parameter.put("effectEventId", effectEventId);
    timeInterpolator = new TimeInterpolator(parameter, timeParam, infiniteEffect);
    controllerArray = controllers.toArray();
    effectEvents.init(nifty, controllerArray, parameter);
    customFlag = false;

    if (hoverEffect) {
      element.setVisibleToMouseEvents(true);
    }
  }

  public void updateParameters() {
    timeInterpolator.initialize(parameter, infiniteEffect);
    effectEvents.init(nifty, controllerArray, parameter);
  }

  public boolean start(final String alternate, final String customKey) {
    this.alternate = alternate;
    if (!canStartEffect(alternate, customKey)) {
      return false;
    }

    log.fine("starting effect [" + getStateString() + "] with customKey [" + customKey + "]");
    internalStart();
    return true;
  }

  private void internalStart() {
    active = true;
    timeInterpolator.start();
    effectEvents.onStartEffect(parameter);
    effectImpl.activate(nifty, element, parameter);
  }

  public void update() {
    setActiveInternal(timeInterpolator.update(), !neverStopRendering);
  }

  public void execute(final NiftyRenderEngine r) {
    if (isHoverEffect()) {
      effectImpl.execute(element, timeInterpolator.getValue(), falloff, r);
    } else {
      effectImpl.execute(element, timeInterpolator.getValue(), null, r);
    }
  }

  public boolean isActive() {
    return active;
  }

  public void deactivate() {
    setActiveInternal(false, true);
  }

  private void setActiveInternal(final boolean newActive, final boolean callDeactivate) {
    boolean ended = false;
    if (active && !newActive) {
      if (callDeactivate) {
        effectImpl.deactivate();
      }
      ended = true;
    }
    active = newActive;

    if (ended) {
      effectEvents.onEndEffect();
    }
  }

  public boolean isPost() {
    return post;
  }

  public boolean isAlternateDisable() {
    return alternateDisable != null;
  }

  public boolean customKeyMatches(final String customKeyToCheck) {
    if (customKeyToCheck == null) {
      return customKey == null;
    }
    return customKeyToCheck.equals(customKey);
  }

  public String getStateString() {
    return "(" + effectImpl.getClass().getSimpleName() + "[" + customKey + "]" + ")";
  }

  public <T extends EffectImpl> T getEffectImpl(final Class<T> requestedClass) {
    if (requestedClass.isInstance(effectImpl)) {
      return requestedClass.cast(effectImpl);
    }
    return null;
  }

  public boolean isInherit() {
    return inherit;
  }

  public boolean isHoverEffect() {
    return hoverEffect;
  }

  public void hoverDistance(final int x, final int y) {
    if (falloff != null) {
      falloff.updateFalloffValue(element, x, y);
    }
  }

  public boolean isInsideFalloff(final int x, final int y) {
    if (falloff != null && falloff.getFalloffConstraint() != HoverFalloffConstraint.none) {
      return falloff.isInside(element, x, y);
    } else {
      return element.isMouseInsideElement(x, y);
    }
  }

  public boolean isOverlay() {
    return overlay;
  }

  public boolean isNeverStopRendering() {
    return neverStopRendering;
  }

  public EffectProperties getParameters() {
    return parameter;
  }

  public String getAlternate() {
    return alternate;
  }

  public String getCustomKey() {
    return customKey;
  }

  private boolean canStartEffect(final String alternate, final String customKey) {
    if (alternate == null) {
      // no alternate key given

      // don't start this effect when it has an alternateKey set.
      if (isAlternateEnable()) {
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [" + alternate + "] and effect " +
                "isAlternateEnable()");
        return false;
      }
    } else {
      // we have an alternate key
      if (isAlternateDisable() && alternateDisableMatches(alternate)) {
        // don't start this effect. it has an alternateKey set and should be used for disable matches only
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [" + alternate + "] matches " +
                "alternateDisableMatches()");
        return false;
      }

      if (isAlternateEnable() && !alternateEnableMatches(alternate)) {
        // start with alternateEnable but names don't match ... don't start
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [" + alternate + "] does not " +
                "match alternateEnableMatches()");
        return false;
      }
    }

    if (!customKeyMatches(customKey)) {
      log.fine(
          "starting effect [" + getStateString() + "] canceled because customKey [" + customKey + "] does not match " +
              "key set at the effect");
      return false;
    }

    return true;
  }

  private boolean isAlternateEnable() {
    return alternateEnable != null;
  }

  private boolean alternateEnableMatches(final String alternate) {
    return (alternate != null) && alternate.equals(alternateEnable);
  }

  private boolean alternateDisableMatches(final String alternate) {
    return (alternate != null) && alternate.equals(alternateDisable);
  }

  public boolean getCustomFlag() {
    return customFlag;
  }

  public void setCustomFlag(final boolean customFlag) {
    this.customFlag = customFlag;
  }
}
