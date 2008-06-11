package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;

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
  void initialize(Nifty nifty, Element element, Properties parameter);

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param r RenderDevice to use
   */
  void execute(Element element, float normalizedTime, RenderEngine r);
}
