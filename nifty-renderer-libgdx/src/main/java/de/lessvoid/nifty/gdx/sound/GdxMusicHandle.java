package de.lessvoid.nifty.gdx.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * This is the sound handle used for background music.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class GdxMusicHandle implements SoundHandle {
  /**
   * The instance of the music that is played.
   */
  private final Music music;

  /**
   * The asset manager used to load the music.
   */
  private final AssetManager assetManager;

  /**
   * The volume the music is currently played at.
   */
  private float currentVolume;

  /**
   * Create a new sound handle for background music.
   *
   * @param assetManager the asset manager used to load the music
   * @param soundSystem the sound system of the nifty gui
   * @param fileName the file name that is the load to fetch the new music
   */
  public GdxMusicHandle(final AssetManager assetManager, final SoundSystem soundSystem, final String fileName) {
    this.assetManager = assetManager;

    if (!assetManager.isLoaded(fileName, Music.class)) {
      assetManager.load(fileName, Music.class);
      assetManager.finishLoading();
    }
    music = assetManager.get(fileName, Music.class);
    currentVolume = soundSystem.getMusicVolume();
    music.setVolume(currentVolume);
  }


  @Override
  public void play() {
    music.play();
  }

  @Override
  public void stop() {
    music.stop();
  }

  @Override
  public void setVolume(final float volume) {
    currentVolume = volume;
    music.setVolume(volume);
  }

  @Override
  public float getVolume() {
    return currentVolume;
  }

  @Override
  public boolean isPlaying() {
    return music.isPlaying();
  }

  @Override
  public void dispose() {
    assetManager.unload(assetManager.getAssetFileName(music));
  }
}
