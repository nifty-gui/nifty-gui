package de.lessvoid.nifty.sound.spi.slick;


import org.newdawn.slick.Sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.spi.SoundHandle;

/**
 * A Slick handle to sound.
 * @author void
 */
public class SlickSoundHandle implements SoundHandle {

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
  public SlickSoundHandle(final SoundSystem newSoundSystem, final Sound newSlickSound) {
    this.soundSystem = newSoundSystem;
    this.slickSound = newSlickSound;
  }

  /**
   * Play the sound.
   */
  public void play() {
    slickSound.play(1.0f, soundSystem.getSoundVolume());
  }
}
