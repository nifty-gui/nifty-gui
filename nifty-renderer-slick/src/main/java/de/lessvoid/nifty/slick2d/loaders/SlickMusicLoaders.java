package de.lessvoid.nifty.slick2d.loaders;

import java.util.Iterator;
import java.util.logging.Logger;

import de.lessvoid.nifty.slick2d.sound.music.SlickLoadMusicException;
import de.lessvoid.nifty.slick2d.sound.music.SlickMusicHandle;
import de.lessvoid.nifty.slick2d.sound.music.loader.MusicSlickMusicLoader;
import de.lessvoid.nifty.slick2d.sound.music.loader.SlickMusicLoader;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * This maintains the list of known music loaders and queries them one by one in order to load a music.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickMusicLoaders extends AbstractSlickLoaders<SlickMusicLoader> {
  /**
   * The logger used to print log messages.
   */
  private static Logger log = Logger.getLogger(SlickMusicLoaders.class.getName());

  /**
   * The singleton instance of this class.
   */
  private static final SlickMusicLoaders INSTANCE = new SlickMusicLoaders();

  /**
   * Get the singleton instance of this class.
   *
   * @return the singleton instance
   */
  public static SlickMusicLoaders getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor so no instances but the singleton instance are created.
   */
  private SlickMusicLoaders() {
  }

  /**
   * Load the default loaders.
   */
  @Override
  public void loadDefaultLoaders(final SlickAddLoaderLocation order) {
    addLoader(new MusicSlickMusicLoader(), order);
  }

  /**
   * Load a music using the registered loaders.
   *
   * @param soundSystem the sound system that stores the sound data
   * @param filename the name of the file that holds the music
   * @return the loaded music
   * @throws IllegalArgumentException in case loading the music fails
   */
  @SuppressWarnings("TypeMayBeWeakened")
  public SlickMusicHandle loadMusic(final SoundSystem soundSystem, final String filename) {
    final Iterator<SlickMusicLoader> itr = getLoaderIterator();

    while (itr.hasNext()) {
      try {
        return itr.next().loadMusic(soundSystem, filename);
      } catch (final SlickLoadMusicException ignored) {
        // this loader failed... does not matter
      }
    }

    log.warning("Failed to load music \"" + filename + "\".");
    return null;
  }
}
