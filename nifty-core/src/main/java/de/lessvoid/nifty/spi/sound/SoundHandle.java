package de.lessvoid.nifty.spi.sound;

/**
 * A interface to a piece of sound. Ready to be played.
 * @author void
 */
public interface SoundHandle {

  /**
   * Play the sound.
   */
  void play();

  /**
   * Stop the sound.
   */
  void stop();

  /**
   * Change volume of the sound while it is playing.
   * @param volume new value in range 0 to 1
   */
  void setVolume(float volume);
  float getVolume();

  /**
   * Checks if this sound is still playing.
   * @return true if the sound is playing and false if not
   */
  boolean isPlaying();

  /**
   * This Sound is not needed anymore and should be removed from the system.
   */
  void dispose();
}
