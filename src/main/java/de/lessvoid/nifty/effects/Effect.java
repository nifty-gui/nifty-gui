package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

/**
 * An effect can be active or not and is always attached to one element. It
 * has a TimeInterpolator that manages the life time of the effect. The actual
 * effect implementation is done be EffectImpl implementations.
 *
 * @author void
 */
public class Effect {

  private EffectEventId effectEventId;
  private boolean active;
  private Element element;
  private TimeInterpolator timeInterpolator;
  private EffectImpl effectImpl;
  private Properties parameter;
  private boolean post;
  private boolean overlay;
  private String alternateKey;
  private boolean alternateEnable;
  private boolean inherit;
  private Nifty nifty;
  private boolean hoverEffect;
  private boolean infiniteEffect;
  private Falloff falloff;

  public Effect(
      final Nifty niftyParam,
      final boolean inheritParam,
      final boolean postParam,
      final boolean overlayParam,
      final String alternateKeyParam,
      final boolean alternateEnableParam,
      final EffectEventId effectEventIdParam) {
    nifty = niftyParam;
    inherit = inheritParam;
    post = postParam;
    overlay = overlayParam;
    active = false;
    alternateKey = alternateKeyParam;
    alternateEnable = alternateEnableParam;
    effectEventId = effectEventIdParam;
    hoverEffect = false;
    infiniteEffect = false;
  }

  public void enableHover(final Falloff falloffParameter) {
    hoverEffect = true;
    falloff = falloffParameter;
  }

  public void enableInfinite() {
    infiniteEffect = true;
  }

  /**
   * initialize the effect.
   * @param elementParam Element
   * @param effectImplParam EffectImpl
   * @param parameterParam Properties
   * @param timeParam TimeProvider
   */
  public void init(
      final Element elementParam,
      final EffectImpl effectImplParam,
      final Properties parameterParam,
      final TimeProvider timeParam) {
    effectImpl = effectImplParam;
    element = elementParam;
    parameter = parameterParam;
    parameter.put("effectEventId", effectEventId);
    timeInterpolator = new TimeInterpolator(parameter, timeParam, infiniteEffect);
  }

  /**
   * start the effect.
   */
  public void start() {
    active = true;
    timeInterpolator.start();
    effectImpl.activate(nifty, element, parameter);
  }

  /**
   * update effect.
   */
  public void update() {
    active = timeInterpolator.update();
  }

  /**
   * execute the effect.
   * @param r RenderDevice
   */
  public void execute(final NiftyRenderEngine r) {
    if (isHoverEffect()) {
      effectImpl.execute(element, timeInterpolator.getValue(), falloff, r);
    } else {
      effectImpl.execute(element, timeInterpolator.getValue(), null, r);
    }
  }

  /**
   * is this effect still active?
   * @return active flag
   */
  public boolean isActive() {
    return active;
  }

  /**
   * change the effects active state.
   * @param newActive new active state
   */
  public void setActive(final boolean newActive) {
    if (this.active && !newActive) {
      effectImpl.deactivate();
    }
    this.active = newActive;
  }

  /**
   * post or pre mode.
   * @return post, true or pre, false
   */
  public boolean isPost() {
    return post;
  }

  /**
   * alternate key.
   * @return alternateKey
   */
  public String alternateKey() {
    return alternateKey;
  }

  /**
   * alternate enable or disable.
   * @return enable (true) or disable (false) mode
   */
  public boolean isAlternateEnable() {
    return alternateEnable;
  }

  /**
   * get state string.
   * @return state string
   */
  public String getStateString() {
    return
      "("
      +
      effectImpl.getClass().getSimpleName()
      +
      ")";
  }

  /**
   * should this effect inherit to children.
   * @return true, when yes and false, when not
   */
  public boolean isInherit() {
    return inherit;
  }

  public boolean isHoverEffect() {
    return hoverEffect;
  }

  /**
   * hover distance.
   * @param x x position
   * @param y y position
   */
  public final void hoverDistance(final int x, final int y) {
    if (falloff != null) {
      falloff.updateFalloffValue(element, x, y);
    }
  }

  /**
   * checks to see if the given mouse position is inside the hover falloff.
   * @param x x position of cursor
   * @param y y position of cursor
   * @return true, when inside and false otherwise
   */
  public final boolean isInsideFalloff(final int x, final int y) {
    if (falloff != null) {
      return falloff.isInside(element, x, y);
    } else {
      return element.isMouseInsideElement(x, y);
    }
  }

  public boolean isOverlay() {
    return overlay;
  }
}
