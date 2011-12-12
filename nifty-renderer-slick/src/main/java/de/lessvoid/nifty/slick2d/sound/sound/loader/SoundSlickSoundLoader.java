package de.lessvoid.nifty.slick2d.sound.sound.loader;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import de.lessvoid.nifty.slick2d.sound.sound.SlickLoadSoundException;
import de.lessvoid.nifty.slick2d.sound.sound.SlickSoundHandle;
import de.lessvoid.nifty.slick2d.sound.sound.SoundSlickSoundHandle;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * Load a Slick sound handle that wraps a slick sound.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SoundSlickSoundLoader implements SlickSoundLoader {
  /**
   * Load the sound.
   */
  @Override
  public SlickSoundHandle loadSound(final SoundSystem soundSystem, final String filename)
      throws SlickLoadSoundException {
    try {
      return new SoundSlickSoundHandle(soundSystem, new Sound(filename));
    } catch (final SlickException e) {
      throw new SlickLoadSoundException("Loading the sound \"" + filename + "\" failed.", e);
    }
  }

}
