package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Falloff.HoverFalloffConstraint;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * An effect can be active or not and is always attached to one element. It has a TimeInterpolator that manages the life
 * time of the effect. The actual effect implementation is done be EffectImpl implementations.
 *
 * @author void
 */
public class Effect {
  @Nonnull
  private static final Logger log = Logger.getLogger(Effect.class.getName());
  @Nonnull
  private final EffectEventId effectEventId;
  private boolean active;
  @Nonnull
  private final Element element;
  @Nonnull
  private final TimeInterpolator timeInterpolator;
  @Nonnull
  private final EffectImpl effectImpl;
  @Nonnull
  private final EffectProperties parameter;
  @Nonnull
  private final Object[] controllerArray;
  private final boolean post;
  private final boolean overlay;
  @Nullable
  private final String alternateEnable;
  @Nullable
  private final String alternateDisable;
  @Nullable
  private String alternate;
  @Nullable
  private final String customKey;
  private final boolean inherit;
  @Nonnull
  private final Nifty nifty;
  private boolean hoverEffect;
  private boolean infiniteEffect;
  @Nullable
  private Falloff falloff;
  @Nonnull
  private final EffectEvents effectEvents;
  private final boolean neverStopRendering;
  private boolean customFlag;

  public Effect(
      @Nonnull final Nifty nifty,
      final boolean inherit,
      final boolean post,
      final boolean overlay,
      @Nullable final String alternateEnable,
      @Nullable final String alternateDisable,
      @Nullable final String customKey,
      final boolean neverStopRendering,
      @Nonnull final EffectEventId effectEventId,
      @Nonnull final Element element,
      @Nonnull final EffectImpl effectImpl,
      @Nonnull final EffectProperties parameter,
      @Nonnull final TimeProvider timeProvider,
      @Nonnull final Collection<Object> controllers) {
    this.nifty = nifty;
    this.inherit = inherit;
    this.post = post;
    this.overlay = overlay;
    active = false;
    this.alternateEnable = alternateEnable;
    this.alternateDisable = alternateDisable;
    alternate = null;
    this.customKey = customKey;
    this.effectEventId = effectEventId;
    hoverEffect = false;
    infiniteEffect = false;
    this.neverStopRendering = neverStopRendering;
    effectEvents = new EffectEvents();
    customFlag = false;

    this.element = element;
    this.effectImpl = effectImpl;
    this.parameter = parameter;
    parameter.put("effectEventId", effectEventId);
    timeInterpolator = new TimeInterpolator(parameter, timeProvider, infiniteEffect);
    controllerArray = controllers.toArray();
    effectEvents.init(nifty, controllerArray, parameter);
    customFlag = false;
  }

  public void enableHover(final Falloff falloffParameter) {
    hoverEffect = true;
    falloff = falloffParameter;
    setVisibleToMouse(true);
  }

  public void disableHover() {
    hoverEffect = false;
    falloff = null;
  }

  public void disableInfinite() {
    setInfiniteEffect(false);
  }

  public void enableInfinite() {
    setInfiniteEffect(true);
  }

  public void setInfiniteEffect(final boolean infinite) {
    if (infinite != infiniteEffect) {
      infiniteEffect = infinite;
      timeInterpolator.initialize(parameter, infinite);
    }
  }

  public void setVisibleToMouse(final boolean visibleToMouse) {
    element.setVisibleToMouseEvents(visibleToMouse);
  }

  public void updateParameters() {
    timeInterpolator.initialize(parameter, infiniteEffect);
    effectEvents.init(nifty, controllerArray, parameter);
  }

  public boolean start(@Nullable final String alternate, @Nullable final String customKey) {
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

  public void execute(@Nonnull final NiftyRenderEngine r) {
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

  public boolean customKeyMatches(@Nullable final String customKeyToCheck) {
    if (customKeyToCheck == null) {
      return customKey == null;
    }
    return customKeyToCheck.equals(customKey);
  }

  @Nonnull
  public String getStateString() {
    return "(" + effectImpl.getClass().getSimpleName() + "[" + customKey + "]" + ")";
  }

  @Nullable
  public <T extends EffectImpl> T getEffectImpl(@Nonnull final Class<T> requestedClass) {
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

  @Nonnull
  public EffectProperties getParameters() {
    return parameter;
  }

  @Nullable
  public String getAlternate() {
    return alternate;
  }

  @Nullable
  public String getCustomKey() {
    return customKey;
  }

  private boolean canStartEffect(@Nullable final String alternate, @Nullable final String customKey) {
    if (alternate == null) {
      // no alternate key given

      // don't start this effect when it has an alternateKey set.
      if (isAlternateEnable()) {
        log.fine(
            "starting effect [" + getStateString() + "] canceled because alternateKey [NULL] and effect " +
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

  private boolean alternateEnableMatches(@Nullable final String alternate) {
    return (alternate != null) && alternate.equals(alternateEnable);
  }

  private boolean alternateDisableMatches(@Nullable final String alternate) {
    return (alternate != null) && alternate.equals(alternateDisable);
  }

  public boolean getCustomFlag() {
    return customFlag;
  }

  public void setCustomFlag(final boolean customFlag) {
    this.customFlag = customFlag;
  }

  @Nonnull
  public EffectEventId getEffectEventId() {
    return effectEventId;
  }
}
