package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Color - color overlay.
 * @author void
 */
public class ColorBar implements HoverEffectImpl {

  /**
   * color of color bar.
   */
  private Color color;

  /**
   * width of color bar.
   */
  private SizeValue width;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    color = new Color(parameter.getProperty("color", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
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
      final RenderDevice r) {
    r.saveState(RenderState.allStates());
    r.enableBlend();
    r.disableTexture();
    r.setColor(
        color.getRed() * normalizedFalloff,
        color.getGreen() * normalizedFalloff,
        color.getBlue() * normalizedFalloff,
        color.getAlpha() * normalizedFalloff);
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }
}
