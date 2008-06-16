package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Nop effect - does nothing.
 * @author void
 */
public class Nop implements EffectImpl {

  /**
   * Initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final NiftyRenderEngine r) {
  }
}


