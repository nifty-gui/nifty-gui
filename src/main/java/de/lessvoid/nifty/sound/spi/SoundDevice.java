package de.lessvoid.nifty.sound.spi;

import de.lessvoid.nifty.sound.SoundSystem;

/**
 * SoundLoader loads sounds and music.
 * @author void
 */
public interface SoundDevice {

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

  /**
   * Called from the SoundSystem in regular intervals with the given
   * delta time in ms.
   * @param delta delta from last call in ms
   */
  void update(int delta);
}
