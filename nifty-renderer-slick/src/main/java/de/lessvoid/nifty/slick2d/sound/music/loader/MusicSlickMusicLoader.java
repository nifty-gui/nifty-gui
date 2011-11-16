package de.lessvoid.nifty.slick2d.sound.music.loader;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.slick2d.sound.music.MusicSlickMusicHandle;
import de.lessvoid.nifty.slick2d.sound.music.SlickLoadMusicException;
import de.lessvoid.nifty.slick2d.sound.music.SlickMusicHandle;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * The Slick music loader that uses Slick music objects for playing the music.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class MusicSlickMusicLoader implements SlickMusicLoader {
    /**
     * Load the music.
     */
    @Override
    public SlickMusicHandle loadMusic(final SoundSystem soundSystem,
        final String filename) throws SlickLoadMusicException {

        try {
            return new MusicSlickMusicHandle(soundSystem, new Music(filename,
                true));
        } catch (SlickException e) {
            throw new SlickLoadMusicException("Loading the music \""
                + filename + "\" failed.", e);
        }
    }

}
