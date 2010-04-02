package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Fade a sound out.
 * @author void
 */
public class FadeSound implements EffectImpl {
  private SoundHandle soundHandle;
  private Nifty nifty;
  private String soundId;

  public void activate(final Nifty niftyParam, final Element element, final EffectProperties parameter) {
    nifty = niftyParam;
    soundId = parameter.getProperty("sound");
    soundHandle = nifty.getSoundSystem().getSound(soundId);
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (soundHandle == null) {
      return;
    }
    soundHandle.setVolume(1.0f - normalizedTime);
    if (normalizedTime >= 1.0f) {
      soundHandle.stop();
    }
  }

  public void deactivate() {
  }
}
