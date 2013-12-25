package de.lessvoid.nifty.sound.paulssoundsystem;

import de.lessvoid.nifty.spi.sound.SoundHandle;
import paulscode.sound.SoundSystem;

public class PaulsMusicHandle implements SoundHandle {
  private final SoundSystem soundSystem;
  private final String id;
  private final String filename;

  public PaulsMusicHandle(final SoundSystem soundSystem, final String id, final String filename) {
    this.soundSystem = soundSystem;
    this.id = id;
    this.filename = filename;
  }

  @Override
  public void play() {
    soundSystem.backgroundMusic(id, filename, false);
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    soundSystem.stop(id);
  }

  @Override
  public void setVolume(final float volume) {
    soundSystem.setVolume(id, volume);
  }

  @Override
  public float getVolume() {
    return soundSystem.getVolume(id);
  }

  @Override
  public boolean isPlaying() {
    return soundSystem.playing(id);
  }

  @Override
  public void dispose() {
    soundSystem.removeSource(id);
  }
}
