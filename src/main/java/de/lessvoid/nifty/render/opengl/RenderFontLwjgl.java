package de.lessvoid.nifty.render.opengl;

import de.lessvoid.font.Font;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderFontLwjgl.
 * @author void
 */
public class RenderFontLwjgl implements RenderFont {

  /**
   * font.
   */
  private Font font;

  /**
   * Initialize the font.
   * @param name font filename
   */
  public RenderFontLwjgl(final String name) {
    font = new Font();
    font.init(name);
  }

  /**
   * render the text.
   * @param text text
   * @param x x
   * @param y y
   * @param color color
   * @param fontSize size
   */
  public void render(final String text, final int x, final int y, final Color color, final float fontSize) {
    RenderTools.beginRender();
    if (color == null) {
      font.drawStringWithSize(x, y, text, fontSize);
    } else {
      font.renderWithSizeAndColor(
          x, y, text, fontSize, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    RenderTools.endRender();
  }

  public int getHeight() {
    return font.getHeight();
  }

  public int getWidth( String text ) {
    return font.getStringWidth( text );
  }

  public int getVisibleCharactersFromStart(final String text, final int width) {
    return font.getLengthFittingPixelSize(text, width, 1.0f);
  }

  public int getVisibleCharactersFromEnd(final String text, final int width) {
    return font.getLengthFittingPixelSizeBackwards(text, width, 1.0f);
  }

  public int getCharacterIndexFromPixelPosition(final String text, final int pixel, final float size) {
    return font.getIndexFromPixel(text, pixel, size);
  }
}
