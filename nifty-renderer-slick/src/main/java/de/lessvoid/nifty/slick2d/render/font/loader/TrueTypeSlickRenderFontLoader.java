package de.lessvoid.nifty.slick2d.render.font.loader;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.TrueTypeSlickRenderFont;

/**
 * This loader is able to load fonts that base on TrueType fonts.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class TrueTypeSlickRenderFontLoader extends
    AbstractJavaSlickRenderFontLoader {
    /**
     * Load the requested font.
     */
    @Override
    public SlickRenderFont loadFont(final String filename)
        throws SlickLoadFontException {

        final Font javaFont = loadJavaFont(filename);
        if (javaFont == null) {
            throw new SlickLoadFontException("Can't load font as true type.");
        }

        return new TrueTypeSlickRenderFont(new TrueTypeFont(javaFont, true),
            javaFont);
    }

}