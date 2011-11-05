package de.lessvoid.nifty.slick2d.render.font.loader;

import org.newdawn.slick.AngelCodeFont;

import de.lessvoid.nifty.slick2d.render.font.AngelCodeSlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.LoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;

/**
 * The loader is able to load render fonts that are based on angel code fonts.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AngelCodeSlickRenderFontLoader implements
    SlickRenderFontLoader {
    /**
     * Load the requested font.
     */
    @Override
    public SlickRenderFont loadFont(final String font)
        throws LoadFontException {
        final String image = font;
        final String definition =
            font.substring(0, font.lastIndexOf('.') + 1) + "fnt";

        try {
            return new AngelCodeSlickRenderFont(new AngelCodeFont(image,
                definition));
        } catch (final Exception e) {
            throw new LoadFontException("Loading font failed.", e);
        }
    }

}
