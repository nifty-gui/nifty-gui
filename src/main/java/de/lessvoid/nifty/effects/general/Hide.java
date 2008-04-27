package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.tools.Color;

/**
 * Fade effect - blend stuff in or out.
 * @author void
 */
public class Hide implements EffectImpl {

  /**
   * initialize.
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
  public void execute(final Element element, final float normalizedTime, final RenderDevice r) {
    if (normalizedTime >= 1.0f) {
      Color c = Color.WHITE;
      r.setColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    } else {
      r.setColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
  }
}


