package de.lessvoid.nifty.gdx.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * This is the sound handle used to play a sound effect.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class GdxSoundHandle implements SoundHandle {
  /**
   * The instance of the sound that is played.
   */
  private final Sound sound;

  /**
   * The asset manager used to load the sound.
   */
  private final AssetManager assetManager;

  /**
   * The volume the sound is currently played at.
   */
  private float currentVolume;

  /**
   * The ID of the sound that is played.
   */
  private long soundId;

  /**
   * Create a new sound handle for a sound effect.
   *
   * @param assetManager the asset manager used to load the sound
   * @param soundSystem the sound system of the nifty gui
   * @param fileName the file name that is the load to fetch the new sound
   */
  public GdxSoundHandle(final AssetManager assetManager, final SoundSystem soundSystem, final String fileName) {
    this.assetManager = assetManager;

    if (!assetManager.isLoaded(fileName, Sound.class)) {
      assetManager.load(fileName, Sound.class);
      assetManager.finishLoading();
    }
    sound = assetManager.get(fileName, Sound.class);
    currentVolume = soundSystem.getMusicVolume();
  }

  @Override
  public void play() {
    soundId = sound.play(currentVolume);
  }

  @Override
  public void stop() {
    sound.stop(soundId);
  }

  @Override
  public void setVolume(final float volume) {
    currentVolume = volume;
    sound.setVolume(soundId, volume);
  }

  @Override
  public float getVolume() {
    return currentVolume;
  }

  @Override
  public boolean isPlaying() {
    return false;
  }

  @Override
  public void dispose() {
    stop();
    assetManager.unload(assetManager.getAssetFileName(sound));
  }
}
