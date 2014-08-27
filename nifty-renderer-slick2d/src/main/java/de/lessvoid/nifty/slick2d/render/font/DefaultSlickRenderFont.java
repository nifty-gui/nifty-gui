package de.lessvoid.nifty.slick2d.render.font;

import org.newdawn.slick.Font;

import javax.annotation.Nonnull;

/**
 * This is the render font implementation that uses an unknown font implementation.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DefaultSlickRenderFont extends AbstractSlickRenderFont {
  /**
   * Create the render font using a already loaded font.
   *
   * @param font the font this render font is supposed to encapsulate
   * @throws SlickLoadFontException in case loading the font fails
   */
  public DefaultSlickRenderFont(@Nonnull final Font font) throws SlickLoadFontException {
    super(font);
  }
}
