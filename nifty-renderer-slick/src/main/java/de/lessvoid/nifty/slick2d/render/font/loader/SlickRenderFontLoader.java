package de.lessvoid.nifty.slick2d.render.font.loader;

import de.lessvoid.nifty.slick2d.render.font.LoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;

/**
 * This interface defines a font loader that loads a font to be used by Nifty.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickRenderFontLoader {
    /**
     * Load a render font that is identified by a String.
     * 
     * @param font the font identifier
     * @return the loaded font
     * @throws LoadFontException in case loading the font fails
     */
    SlickRenderFont loadFont(String font) throws LoadFontException;
}
