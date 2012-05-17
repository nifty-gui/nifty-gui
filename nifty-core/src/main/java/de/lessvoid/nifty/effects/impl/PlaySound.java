package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Play a sound.
 * @author void
 */
public class PlaySound implements EffectImpl {
  private boolean done;
  private boolean repeat;
  private SoundHandle soundHandle;
  private Nifty nifty;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    this.nifty = nifty;
    soundHandle = nifty.getSoundSystem().getSound(parameter.getProperty("sound"));
    repeat = Boolean.valueOf(parameter.getProperty("repeat", "false"));
    done = false;
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
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
    soundHandle.setVolume(nifty.getSoundSystem().getSoundVolume());
    soundHandle.play();
  }

  public void deactivate() {
  }
}
