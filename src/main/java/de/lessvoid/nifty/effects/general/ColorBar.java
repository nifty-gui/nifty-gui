package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Color - color overlay.
 * @author void
 */
public class ColorBar implements EffectImpl {

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
   * @param r RenderDevice to use
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final NiftyRenderEngine r) {
    r.saveState(RenderStateType.allStates());
    r.setColor(color);
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }
}
