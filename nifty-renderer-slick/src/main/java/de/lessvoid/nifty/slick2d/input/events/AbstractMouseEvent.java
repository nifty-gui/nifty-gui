package de.lessvoid.nifty.slick2d.input.events;

/**
 * This is the abstract mouse event that stores only the data all mouse events
 * have in common.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractMouseEvent implements InputEvent {
    /**
     * The X coordinate of the location where the mouse event occurred.
     */
    private final int locX;

    /**
     * The Y coordinate of the location where the mouse event occurred.
     */
    private final int locY;

    /**
     * Create a instance of this class and define the x and the y coordinate of
     * the location where the event happened.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    protected AbstractMouseEvent(final int x, final int y) {
        locX = x;
        locY = y;
    }

    /**
     * Get the X coordinate of the location where the mouse event happened.
     * 
     * @return the x coordinate of the event location
     */
    protected final int getX() {
        return locX;
    }

    /**
     * Get the Y coordinate of the location where the mouse event happened.
     * 
     * @return the y coordinate of the event location
     */
    protected final int getY() {
        return locY;
    }
}