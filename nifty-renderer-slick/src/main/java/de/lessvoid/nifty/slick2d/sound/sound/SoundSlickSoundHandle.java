package de.lessvoid.nifty.slick2d.sound.sound;

import org.newdawn.slick.Sound;

import de.lessvoid.nifty.sound.SoundSystem;

/**
 * This Slick music handle uses the slick music class to implement and playback
 * music.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class SoundSlickSoundHandle implements SlickSoundHandle {
  /**
   * The sound object that is used for playback.
   */
  private final Sound sound;

  /**
   * The sound system this music is played by.
   */
  private final SoundSystem soundSys;

  /**
   * Create a new sound handle that wraps a Slick Sound object.
   * 
   * @param soundSystem
   *          the sound system that manages this sound
   * @param sound
   *          the sound object that is used for playback
   */
  public SoundSlickSoundHandle(final SoundSystem soundSystem, final Sound sound) {
    this.sound = sound;
    soundSys = soundSystem;
  }

  /**
   * Erase the sound instance from the memory.
   */
  @Override
  public void dispose() {
    // by default nothing needs to be disposed
  }

  /**
   * Get the currently set volume of this sound.
   */
  @Override
  public float getVolume() {
    return soundSys.getSoundVolume();
  }

  /**
   * Check if this sound is currently playing.
   */
  @Override
  public boolean isPlaying() {
    return sound.playing();
  }

  /**
   * Start playing the sound.
   */
  @Override
  public void play() {
    sound.play(1.f, soundSys.getSoundVolume());
  }

  /**
   * Change the sound volume. This how ever is not supported by the Slick
   * sounds.
   */
  @Override
  public void setVolume(final float volume) {
    // sound can't change the volume on the fly.
  }

  /**
   * Stop playing the sound.
   */
  @Override
  public void stop() {
    sound.stop();
  }

}
