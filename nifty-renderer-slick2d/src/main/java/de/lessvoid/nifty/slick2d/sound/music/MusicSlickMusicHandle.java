package de.lessvoid.nifty.slick2d.sound.music;

import de.lessvoid.nifty.sound.SoundSystem;
import org.newdawn.slick.Music;

/**
 * This Slick music handle uses the slick music class to implement and playback music.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MusicSlickMusicHandle implements SlickMusicHandle {
  /**
   * The music object that is used for playback.
   */
  private final Music music;

  /**
   * The sound system this music is played by.
   */
  private final SoundSystem soundSys;

  /**
   * Create a new music handle that wraps a Slick music object.
   *
   * @param soundSystem     the sound system that manages this music
   * @param backgroundMusic the music object that is used for playback
   */
  public MusicSlickMusicHandle(final SoundSystem soundSystem, final Music backgroundMusic) {
    music = backgroundMusic;
    soundSys = soundSystem;
  }

  /**
   * Erase the music instance from the memory.
   */
  @Override
  public void dispose() {
    // nothing
  }

  /**
   * Get the currently set volume of this music.
   */
  @Override
  public float getVolume() {
    return music.getVolume();
  }

  /**
   * Check if this music is currently playing.
   */
  @Override
  public boolean isPlaying() {
    return music.playing();
  }

  /**
   * Start playing the music.
   */
  @Override
  public void play() {
    music.play(1.0f, soundSys.getMusicVolume());
  }

  /**
   * Alter the volume of this music track.
   */
  @Override
  public void setVolume(final float volume) {
    music.setVolume(volume);
  }

  /**
   * Stop playing the music.
   */
  @Override
  public void stop() {
    music.stop();
  }

}
