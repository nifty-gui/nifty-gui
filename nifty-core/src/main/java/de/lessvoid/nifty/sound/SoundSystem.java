package de.lessvoid.nifty.sound;


import java.util.Hashtable;
import java.util.logging.Logger;

import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * The SoundManager loads and manages all available Sound and Music Files available to be played.
 * @author void
 */
public class SoundSystem {
  private static Logger log = Logger.getLogger(SoundSystem.class.getName());

  private SoundDevice soundDevice;
  private Hashtable < String, SoundHandle > soundLookup;

  private float soundVolume;
  private float musicVolume;

  /**
   * create new sound manager.
   * @param newSoundLoader the SoundLoader we should use
   */
  public SoundSystem(final SoundDevice newSoundLoader) {
    soundDevice = newSoundLoader;

    soundVolume = 1.0f;
    musicVolume = 1.0f;

    soundLookup = new Hashtable < String, SoundHandle >();
  }

  /**
   * Add a sound file.
   * @param name name to register sound for
   * @param filename name of the sound file to load
   * @return true on success and false when loading the sound failed
   */
  public boolean addSound(final String name, final String filename) {
    log.fine("register sound [" + name + "] for file '" + filename + "'");

    SoundHandle sound = soundDevice.loadSound(this, filename);
    if (sound == null) {
      return false;
    }

    soundLookup.put(name, sound);
    return true;
  }

  /**
   * Add a music file.
   * @param name name to register the music for
   * @param filename name of music file
   * @return true on success and false when loading the music file failed
   */
  public boolean addMusic(final String name, final String filename) {
    log.fine("register music [" + name + "] for file '" + filename + "'");

    SoundHandle music = soundDevice.loadMusic(this, filename);
    if (music == null) {
      return false;
    }

    soundLookup.put(name, music);
    return true;
  }

  public SoundHandle getSound(final String name) {
    if (name == null) {
      log.warning("unknown sound name given [" + name + "]?");
      return null;
    }

    SoundHandle sound = soundLookup.get(name);
    if (sound == null) {
      log.warning("missing sound [" + name + "]");
      return null;
    }

    return sound;
  }

  public SoundHandle getMusic(final String name) {
    if (name == null) {
      log.warning("unknown music name given [" + name + "]?");
      return null;
    }

    SoundHandle sound = soundLookup.get(name);
    if (sound == null) {
      log.warning("missing sound [" + name + "]");
      return null;
    }

    return sound;
  }

  /**
   * Get current set sound volume.
   * @return the current sound volume.
   */
  public float getSoundVolume() {
    return soundVolume;
  }

  /**
   * Set sound volume.
   * @param newSoundVolume new sound volume
   */
  public void setSoundVolume(final float newSoundVolume) {
    this.soundVolume = newSoundVolume;
  }

  /**
   * Get music volume.
   * @return current music volume [0.0, 1.0]
   */
  public float getMusicVolume() {
    return musicVolume;
  }

  /**
   * Set music volume.
   * @param newMusicVolume new music volume [0.0, 1.0]
   */
  public void setMusicVolume(final float newMusicVolume) {
    this.musicVolume = newMusicVolume;
  }

  public void update(final int delta) {
    soundDevice.update(delta);
  }
}
