package de.lessvoid.nifty.sound.slick;


import org.newdawn.slick.Music;

import de.lessvoid.nifty.sound.SoundHandle;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * A Slick handle to music.
 * @author void
 */
public class SlickMusicHandle implements SoundHandle {

  /**
   * internal Music handle.
   */
  private Music slickMusic;

  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * Create new instance.
   * @param newSoundSystem the soundsystem we are connected to
   * @param newSlickMusic the Slick Music thing
   */
  public SlickMusicHandle(final SoundSystem newSoundSystem, final Music newSlickMusic) {
    this.soundSystem = newSoundSystem;
    this.slickMusic = newSlickMusic;
  }

  /**
   * Play the music.
   */
  public void play() {
    slickMusic.play(1.0f, soundSystem.getMusicVolume());
  }
}
