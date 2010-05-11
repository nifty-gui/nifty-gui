package de.lessvoid.nifty.renderer.lwjgl.sound;

import java.util.logging.Logger;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Slick Implementation of the SoundLoader.
 * @author void
 */
public class OpenALSoundDevice implements SoundDevice {
  private static Logger log = Logger.getLogger(SoundSystem.class.getName());

  public SoundHandle loadSound(final SoundSystem soundSystem, final String filename) {
    return null;
  }

  public SoundHandle loadMusic(final SoundSystem soundSystem, final String filename) {
    return null;
  }

  public void update(final int delta) {
  }
}
