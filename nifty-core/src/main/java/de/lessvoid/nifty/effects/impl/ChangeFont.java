package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ChangeFont.
 *
 * @author void
 */
public class ChangeFont implements EffectImpl {
  @Nullable
  private RenderFont font;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    font = nifty.getRenderEngine().createFont(parameter.getProperty("font"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    r.setFont(font);
  }

  @Override
  public void deactivate() {
  }
}
