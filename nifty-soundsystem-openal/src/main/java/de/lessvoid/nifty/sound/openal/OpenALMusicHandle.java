package de.lessvoid.nifty.sound.openal;


import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * A Slick handle to music.
 *
 * @author void
 */
public class OpenALMusicHandle implements SoundHandle {
  private final SoundSystem soundSystem;
  private final Music slickMusic;

  /**
   * Create new instance.
   *
   * @param newSoundSystem the soundsystem we are connected to
   * @param newSlickMusic  the Slick Music thing
   */
  public OpenALMusicHandle(final SoundSystem newSoundSystem, final Music newSlickMusic) {
    this.soundSystem = newSoundSystem;
    this.slickMusic = newSlickMusic;
  }

  @Override
  public void play() {
    slickMusic.play(1.0f, soundSystem.getMusicVolume());
  }

  @Override
  public void stop() {
    slickMusic.stop();
  }

  @Override
  public void setVolume(final float volume) {
    slickMusic.setVolume(volume);
  }

  @Override
  public float getVolume() {
    return slickMusic.getVolume();
  }

  @Override
  public boolean isPlaying() {
    return slickMusic.playing();
  }

  @Override
  public void dispose() {

  }
}
