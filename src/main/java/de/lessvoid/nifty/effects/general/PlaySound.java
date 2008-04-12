package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.sound.SoundHandle;

/**
 * Play a sound.
 * @author void
 */
public class PlaySound implements EffectImpl {

  /**
   * play sound only once helper.
   */
  private boolean done;

  /**
   * SoundHandle.
   */
  private SoundHandle soundHandle;

  /**
   * Initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    soundHandle = nifty.getSoundSystem().getSound(parameter.getProperty("sound"));
    done = false;
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final RenderDevice r) {
    if (normalizedTime > 0.0f) {
      if (soundHandle != null && !done) {
        soundHandle.play();
        done = true;
      }
    }
  }
}


