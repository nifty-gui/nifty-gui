package de.lessvoid.nifty.slick2d.render.font;

import java.awt.Font;

import org.newdawn.slick.UnicodeFont;

/**
 * This render font implementation uses a unicode font to draw the text on the
 * screen.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UnicodeSlickRenderFont extends AbstractJavaSlickRenderFont {
    /**
     * The constructor to create this unicode font based render font.
     * 
     * @param ucFont the unicode font that is used to render
     * @param javaFont the java font that is used to render just the same font
     * @throws LoadFontException in case loading the font fails
     */
    public UnicodeSlickRenderFont(final UnicodeFont ucFont,
        final Font javaFont) throws LoadFontException {
        super(ucFont, javaFont);
    }
}
