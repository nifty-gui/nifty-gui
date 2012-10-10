package de.lessvoid.nifty.slick2d.render.font.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.lessvoid.nifty.slick2d.render.font.AngelCodeSlickRenderFont;
import de.lessvoid.nifty.slick2d.render.font.SlickLoadFontException;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The loader is able to load render fonts that are based on angel code fonts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AngelCodeSlickRenderFontLoader implements SlickRenderFontLoader {
  /**
   * Load the requested font.
   */
  @Override
  public SlickRenderFont loadFont(final Graphics g, final String filename) throws SlickLoadFontException {
    String definition;
    String image;
    if (filename.endsWith("fnt")) {
      definition = filename;

      /* Slick is unable to extract the image files from the font definition. */
      BufferedReader in = null;
      try {
    	String imageFilepath;
        in = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream(definition)));
        in.readLine();
        in.readLine();
        image = in.readLine();
        final int indexOfFileEntry = image.indexOf("file=\"") + 6;
        image = image.substring(indexOfFileEntry, image.length() - 1);
        final int indexOfPathEnd = filename.lastIndexOf("/");
        imageFilepath = filename.substring(0,indexOfPathEnd + 1);
        image = imageFilepath + image;
      } catch (IOException e) {
        throw new SlickLoadFontException("Loading font failed.", e);
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            // nothing
          }
        }
      }
    } else {
      image = filename;
      definition = filename.substring(0, filename.lastIndexOf('.') + 1) + "fnt";
    }

    try {
      return new AngelCodeSlickRenderFont(new AngelCodeFont(definition, image));
    } catch (final SlickLoadFontException e) {
      throw new SlickLoadFontException("Loading font failed.", e);
    } catch (final NullPointerException e) {
      // Seems Slick throws around NullPointerExceptions when something goes wrong. How nasty!
      throw new SlickLoadFontException("Loading font failed.", e);
    } catch (final SlickException e) {
      throw new SlickLoadFontException("Loading font failed.", e);
    }
  }

}
