package de.lessvoid.nifty.spi.sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * SoundDevice loads sounds and music and handles updating the active streams as needed.
 *
 * @author void
 */
public interface SoundDevice {
  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * Load the Sound with the given name. Caching already loaded handles is done by the calling class.
   *
   * @param soundSystem the SoundSystem
   * @param filename filename to load
   * @return initialized SoundHandle or {@code null} in case of any errors
   */
  @Nullable
  SoundHandle loadSound(@Nonnull SoundSystem soundSystem, @Nonnull String filename);

  /**
   * Load the Music with the given name. Caching already loaded handles is done by the calling class.
   *
   * @param soundSystem the SoundSystem
   * @param filename filename to load
   * @return initialized SoundHandle or {@code null} in case of any errors
   */
  @Nullable
  SoundHandle loadMusic(@Nonnull SoundSystem soundSystem, @Nonnull String filename);

  /**
   * Called from the SoundSystem in regular intervals with the given delta time in ms.
   *
   * @param delta delta from last call in ms
   */
  void update(int delta);
}
