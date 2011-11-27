package de.lessvoid.nifty.spi.sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * SoundLoader loads sounds and music.
 * @author void
 */
public interface SoundDevice {

  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(NiftyResourceLoader niftyResourceLoader);

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
