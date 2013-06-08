package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class NullSoundDevice implements SoundDevice {
  @Override
  public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
  }
  
  @Override
  public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
    return new NullSoundHandle();
  }
  
  @Override
  public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
    return new NullSoundHandle();
  }
  
  @Override
  public void update(int delta) {
  }

  public static class NullSoundHandle implements SoundHandle {

    @Override
    public void play() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void setVolume(float volume) {
    }

    @Override
    public float getVolume() {
      return 0;
    }

    @Override
    public boolean isPlaying() {
      return false;
    }

    @Override
    public void dispose() {
    }
  }
}
