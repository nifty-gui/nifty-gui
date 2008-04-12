package de.lessvoid.nifty.sound;

/**
 * SoundLoader loads sounds and music.
 * @author void
 */
public interface SoundLoader {

  /**
   * Load the Sound with the given name.
   * @param soundSystem the SoundSystem
   * @param filename filename to load
   * @return initialized SoundHandle or null in case of any errors
   */
  SoundHandle loadSound(SoundSystem soundSystem, String filename);

  /**
   * Load the Music with the given name.
   * @param soundSystem the SoundSystem
   * @param filename filename to load
   * @return initialized SoundHandle or null in case of any errors
   */
  SoundHandle loadMusic(SoundSystem soundSystem, String filename);
}
