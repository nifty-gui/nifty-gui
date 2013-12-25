package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * TextColor Effect.
 *
 * @author void
 */
public class TextColorAnimated implements EffectImpl {
  @Nonnull
  private final Color currentColor = new Color("#000f");
  @Nonnull
  private final Color tempColor = new Color("#000f");
  private Color startColor;
  private Color endColor;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    startColor = new Color(parameter.getProperty("startColor", "#0000"));
    endColor = new Color(parameter.getProperty("endColor", "#ffff"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    currentColor.linear(startColor, endColor, normalizedTime);
    if (falloff == null) {
      setColor(r, currentColor);
    } else {
      tempColor.multiply(currentColor, falloff.getFalloffValue());
      setColor(r, tempColor);
    }
  }

  private void setColor(@Nonnull final NiftyRenderEngine r, @Nonnull final Color color) {
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
