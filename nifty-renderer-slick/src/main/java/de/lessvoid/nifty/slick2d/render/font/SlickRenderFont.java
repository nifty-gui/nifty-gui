package de.lessvoid.nifty.slick2d.render.font;

import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import org.newdawn.slick.Graphics;

/**
 * This interface extends the normal Nifty {@link RenderFont} to make it usable with the render device of this Slick
 * renderer.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickRenderFont extends RenderFont {
  /**
   * Render a text using this font.
   *
   * @param g the slick graphics object that is used to render
   * @param text the text that is supposed to be rendered
   * @param locX the x coordinate of the position on the screen where the text is supposed to be rendered
   * @param locY the y coordinate of the position on the screen where the text is supposed to be rendered
   * @param color the color of the text
   * @param sizeX the horizontal scaling factor of the text
   * @param sizeY the vertical scaling factor of the text
   * @throws IllegalArgumentException in case the parameter g is {@code null} or in case the parameter text is {@code
   * null}
   */
  void renderText(Graphics g, String text, int locX, int locY, Color color, float sizeX, float sizeY);
}
