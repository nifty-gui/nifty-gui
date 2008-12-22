package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * Fade effect - blend stuff in or out.
 * @author void
 */
public class Fade implements EffectImpl {
  private Color start = Color.BLACK;
  private Color end = Color.WHITE;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    start = new Color(parameter.getProperty("startColor", "#000000ff"));
    end = new Color(parameter.getProperty("endColor", "#ffffffff"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    Color c = start.linear(end, normalizedTime);
    r.setColorAlpha(c.getAlpha());
  }

  public void deactivate() {
  }
}


