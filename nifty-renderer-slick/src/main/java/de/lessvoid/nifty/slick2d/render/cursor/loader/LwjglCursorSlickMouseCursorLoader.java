package de.lessvoid.nifty.slick2d.render.cursor.loader;

import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageDataFactory;

import de.lessvoid.nifty.slick2d.render.cursor.LwjglCursorSlickMouseCursor;
import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;

/**
 * This loader is used to load slick mouse cursors that work with LWJGL mouse
 * cursors.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class LwjglCursorSlickMouseCursorLoader implements
    SlickMouseCursorLoader {

    /**
     * Load a new mouse cursor.
     */
    @Override
    public SlickMouseCursor loadCursor(final String filename, final int hotspotX,
        final int hotspotY) throws SlickLoadCursorException {
        
        final ImageData data = ImageDataFactory.getImageDataFor(filename);
        
        try {
            return new LwjglCursorSlickMouseCursor(CursorLoader.get().getCursor(data, hotspotX, data.getHeight() - hotspotY + 1));
        } catch (final Exception e) {
            throw new SlickLoadCursorException("Failed loading cursor.", e);
        }
    }

}
