package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * change textcolor on hover.
 * @author void
 */
public class TextColor implements EffectImpl {

  /**
   * textcolor.
   */
  private Color color;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    color = new de.lessvoid.nifty.tools.Color(parameter.getProperty("color", "#ffffffff"));
  }

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param normalizedFalloff falloff value
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff fFalloff,
      final NiftyRenderEngine r) {
    r.setColor(color);
  }

  /**
   * deactivate the effect.
   */
  public void deactivate() {
  }
}
