package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
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
  private Color color;
  private SizeValue width;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    color = new Color(parameter.getProperty("color", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(null);
    if (falloff == null) {
      r.setColor(color);
    } else {
      r.setColor(color.mutiply(falloff.getFalloffValue()));
    }
    int size = (int) width.getValue(element.getParent().getWidth());
    if (size == -1) {
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    } else {
      r.renderQuad((element.getX() + element.getWidth() / 2) - size / 2, element.getY(), size, element.getHeight());
    }
    r.restoreState();
  }

  public void deactivate() {
  }
}
