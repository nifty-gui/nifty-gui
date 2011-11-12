package de.lessvoid.nifty.sound.paulssoundsystem;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;


public class PaulsSoundHandle implements de.lessvoid.nifty.spi.sound.SoundHandle {
  private SoundSystem soundSystem;
  private String id;
  private String filename;

  public PaulsSoundHandle(final SoundSystem soundSystem, final String filename) {
    this.soundSystem = soundSystem;
    this.filename = filename;
    soundSystem.loadSound(filename);
  }

  public void play() {
    id = soundSystem.quickPlay(true, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
  }

  public void stop() {
    soundSystem.stop(id);
  }

  public void setVolume(final float volume) {
    soundSystem.setVolume(id, volume);
  }

  public float getVolume() {
    return soundSystem.getVolume(id);
  }

  public boolean isPlaying() {
    return soundSystem.playing(id);
  }

  public void dispose() {
    soundSystem.removeSource(id);
  }
}
