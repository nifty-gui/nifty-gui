package de.lessvoid.nifty.render.opengl;

import de.lessvoid.font.Font;
import de.lessvoid.nifty.render.RenderFont;

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
   * font size.
   */
  private float fontSize = 1.0f;

  /**
   * use default or custom color.
   */
  private boolean useDefaultColor = true;

  /**
   * font color red.
   */
  private float fontColorR = 1.0f;

  /**
   * font color green.
   */
  private float fontColorG = 1.0f;

  /**
   * font color blue.
   */
  private float fontColorB = 1.0f;

  /**
   * font color alpha.
   */
  private float fontColorA = 1.0f;

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
   */
  public void render(final String text, final int x, final int y) {
    if (useDefaultColor) {
      font.drawStringWithSize(x, y, text, fontSize);
    } else {
      font.renderWithSizeAndColor(x, y, text, fontSize, fontColorR, fontColorG, fontColorB, fontColorA);
    }
  }

  /**
   * set font size.
   * @param size size
   */
  public void setSize(final float size) {
    fontSize = size;
  }

  /**
   * set font color.
   * @param r red
   * @param g green
   * @param b blue
   * @param a alpha
   */
  public void setColor(final float r, final float g, final float b, final float a) {
    fontColorR = r;
    fontColorG = g;
    fontColorB = b;
    fontColorA = a;
    useDefaultColor = false;
  }

  /**
   * set default color.
   */
  public void setDefaultColor() {
    useDefaultColor = true;
  }

  public int getHeight() {
    return font.getHeight();
  }

  public int getWidth( String text ) {
    return font.getStringWidth( text );
  }

  public int getFittingOffset(final String text, final int width) {
    return font.getLengthFittingPixelSize(text, width, 1.0f);
  }

  public int getFittingOffsetBackward(final String text, final int width) {
    return font.getLengthFittingPixelSizeBackwards(text, width, 1.0f);
  }

  public int getIndexFromPixel(final String text, final int pixel, final float size) {
    return font.getIndexFromPixel(text, pixel, size);
  }

  /**
   * set selection.
   * @param selectionStart selection start
   * @param selectionEnd selection end
   */
  public void setSelection(final int selectionStart, final int selectionEnd) {
    font.setSelectionStart(selectionStart);
    font.setSelectionEnd(selectionEnd);
  }
}
