package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

/**
 * TextColor Effect.
 *
 * @author void
 */
public class TextColor implements EffectImpl {
  private Color color;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    color = new de.lessvoid.nifty.tools.Color(parameter.getProperty("color", "#ffff"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  @Override
  public void deactivate() {
  }
}
