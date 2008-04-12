package de.lessvoid.nifty.sound.slick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.lessvoid.nifty.sound.SoundHandle;
import de.lessvoid.nifty.sound.SoundLoader;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * Slick Implementation of the SoundLoader.
 * @author void
 */
public class SlickSoundLoader implements SoundLoader {

  /**
   * The logger.
   */
  private static Log log = LogFactory.getLog(SoundSystem.class);

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
      log.error("loading of '" + filename + "' failed", e);
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
      return new SlickMusicHandle(soundSystem, new Music(filename));
    } catch (SlickException e) {
      log.error("loading of '" + filename + "' failed", e);
    }
    return null;
  }

}
