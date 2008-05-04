package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.pulsate.Pulsater;

/**
 * Color - color overlay.
 * @author void
 */
public class Pulsate implements EffectImpl {

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
   * the pulsater.
   */
  private Pulsater pulsater;

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
    this.pulsater = new Pulsater(parameter, new TimeProvider());
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
      final RenderDevice r) {
    r.saveState(RenderState.allStates());
    r.enableBlend();
    r.disableTexture();

    float value = pulsater.update();
    Color c = startColor.linear(endColor, value);
    r.setColor(
        c.getRed(),
        c.getGreen(),
        c.getBlue(),
        c.getAlpha());
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }

}
