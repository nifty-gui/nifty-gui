package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TargetElementResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Remote implements EffectImpl {
  @Nullable
  private Element targetElement;
  @Nullable
  private EffectEventId effectEventId;

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
    if (targetElement != null) {
      effectEventId = (EffectEventId) parameter.get("effectEventId");
      targetElement.startEffect(effectEventId, null);
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
    if (targetElement != null && effectEventId != null) {
      targetElement.stopEffect(effectEventId);
    }
  }
}


