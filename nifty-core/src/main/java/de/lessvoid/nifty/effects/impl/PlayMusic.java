package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Play music.
 * @author void
 */
public class PlayMusic implements EffectImpl {
  private boolean done;
  private boolean repeat;
  private SoundHandle musicHandle;
  private Nifty nifty;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    this.nifty = nifty;
    musicHandle = nifty.getSoundSystem().getMusic(parameter.getProperty("music"));
    repeat = Boolean.valueOf(parameter.getProperty("repeat", "false"));
    done = false;
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (normalizedTime > 0.0f) {
      if (musicHandle != null) {
        if (!done) {
          playMusic();
          done = true;
        } else {
          // in repeat mode, when the sound is not playing anymore we'll simply start it again
          if (repeat) {
            if (!musicHandle.isPlaying()) {
              playMusic();
            }
          }
        }
      }
    }
  }

  private void playMusic() {
    musicHandle.setVolume(nifty.getSoundSystem().getMusicVolume());
    musicHandle.play();
  }

  public void deactivate() {
  }
}
