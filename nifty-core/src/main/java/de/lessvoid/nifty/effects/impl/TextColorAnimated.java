package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * TextColor Effect.
 * @author void
 */
public class TextColorAnimated implements EffectImpl {
  private Color currentColor = new Color("#000f");
  private Color tempColor = new Color("#000f");
  private Color startColor;
  private Color endColor;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#0000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffff"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    currentColor.linear(startColor, endColor, normalizedTime);
    if (falloff == null) {
      setColor(r, currentColor);
    } else {
      tempColor.mutiply(currentColor, falloff.getFalloffValue());
      setColor(r, tempColor);
    }
  }

  private void setColor(final NiftyRenderEngine r, final Color color) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  public void deactivate() {
  }
}
