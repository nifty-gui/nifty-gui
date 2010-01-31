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
 * Border - border overlay.
 * @author void
 */
public class Border implements EffectImpl {
  private Color borderColor;
  private SizeValue borderWidth;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    borderColor = new Color(parameter.getProperty("color", "#ffffffff"));
    borderWidth = new SizeValue(parameter.getProperty("border"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(null);
    r.setColor(borderColor);
    int w = (int) borderWidth.getValue(element.getParent().getWidth());
    if (w == -1) {
      w = 1;
    }
    r.renderQuad(element.getX() - w,                  element.getY() - w,                   element.getWidth() + 2 * w, w);
    r.renderQuad(element.getX() - w,                  element.getY() + element.getHeight(), element.getWidth() + 2 * w, w);
    r.renderQuad(element.getX() - w,                  element.getY(),                       w,                          element.getHeight());
    r.renderQuad(element.getX() + element.getWidth(), element.getY(),                       w,                          element.getHeight());
    r.restoreState();
  }

  public void deactivate() {
  }
}
