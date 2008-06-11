package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.time.TimeInterpolator;

/**
 * An effect can be active or not and is always attached to one element. It
 * has a TimeInterpolator that manages the life time of the effect. The actual
 * effect implementation is done be EffectImpl implementations.
 *
 * @author void
 */
public class StaticEffect implements Effect {

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
  private EffectImpl effectImpl;

  /**
   * saved parameters.
   */
  private Properties parameter;

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
   * Nifty.
   */
  private Nifty nifty;

  /**
   * Constructor.
   * @param newNifty Nifty
   * @param newInherit inherit changes done to renderStates to child elements
   * @param newPost post or pre effect
   * @param newAlternateKey alternate key
   * @param newAlternateEnable alternate mode (enable or disable effect)
   */
  public StaticEffect(
      final Nifty newNifty,
      final boolean newInherit,
      final boolean newPost,
      final String newAlternateKey,
      final boolean newAlternateEnable) {
    this.nifty = newNifty;
    this.inherit = newInherit;
    this.post = newPost;
    this.active = false;
    this.alternateKey = newAlternateKey;
    this.alternateEnable = newAlternateEnable;
  }

  /**
   * initialize the effect.
   * @param newElement Element
   * @param newEffectImpl EffectImpl
   * @param newParameter Properties
   * @param time TimeProvider
   */
  public void init(
      final Element newElement,
      final EffectImpl newEffectImpl,
      final Properties newParameter,
      final TimeProvider time) {
    this.effectImpl = newEffectImpl;
    this.element = newElement;
    this.parameter = newParameter;
    this.timeInterpolator = new TimeInterpolator(parameter, time);
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
    this.active = timeInterpolator.update();
  }

  /**
   * execute the effect.
   * @param r RenderDevice
   */
  public void execute(final RenderEngine r) {
    effectImpl.execute(element, timeInterpolator.getValue(), r);
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
      ": active: " + (active ? "1" : "0")
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
}
