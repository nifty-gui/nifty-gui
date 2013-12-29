package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a sound device that does not play or create any music. In case the GUI is not supposed to play music,
 * this device works.
 */
public class NullSoundDevice implements SoundDevice {
  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
  }

  @Nullable
  @Override
  public SoundHandle loadSound(@Nonnull SoundSystem soundSystem, @Nonnull String filename) {
    return null;
  }

  @Nullable
  @Override
  public SoundHandle loadMusic(@Nonnull SoundSystem soundSystem, @Nonnull String filename) {
    return null;
  }

  @Override
  public void update(int delta) {
  }
}
