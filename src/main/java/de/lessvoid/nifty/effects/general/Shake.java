package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * Shake effect.
 * @author void
 */
public class Shake implements EffectImpl {

  /**
   * distance.
   */
  private float distance;

  /**
   * global.
   */
  private boolean global;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    distance = Float.parseFloat(parameter.getProperty("distance", "10.0"));
    global = "true".equals(parameter.getProperty("global", "true").toLowerCase());
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final NiftyRenderEngine r) {
    float t = normalizedTime;
    if (t > 0.0f) {
      float d = distance;
      float x = -d + (float) Math.random() * 2 * d;
      float y = -d + (float) Math.random() * 2 * d;

      if (global) {
        r.setGlobalPosition(x, y);
      } else {
        r.moveTo(x, y);
      }
    }
  }
}
