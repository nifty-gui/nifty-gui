package de.lessvoid.nifty.sound.paulssoundsystem;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

import javax.annotation.Nonnull;


public class PaulsSoundHandle implements de.lessvoid.nifty.spi.sound.SoundHandle {
  @Nonnull
  private final SoundSystem soundSystem;
  private String id;
  private final String filename;

  public PaulsSoundHandle(@Nonnull final SoundSystem soundSystem, final String filename) {
    this.soundSystem = soundSystem;
    this.filename = filename;
    soundSystem.loadSound(filename);
  }

  @Override
  public void play() {
    id = soundSystem.quickPlay(true, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
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
