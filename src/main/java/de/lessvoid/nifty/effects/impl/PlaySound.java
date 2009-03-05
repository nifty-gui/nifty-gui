package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.sound.SoundHandle;

/**
 * Play a sound.
 * @author void
 */
public class PlaySound implements EffectImpl {

  private boolean done;
  private SoundHandle soundHandle;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    soundHandle = nifty.getSoundSystem().getSound(parameter.getProperty("sound"));
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
          soundHandle.play();
          done = true;
        }
      }
    }
  }

  public void deactivate() {
  }
}


