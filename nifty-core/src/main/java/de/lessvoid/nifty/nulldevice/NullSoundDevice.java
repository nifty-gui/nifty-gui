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
    return null;
  }
  
  @Override
  public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
    return null;
  }
  
  @Override
  public void update(int delta) {
  }
}
