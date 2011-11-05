package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.HieroSettings;

import de.lessvoid.nifty.slick2d.render.font.LoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.UnicodeSlickRenderFont;

/**
 * Load the font as Unicode font using Hiero settings to specify how the font is
 * supposed to look like.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UnicodeSlickRenderFontLoader extends
    AbstractJavaSlickRenderFontLoader {
    /**
     * Load the font. The name of the font will be used as name of the Hiero
     * settings file. The true type font file will be load by trying to add
     * ".ttf" to the filename and by replacing the file ending with "ttf".
     */
    @Override
    public SlickRenderFont loadFont(final String font)
        throws LoadFontException {
        try {
            final HieroSettings hieroSettings = new HieroSettings(font);
            Font javaFont = loadJavaFont(font + ".ttf");
            if (javaFont == null) {
                javaFont =
                    loadJavaFont(font.substring(0, font.lastIndexOf("."))
                        + "ttf");

                if (javaFont == null) {
                    throw new LoadFontException("Loading TTF Font failed.");
                }
            }
            return new UnicodeSlickRenderFont(new UnicodeFont(javaFont,
                hieroSettings), javaFont);
        } catch (final SlickException e) {
            throw new LoadFontException("Loading the font failed.", e);
        }
    }
}
