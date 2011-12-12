package de.lessvoid.nifty.slick2d.render.font;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

/**
 * This is the render font implementation that uses an true type font to render
 * the text.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated This font uses the {@link org.newdawn.slick.TrueTypeFont} that is
 *             marked as deprecated
 */
@Deprecated
public class TrueTypeSlickRenderFont extends AbstractJavaSlickRenderFont {
  /**
   * The constructor to create this true type based render font.
   * 
   * @param ttFont
   *          the true type font that is used to render
   * @param javaFont
   *          the java font that is used to render just the same font
   * @throws SlickLoadFontException
   *           in case loading the font fails
   */
  public TrueTypeSlickRenderFont(final TrueTypeFont ttFont, final Font javaFont) throws SlickLoadFontException {
    super(ttFont, javaFont);
  }
}
