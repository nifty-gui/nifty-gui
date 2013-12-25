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

public class Show implements EffectImpl {
  @Nullable
  private Element targetElement;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      return;
    }
    TargetElementResolver resolver = new TargetElementResolver(screen, element);
    targetElement = resolver.resolve(parameter.getProperty("targetElement"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (targetElement != null) {
      targetElement.show();
    } else {
      element.show();
    }
  }

  @Override
  public void deactivate() {
  }
}
