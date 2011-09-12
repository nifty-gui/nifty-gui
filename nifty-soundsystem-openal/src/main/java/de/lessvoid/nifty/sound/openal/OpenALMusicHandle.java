package de.lessvoid.nifty.sound.openal;


import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * A Slick handle to music.
 * @author void
 */
public class OpenALMusicHandle implements SoundHandle {
  private SoundSystem soundSystem;
  private Music slickMusic;

  /**
   * Create new instance.
   * @param newSoundSystem the soundsystem we are connected to
   * @param newSlickMusic the Slick Music thing
   */
  public OpenALMusicHandle(final SoundSystem newSoundSystem, final Music newSlickMusic) {
    this.soundSystem = newSoundSystem;
    this.slickMusic = newSlickMusic;
  }

  public void play() {
    slickMusic.play(1.0f, soundSystem.getMusicVolume());
  }

  public void stop() {
    slickMusic.stop();
  }

  public void setVolume(final float volume) {
    slickMusic.setVolume(volume);
  }

  public float getVolume() {
    return slickMusic.getVolume();
  }

  public boolean isPlaying() {
    return slickMusic.playing();
  }

  public void dispose() {
    
  }
}
