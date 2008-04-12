package de.lessvoid.nifty.effects.hover;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.sound.SoundHandle;

/**
 * Play a sound.
 * @author void
 */
public class PlaySound implements HoverEffectImpl {

  /**
   * SoundHandle.
   */
  private SoundHandle soundHandle;

  /**
   * play sound only once helper.
   */
  private boolean done;

  /**
   * initialize.
   * @param nifty nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(
      final Nifty nifty,
      final Element element,
      final Properties parameter) {
    soundHandle = nifty.getSoundSystem().getSound(parameter.getProperty("sound"));
    done = false;
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime time value
   * @param normalizedFalloff fall off value
   * @param r RenderDevice
   */
  public void execute(
      final Element element,
      final float normalizedTime,
      final float normalizedFalloff,
      final RenderDevice r) {
    if (normalizedTime > 0.0f) {
      if (soundHandle != null && !done) {
        soundHandle.play();
        done = true;
      }
    }
  }
}


