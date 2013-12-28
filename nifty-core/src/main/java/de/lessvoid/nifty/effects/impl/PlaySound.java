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
 * Play a sound.
 *
 * @author void
 */
public class PlaySound implements EffectImpl {
  private boolean done;
  private boolean repeat;
  @Nullable
  private SoundHandle soundHandle;
  @Nullable
  private Nifty nifty;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    this.nifty = nifty;
    soundHandle = nifty.getSoundSystem().getSound(parameter.getProperty("sound"));
    repeat = Boolean.valueOf(parameter.getProperty("repeat", "false"));
    done = false;
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (normalizedTime > 0.0f) {
      if (soundHandle != null) {
        if (!done) {
          playSound();
          done = true;
        } else {
          // in repeat mode, when the sound is not playing anymore we'll simply start it again
          if (repeat) {
            if (!soundHandle.isPlaying()) {
              playSound();
            }
          }
        }
      }
    }
  }

  private void playSound() {
    if (soundHandle != null && nifty != null) {
      soundHandle.setVolume(nifty.getSoundSystem().getSoundVolume());
      soundHandle.play();
    }
  }

  @Override
  public void deactivate() {
  }
}
