package de.lessvoid.nifty.slick2d.render.cursor;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

/**
 * This implementation of the slick mouse cursor uses the LWJGL cursor
 * implementation to display the mouse cursor.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class LwjglCursorSlickMouseCursor extends
    AbstractNativeSlickMouseCursor {
    /**
     * The cursor that is displayed.
     */
    private final Cursor cursor;

    /**
     * Create a new slick mouse cursor that wraps a LWJGL cursor.
     * 
     * @param lwjglCursor the lwjgl cursor
     */
    public LwjglCursorSlickMouseCursor(final Cursor lwjglCursor) {
        cursor = lwjglCursor;
    }

    /**
     * Dispose the cursor.
     */
    @Override
    public void dispose() {
        cursor.destroy();
    }

    /**
     * Start displaying the cursor.
     */
    @Override
    public void enableCursor() {
        try {
            Mouse.setNativeCursor(cursor);
        } catch (final LWJGLException e) {
            // enabling failed
        }
    }

    /**
     * Switch back to the native default cursor.
     */
    @Override
    public void disableCursor() {
        try {
            Mouse.setNativeCursor(null);
        } catch (final LWJGLException e) {
            // hiding failed
        }
    }
}
