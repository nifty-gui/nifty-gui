package de.lessvoid.nifty.lwjglslick.sound;

import java.util.logging.Logger;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * Slick Implementation of the SoundLoader.
 * @author void
 */
public class SlickSoundDevice implements SoundDevice {

  /**
   * The logger.
   */
  private static Logger log = Logger.getLogger(SoundSystem.class.getName());

  /**
   * Load a sound.
   * @param soundSystem soundSystem
   * @param filename filename of sound
   * @return handle to sound
   */
  public SoundHandle loadSound(final SoundSystem soundSystem, final String filename) {
    try {
      return new SlickSoundHandle(soundSystem, new Sound(filename));
    } catch (SlickException e) {
      log.warning("loading of '" + filename + "' failed" + e);
    }
    return null;
  }

  /**
   * Load a music piece.
   * @param soundSystem soundSystem
   * @param filename file to load
   * @return the music piece
   */
  public SoundHandle loadMusic(final SoundSystem soundSystem, final String filename) {
    try {
      return new SlickMusicHandle(soundSystem, new Music(filename, true));
    } catch (SlickException e) {
      log.warning("loading of '" + filename + "' failed" + e);
    }
    return null;
  }

  public void update(final int delta) {
    Music.poll(delta);
  }
}
