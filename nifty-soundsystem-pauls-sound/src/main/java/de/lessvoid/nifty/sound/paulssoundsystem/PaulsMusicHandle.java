package de.lessvoid.nifty.sound.paulssoundsystem;

import paulscode.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

public class PaulsMusicHandle implements SoundHandle {
  private SoundSystem soundSystem;
  private String id;
  private String filename;

  public PaulsMusicHandle(final SoundSystem soundSystem, final String id, final String filename) {
    this.soundSystem = soundSystem;
    this.id = id;
    this.filename = filename;
  }

  public void play() {
    soundSystem.backgroundMusic(id, filename, false);
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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
