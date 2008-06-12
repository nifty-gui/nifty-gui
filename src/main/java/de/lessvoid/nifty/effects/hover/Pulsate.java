package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

/**
 * Color - color overlay.
 * @author void
 */
public class Pulsate implements HoverEffectImpl {

  /**
   * start color.
   */
  private Color startColor;

  /**
   * end color.
   */
  private Color endColor;

  /**
   * width of effect.
   */
  private SizeValue width;

  /**
   * pulsater.
   */
  private Pulsator pulsater;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#00000000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
    pulsater = new Pulsator(parameter, new TimeProvider());
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
      final float normalizedFalloff,
      final RenderEngine r) {
    r.saveState(RenderState.allStates());

    float value = pulsater.update();
    Color c = startColor.linear(endColor, value);
    r.setColor(c);
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }

}
