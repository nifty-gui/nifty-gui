package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * Fade effect - blend stuff in or out.
 * @author void
 */
public class Fade implements EffectImpl {

  /**
   * start color.
   */
  private Color start = Color.BLACK;

  /**
   * end color.
   */
  private Color end = Color.WHITE;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    start = new Color(parameter.getProperty("startColor", "#000000ff"));
    end = new Color(parameter.getProperty("endColor", "#ffffffff"));
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final NiftyRenderEngine r) {
    Color c = start.linear(end, normalizedTime);
    r.setColor(c);
  }
}


