package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.effects.shared.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

/**
 * HoverEffect.
 * @author void
 */
public class HoverEffect implements Effect {

  /**
   * effect active.
   */
  private boolean active;

  /**
   * the element we're bound to.
   */
  private Element element;

  /**
   * the TimeInterpolator this effect uses.
   */
  private TimeInterpolator timeInterpolator;

  /**
   * EffectImpl.
   */
  private HoverEffectImpl effectImpl;

  /**
   * hover falloff.
   */
  private Falloff falloff;

  /**
   * current fallofValue.
   */
  private float falloffValue;

  /**
   * parameter.
   */
  private Properties parameter;

  /**
   * RenderDevice.
   */
  private RenderEngine renderDevice;

  /**
   * post (true) or pre (false) effect.
   */
  private boolean post;

  /**
   * alternate key.
   */
  private String alternateKey;

  /**
   * alternate enable (true) or disable (false) mode.
   */
  private boolean alternateEnable;

  /**
   * inherit changes done to renderStates to child elements.
   */
  private boolean inherit;

  /**
   * nifty.
   */
  private Nifty nifty;

  /**
   * constructor.
   * @param newNifty Nifty
   * @param newInherit inherit changes done to renderStates to child elements
   * @param newPost post or pre effect
   * @param newAlternateKey alternate key
   * @param newAlternateEnable alternate mode (enable or disable effect)
   */
  public HoverEffect(
      final Nifty newNifty,
      final boolean newInherit,
      final boolean newPost,
      final String newAlternateKey,
      final boolean newAlternateEnable) {
    this.nifty = newNifty;
    this.inherit = newInherit;
    this.active = false;
    this.post = newPost;
    this.alternateKey = newAlternateKey;
    this.alternateEnable = newAlternateEnable;
  }

  /**
   * initialize the effect.
   * @param newElement Element
   * @param newEffectImpl HoverEffectImpl
   * @param newParameter Properties
   * @param time TimeProvider
   */
  public void init(
      final Element newElement,
      final HoverEffectImpl newEffectImpl,
      final Properties newParameter,
      final TimeProvider time) {
    this.effectImpl = newEffectImpl;
    this.element = newElement;
    this.parameter = newParameter;
    this.falloff = new Falloff(newParameter);
    this.timeInterpolator = new TimeInterpolator(newParameter, time);
  }

  /**
   * start the effect.
   */
  public void start() {
    this.active = true;
    timeInterpolator.start();
    effectImpl.initialize(nifty, element, parameter);
  }

  /**
   * update effect.
   */
  public void update() {
    timeInterpolator.update();
  }

  /**
   * execute the effect.
   * @param r RenderDevice
   */
  public void execute(final RenderEngine r) {
    effectImpl.execute(element, timeInterpolator.getValue(), falloffValue, r);
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
    this.active = newActive;
  }
  /**
   * hover distance.
   * @param x x position
   * @param y y position
   */
  public final void hoverDistance(final int x, final int y) {
    this.falloffValue = falloff.getFalloffDistance(element, x, y);
  }

  /**
   * checks to see if the given mouse position is inside the hover falloff.
   * @param x x position of cursor
   * @param y y position of cursor
   * @return true, when inside and false otherwise
   */
  public final boolean isInsideFalloff(final int x, final int y) {
    return falloff.isInside(element, x, y);
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
    effectImpl.getClass().getName()
    +
    ", active: " + (active ? "1" : "0");
  }

  /**
   * should this effect inherit to children.
   * @return true, when yes and false, when not
   */
  public boolean isInherit() {
    return inherit;
  }
}
