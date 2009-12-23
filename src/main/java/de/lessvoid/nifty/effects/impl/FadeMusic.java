package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Fade a sound out.
 * @author void
 */
public class FadeMusic implements EffectImpl {
  private Nifty nifty;
  private float fromVolume;
  private float toVolume;
  private SoundHandle music;

  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    nifty = niftyParam;
    music = nifty.getSoundSystem().getMusic(parameter.getProperty("sound"));
    fromVolume = new SizeValue(parameter.getProperty("from", "0%")).getValue(1.0f);
    toVolume = new SizeValue(parameter.getProperty("to", "100%")).getValue(1.0f);
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    float volume = normalizedTime * (toVolume - fromVolume) + fromVolume;
    music.setVolume(volume);
    nifty.getSoundSystem().setMusicVolume(volume);
  }

  public void deactivate() {
  }
}
