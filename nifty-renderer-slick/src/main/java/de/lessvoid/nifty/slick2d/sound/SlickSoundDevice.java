package de.lessvoid.nifty.slick2d.sound;

import de.lessvoid.nifty.slick2d.loaders.SlickMusicLoaders;
import de.lessvoid.nifty.slick2d.loaders.SlickSoundLoaders;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Slick Implementation of the SoundLoader.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickSoundDevice implements SoundDevice {
  /**
   * Load a music piece.
   *
   * @param soundSystem soundSystem
   * @param filename file to load
   * @return the music piece
   */
  @Override
  public SoundHandle loadMusic(final SoundSystem soundSystem, final String filename) {
    return SlickMusicLoaders.getInstance().loadMusic(soundSystem, filename);
  }

  /**
   * Load a sound.
   *
   * @param soundSystem soundSystem
   * @param filename filename of sound
   * @return handle to sound
   */
  @Override
  public SoundHandle loadSound(final SoundSystem soundSystem, final String filename) {
    return SlickSoundLoaders.getInstance().loadSound(soundSystem, filename);
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader resourceLoader) {
    // no need for the resource loader
  }

  /**
   * Update the sound device.
   */
  @Override
  public void update(final int delta) {
    // nothing to do
  }
}
