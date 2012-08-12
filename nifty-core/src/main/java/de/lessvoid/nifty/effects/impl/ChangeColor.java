package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 * ChangeColor Effect - changes the current color including alpha.
 * @author void
 */
public class ChangeColor implements EffectImpl {
  private Color color;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    color = new de.lessvoid.nifty.tools.Color(parameter.getProperty("color", "#ffff"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.setColor(color);
  }

  public void deactivate() {
  }
}
