package de.lessvoid.nifty.slick2d.sound.music.loader;

import de.lessvoid.nifty.slick2d.loaders.SlickLoader;
import de.lessvoid.nifty.slick2d.sound.music.SlickLoadMusicException;
import de.lessvoid.nifty.slick2d.sound.music.SlickMusicHandle;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * The interface for all music loaders.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickMusicLoader extends SlickLoader {
  /**
   * Load some music.
   *
   * @param soundSystem the Nifty sound system that is going to manage the load music
   * @param filename the name of the file that stores the music
   * @return the loaded music
   * @throws SlickLoadMusicException in case loading the music failed
   */
  SlickMusicHandle loadMusic(SoundSystem soundSystem, String filename) throws SlickLoadMusicException;
}
