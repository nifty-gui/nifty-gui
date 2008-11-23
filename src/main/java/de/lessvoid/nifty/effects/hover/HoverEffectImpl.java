package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Effect Implementation ... here is the actual fun :>
 * @author void
 */
public interface HoverEffectImpl {

  /**
   * Initialize effect.
   * @param nifty Nifty
   * @param element Element
   * @param parameter parameters
   */
  void initialize(Nifty nifty, Element element, Properties parameter);

  /**
   * Execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param normalizedFalloff falloff value
   * @param renderDevice RenderDevice to use
   */
  void execute(Element element, float normalizedTime, float normalizedFalloff, NiftyRenderEngine renderDevice);

  /**
   * deactivate the effect.
   */
  void deactivate();
}
