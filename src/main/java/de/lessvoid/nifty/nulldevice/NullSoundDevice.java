package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

public class NullSoundDevice implements SoundDevice {
  
  public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
    return null;
  }
  
  public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
    return null;
  }
  
  public void update(int delta) {
  }  
}
