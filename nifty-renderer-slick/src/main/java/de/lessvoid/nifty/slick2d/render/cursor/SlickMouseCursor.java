package de.lessvoid.nifty.slick2d.render.cursor;

import org.newdawn.slick.Graphics;

import de.lessvoid.nifty.spi.render.MouseCursor;

/**
 * The extended mouse cursor interface that adds the functions needed to
 * handle the mouse cursor by the Slick render device.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickMouseCursor extends MouseCursor {
    /**
     * Start showing this mouse cursor.
     */
    void enableCursor();
    
    /**
     * Stop showing this mouse cursor
     */
    void disableCursor();
    
    /**
     * Render the mouse cursor on the screen. This method is called after
     * every render loop, how ever in case the mouse cursor uses a native
     * implementation, its not needed to do anything in this function.
     * 
     * @param g the graphics object that should be used to render
     * @param x the x coordinate of the mouse
     * @param y the y coordinate of the mouse
     */
    void render(Graphics g, int x, int y);
}
