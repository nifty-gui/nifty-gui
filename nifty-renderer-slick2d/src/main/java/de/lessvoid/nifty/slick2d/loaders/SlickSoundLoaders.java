package de.lessvoid.nifty.slick2d.loaders;

import de.lessvoid.nifty.slick2d.sound.sound.SlickLoadSoundException;
import de.lessvoid.nifty.slick2d.sound.sound.SlickSoundHandle;
import de.lessvoid.nifty.slick2d.sound.sound.loader.SlickSoundLoader;
import de.lessvoid.nifty.slick2d.sound.sound.loader.SoundSlickSoundLoader;
import de.lessvoid.nifty.sound.SoundSystem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * This maintains the list of known sound loaders and queries them one by one in order to load a sound.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickSoundLoaders extends AbstractSlickLoaders<SlickSoundLoader> {
  /**
   * The logger used to print log messages.
   */
  private static final Logger log = Logger.getLogger(SlickSoundLoaders.class.getName());

  /**
   * The singleton instance of this class.
   */
  private static final SlickSoundLoaders INSTANCE = new SlickSoundLoaders();

  /**
   * Get the singleton instance of this class.
   *
   * @return the singleton instance
   */
  @Nonnull
  public static SlickSoundLoaders getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor so no instances but the singleton instance are created.
   */
  private SlickSoundLoaders() {
  }

  /**
   * Load the default loaders.
   */
  @Override
  public void loadDefaultLoaders(@Nonnull final SlickAddLoaderLocation order) {
    addLoader(new SoundSlickSoundLoader(), order);
  }

  /**
   * Load a sound using the registered loaders.
   *
   * @param soundSystem the sound system that stores the sound data
   * @param filename    the name of the file that holds the sound
   * @return the loaded sound
   * @throws IllegalArgumentException in case loading the sound fails
   */
  @Nullable
  @SuppressWarnings("TypeMayBeWeakened")
  public SlickSoundHandle loadSound(final SoundSystem soundSystem, final String filename) {
    final Iterator<SlickSoundLoader> itr = getLoaderIterator();

    while (itr.hasNext()) {
      try {
        return itr.next().loadSound(soundSystem, filename);
      } catch (@Nonnull final SlickLoadSoundException ignored) {
        // this loader failed... does not matter
      }
    }

    log.warning("Failed to load sound \"" + filename + "\".");
    return null;
  }
}
