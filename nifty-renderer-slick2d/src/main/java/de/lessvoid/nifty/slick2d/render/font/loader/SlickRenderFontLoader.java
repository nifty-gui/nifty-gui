package de.lessvoid.nifty.slick2d.render.font.loader;

import de.lessvoid.nifty.slick2d.loaders.SlickLoader;
import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import org.newdawn.slick.Graphics;

import javax.annotation.Nonnull;

/**
 * This interface defines a font loader that loads a font to be used by Nifty.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickRenderFontLoader extends SlickLoader {
  /**
   * Load a render font that is identified by a String.
   *
   * @param g        the graphics instance used for all render operations
   * @param filename the filename to load
   * @return the loaded font
   * @throws SlickLoadFontException in case loading the font fails
   */
  @Nonnull
  SlickRenderFont loadFont(Graphics g, String filename) throws SlickLoadFontException;
}
