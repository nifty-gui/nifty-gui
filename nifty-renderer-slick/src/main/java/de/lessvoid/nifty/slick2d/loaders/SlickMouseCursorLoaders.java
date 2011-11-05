package de.lessvoid.nifty.slick2d.loaders;

import java.util.Iterator;

import de.lessvoid.nifty.slick2d.render.cursor.SlickLoadCursorException;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import de.lessvoid.nifty.slick2d.render.cursor.loader.LwjglCursorSlickMouseCursorLoader;
import de.lessvoid.nifty.slick2d.render.cursor.loader.SlickMouseCursorLoader;

public final class SlickMouseCursorLoaders extends
    AbstractSlickLoaders<SlickMouseCursorLoader> {
    /**
     * The singleton instance of this class.
     */
    private static final SlickMouseCursorLoaders INSTANCE =
        new SlickMouseCursorLoaders();

    /**
     * Private constructor so no instances but the singleton instance are
     * created.
     */
    private SlickMouseCursorLoaders() {
        super();
    }

    /**
     * Get the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static SlickMouseCursorLoaders getInstance() {
        return INSTANCE;
    }

    /**
     * Add the default loaders.
     */
    @Override
    public void loadDefaultLoaders(SlickAddLoaderLocation order) {
        addLoader(new LwjglCursorSlickMouseCursorLoader(), order);
    }

    /**
     * Load a mouse cursor.
     * 
     * @param filename the name of the file that stores the image of this cursor
     * @param hotspotX the x coordinate of the cursor hotspot
     * @param hotspotY the y coordinate of the cursor hotspot
     * @return the loaded mouse cursor
     * @throws IllegalArgumentException in case all loaders fail to load this
     *             cursor
     */
    public SlickMouseCursor loadCursor(final String filename,
        final int hotspotX, final int hotspotY) {
        final Iterator<SlickMouseCursorLoader> itr = getLoaderIterator();

        while (itr.hasNext()) {
            try {
                return itr.next().loadCursor(filename, hotspotX, hotspotY);
            } catch (final SlickLoadCursorException e) {
                // this loader failed... does not matter
            }
        }

        throw new IllegalArgumentException("Failed to load cursor \""
            + filename + "\".");
    }
}
