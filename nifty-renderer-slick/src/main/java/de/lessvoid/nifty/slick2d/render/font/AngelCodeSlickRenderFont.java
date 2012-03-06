package de.lessvoid.nifty.slick2d.render.font;

import org.newdawn.slick.AngelCodeFont;

/**
 * This is the render font implementation that uses an angel code font to render the text.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AngelCodeSlickRenderFont extends AbstractSlickRenderFont {
  /**
   * Create the render font using a already loaded angel code font.
   *
   * @param font the font this render font is supposed to encapsulate
   * @throws SlickLoadFontException in case loading the font fails
   */
  @SuppressWarnings("TypeMayBeWeakened")
  public AngelCodeSlickRenderFont(final AngelCodeFont font) throws SlickLoadFontException {
    super(font);
  }
}
