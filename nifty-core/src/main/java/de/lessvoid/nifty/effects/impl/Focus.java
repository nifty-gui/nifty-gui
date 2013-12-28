package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TargetElementResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * focus on hover.
 *
 * @author void
 */
public class Focus implements EffectImpl {

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      return;
    }
    TargetElementResolver resolver = new TargetElementResolver(nifty.getCurrentScreen(), element);
    Element targetElement = resolver.resolve(parameter.getProperty("targetElement"));

    if (targetElement != null) {
      targetElement.setFocus();
    } else {
      element.setFocus();
    }
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
  }

  @Override
  public void deactivate() {
  }
}
