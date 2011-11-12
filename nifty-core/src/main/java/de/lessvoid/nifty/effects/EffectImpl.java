package de.lessvoid.nifty.effects;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Effect Implementation ... here is the actual fun :>
 * @author void
 */
public interface EffectImpl {

  /**
   * initialize effect.
   * @param nifty Nifty
   * @param element Element
   * @param parameter parameters
   */
  void activate(Nifty nifty, Element element, EffectProperties parameter);

  /**
   * execute the effect.
   * @param element the Element
   * @param effectTime current effect time
   * @param falloff the Falloff class for hover effects. This is supposed to be null for none hover effects.
   * @param r RenderDevice to use
   */
  void execute(Element element, float effectTime, Falloff falloff, NiftyRenderEngine r);

  /**
   * deactivate the effect.
   */
  void deactivate();
}
