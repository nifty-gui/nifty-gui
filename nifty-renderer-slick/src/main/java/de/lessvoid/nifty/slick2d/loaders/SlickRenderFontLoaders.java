package de.lessvoid.nifty.slick2d.loaders;

import java.util.Iterator;

import org.newdawn.slick.Graphics;

import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.loader.AngelCodeSlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.DefaultSlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.SlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.TrueTypeSlickRenderFontLoader;
import de.lessvoid.nifty.slick2d.render.font.loader.UnicodeSlickRenderFontLoader;

/**
 * This class is used to trigger the actual font loading. It will query all the
 * font loaders and try to load a font with the specified name.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickRenderFontLoaders extends AbstractSlickLoaders<SlickRenderFontLoader> {
  /**
   * The singleton instance of this class.
   */
  private static final SlickRenderFontLoaders INSTANCE = new SlickRenderFontLoaders();

  /**
   * Get the singleton instance of this class.
   * 
   * @return the singleton instance
   */
  public static SlickRenderFontLoaders getInstance() {
    return INSTANCE;
  }

  /**
   * Private constructor so no instances but the singleton instance are created.
   */
  private SlickRenderFontLoaders() {
    super();
  }

  /**
   * Add the default implemented loaders to the loader list. This is done
   * automatically in case fonts are requested but no loaders got registered. In
   * general using this function should be avoided. Its better to load only the
   * loaders that are actually needed for your resources.
   * 
   * @param order
   *          the place where the default loaders are added to the list
   */
  @Override
  public void loadDefaultLoaders(final SlickAddLoaderLocation order) {
    switch (order) {
    case first:
      addLoader(new DefaultSlickRenderFontLoader(), order);
      addLoader(new AngelCodeSlickRenderFontLoader(), order);
      addLoader(new UnicodeSlickRenderFontLoader(), order);
      addLoader(new TrueTypeSlickRenderFontLoader(), order);
      break;
    case last:
    case dontCare:
      addLoader(new TrueTypeSlickRenderFontLoader(), order);
      addLoader(new UnicodeSlickRenderFontLoader(), order);
      addLoader(new AngelCodeSlickRenderFontLoader(), order);
      addLoader(new DefaultSlickRenderFontLoader(), order);
      break;
    }
  }

  /**
   * Load the font with the defined name.
   * 
   * @param g
   *          the graphics instance used for rendering
   * @param filename
   *          name of the file that contains the font
   * @return the font loaded
   * @throws IllegalArgumentException
   *           in case all loaders fail to load the font
   */
  public SlickRenderFont loadFont(final Graphics g, final String filename) {
    final Iterator<SlickRenderFontLoader> itr = getLoaderIterator();

    while (itr.hasNext()) {
      try {
        return itr.next().loadFont(g, filename);
      } catch (final SlickLoadFontException e) {
        // this loader failed... does not matter
      }
    }

    throw new IllegalArgumentException("Failed to load font \"" + filename + "\".");
  }
}
