package de.lessvoid.nifty.slick2d.render.cursor;

import de.lessvoid.nifty.spi.render.MouseCursor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * The extended mouse cursor interface that adds the functions needed to handle the mouse cursor by the Slick render
 * device.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickMouseCursor extends MouseCursor {
  /**
   * Stop showing this mouse cursor
   *
   * @param container the game container that is supposed to display the default cursor now
   */
  void disableCursor(GameContainer container);

  /**
   * Start showing this mouse cursor.
   *
   * @param container the game container that is now supposed to show this cursor
   */
  void enableCursor(GameContainer container);

  /**
   * Render the mouse cursor on the screen. This method is called after every render loop, how ever in case the mouse
   * cursor uses a native implementation, its not needed to do anything in this function.
   *
   * @param g the graphics object that should be used to render
   * @param x the x coordinate of the mouse
   * @param y the y coordinate of the mouse
   */
  void render(Graphics g, int x, int y);
}
