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
public class Gradient implements EffectImpl {
  private Color color1;
  private Color color2;
  private Color color3;
  private Color color4;
  private SizeValue width;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    color1 = new Color(parameter.getProperty("color1", "#ffffffff"));
    color2 = new Color(parameter.getProperty("color2", "#ffffffff"));
    color3 = new Color(parameter.getProperty("color3", "#ffffffff"));
    color4 = new Color(parameter.getProperty("color4", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(RenderStateType.allStates());
    r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight() / 2, color1, color2, color3, color4);
    r.renderQuad(element.getX(), element.getY() + element.getHeight() / 2, element.getWidth(), element.getHeight() / 2, color3, color4, color1, color2);
    r.restoreState();
  }

  public void deactivate() {
  }
}
