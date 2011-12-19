package de.lessvoid.nifty.slick2d.sound.sound.loader;

import de.lessvoid.nifty.slick2d.loaders.SlickLoader;
import de.lessvoid.nifty.slick2d.sound.sound.SlickLoadSoundException;
import de.lessvoid.nifty.slick2d.sound.sound.SlickSoundHandle;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * The interface for all loaders that take care for loading sounds.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickSoundLoader extends SlickLoader {
  /**
   * Load a new sound.
   * 
   * @param soundSystem
   *          the sound system that manages the sound
   * @param filename
   *          the name of the file storing the sound data
   * @return the loaded sound
   * @throws SlickLoadSoundException
   *           in case loading the sound fails
   */
  SlickSoundHandle loadSound(SoundSystem soundSystem, String filename) throws SlickLoadSoundException;
}
