package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * Fade a sound out.
 *
 * @author void
 */
public class FadeMusic implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(FadeMusic.class.getName());
  @Nullable
  private Nifty nifty;
  private float fromVolume;
  private float toVolume;
  @Nullable
  private SoundHandle music;

  @Override
  public void activate(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    nifty = niftyParam;
    music = nifty.getSoundSystem().getMusic(parameter.getProperty("sound"));
    if (music == null) {
      log.warning("Failed to get music for effect.");
    } else {
      fromVolume = new SizeValue(parameter.getProperty("from", "0%")).getValue(1.0f);
      toVolume = new SizeValue(parameter.getProperty("to", "100%")).getValue(1.0f);
    }
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (nifty == null) {
      log.warning("FadeMusic effect is executed before it was activated.");
      return;
    }
    if (music != null) {
      float volume = normalizedTime * (toVolume - fromVolume) + fromVolume;
      music.setVolume(volume);
      nifty.getSoundSystem().setMusicVolume(volume);
    }
  }

  @Override
  public void deactivate() {
  }
}
