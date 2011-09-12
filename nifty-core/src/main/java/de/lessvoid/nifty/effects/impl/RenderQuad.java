package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * This renders a quad AND interpolates the color between startColor and endColor
 * over the lifetime of the effect.
 * @author void
 */
public class RenderQuad implements EffectImpl {
  private Color currentColor = new Color("#000f");
  private Color tempColor = new Color("#000f");
  private Color startColor;
  private Color endColor;
  private SizeValue width;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#0000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffff"));
    width = new SizeValue(parameter.getProperty("width"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(null);

    currentColor.linear(startColor, endColor, normalizedTime);
    if (falloff == null) {
      r.setColor(currentColor);
    } else {
      tempColor.mutiply(currentColor, falloff.getFalloffValue());
      r.setColor(tempColor);
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
