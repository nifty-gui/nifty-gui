package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Border - border overlay.
 * @author void
 */
public class Border implements EffectImpl {

  /**
   * color of border.
   */
  private Color color;

  /**
   * width of border.
   */
  private SizeValue border;

  /**
   * initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    color = new Color(parameter.getProperty("color", "#ffffffff"));
    border = new SizeValue(parameter.getProperty("border"));
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
    int w = (int) border.getValue(element.getParent().getWidth());
    if (w == -1) {
      w = 1;
    }
    r.renderQuad(element.getX() - w, element.getY() - w, element.getWidth() + 2 * w, w);
    r.renderQuad(element.getX() - w, element.getY() + element.getHeight() + w, element.getWidth() + 2 * w, w);
    r.renderQuad(element.getX() - w, element.getY() - w, w, element.getHeight() + 2 * w);
    r.renderQuad(element.getX() + element.getWidth() + w, element.getY() - w, w, element.getHeight() + 2 * w);
    r.restoreState();
  }
}
