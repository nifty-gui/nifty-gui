package de.lessvoid.nifty.sound.openal;


import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * A Slick handle to sound.
 * @author void
 */
public class OpenALSoundHandle implements SoundHandle {

  /**
   * slick sound.
   */
  private Sound slickSound;

  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * Construct a new Sound.
   * @param newSoundSystem the SoundSystem we're connected too
   * @param newSlickSound the slickSound to set
   */
  public OpenALSoundHandle(final SoundSystem newSoundSystem, final Sound newSlickSound) {
    this.soundSystem = newSoundSystem;
    this.slickSound = newSlickSound;
  }

  public void play() {
    slickSound.play(1.0f, soundSystem.getSoundVolume());
  }

  public void stop() {
    slickSound.stop();
  }

  public void setVolume(final float volume) {
    // can't change volume of sound that is currently playing with slick
  }

  public float getVolume() {
    return soundSystem.getSoundVolume();
  }

  public boolean isPlaying() {
    return slickSound.playing();
  }

  public void dispose() {
    
  }
}
