package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Fade a sound out.
 *
 * @author void
 */
public class FadeSound implements EffectImpl {
  @Nullable
  private SoundHandle soundHandle;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    String soundId = parameter.getProperty("sound");
    soundHandle = nifty.getSoundSystem().getSound(soundId);
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (soundHandle == null) {
      return;
    }
    soundHandle.setVolume(1.0f - normalizedTime);
    if (normalizedTime >= 1.0f) {
      soundHandle.stop();
    }
  }

  @Override
  public void deactivate() {
  }
}
