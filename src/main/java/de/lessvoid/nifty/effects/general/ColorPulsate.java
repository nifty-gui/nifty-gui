package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * ColorPulsate.
 * @author void
 */
public class ColorPulsate implements EffectImpl {

  /**
   * start color.
   */
  private Color startColor;

  /**
   * end color.
   */
  private Color endColor;

  /**
   * pulsator.
   */
  private Pulsator pulsator;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    pulsator = new Pulsator(parameter, new TimeProvider());
  }

  /**
   * execute the effect.
   * @param element the Element
   * @param normalizedTime TimeInterpolator to use
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final NiftyRenderEngine r) {
    float value = pulsator.update();
    Color c = startColor.linear(endColor, value);
    r.setColor(c);
  }

}
