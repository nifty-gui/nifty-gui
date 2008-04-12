package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * The TextRenderer implementation.
 * @author void
 */
public class TextRenderer implements ElementRenderer {

  /**
   * the font to use.
   */
  private RenderFont font;

  /**
   * the text to output.
   */
  private String[] text;

  /**
   * max width of all text strings.
   */
  private int maxWidth;

  /**
   * the font color.
   */
  private Color color;
  
  private int xoffsetHack = 0;

  private int selectionStart;

  private int selectionEnd;

  /**
   * create new renderer with the given font and text.
   * @param newFont the font to use
   * @param newText the text to use
   */
  public TextRenderer(final RenderFont newFont, final String newText) {
    init(newFont, newText);
  }

  /**
   * create new renderer with the given font, text and color.
   * @param newFont the font to use
   * @param newText the text to use
   * @param newColor the fontColor to use
   */
  public TextRenderer(final RenderFont newFont, final String newText, final Color newColor) {
    this.color = newColor;
    init(newFont, newText);
  }

  /**
   * initialize.
   * @param newFont new font
   * @param newText new text
   */
  private void init(final RenderFont newFont, final String newText) {
    this.font = newFont;
    initText(newText);
  }

  /**
   * @param newText
   */
  private void initText(final String newText) {
    this.text = newText.split("\n", -1);

    maxWidth = 0;
    for (String line : text) {
      int lineWidth = font.getWidth(line);
      if (lineWidth > maxWidth) {
        maxWidth = lineWidth;
      }
    }
  }

  /**
   * render the stuff.
   * @param w the widget we're connected to
   * @param r the renderDevice we should use
   */
  public final void render(final Element w, final RenderDevice r) {

    if (color != null) {
      r.setFontColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    int x = 0;
    int y = 0;
    for (String line : text) {
      if (Math.abs(xoffsetHack) > 0) {
        int fittingOffset = font.getFittingOffset(line, Math.abs(xoffsetHack));
        String cut = line.substring(0, fittingOffset);
        String substring = line.substring(fittingOffset, line.length());
        font.setSelection(selectionStart - fittingOffset, selectionEnd - fittingOffset);
        r.renderText(
            font,
            substring,
            (int) (r.getMoveToX() + w.getX() + x + xoffsetHack + font.getWidth(cut)),
            (int) (r.getMoveToY() + w.getY()) + y);
      } else {
        font.setSelection(selectionStart, selectionEnd);
        r.renderText(font, line, (int) (r.getMoveToX() + w.getX() + x + xoffsetHack), (int) (r.getMoveToY() + w.getY()) + y);
      }
      y += font.getHeight();
    }
  }

  /**
   * Helper method to get width of text.
   * @return the width in pixel of the current set text.
   */
  public final int getTextWidth() {
    return maxWidth;
  }

  /**
   * Helper method to get height of text.
   * @return the height in pixel of the current set text.
   */
  public final int getTextHeight() {
    return font.getHeight() * text.length;
  }

  /**
   * Change the text.
   * @param newText the new text
   */
  public final void changeText(final String newText) {
    initText(newText);
  }

  public void setXoffsetHack(int xoffsetHack) {
    this.xoffsetHack = xoffsetHack;
  }

  public RenderFont getFont() {
    return font;
  }

  public void setSelection(int selectionStart, int selectionEnd) {
    this.selectionStart = selectionStart;
    this.selectionEnd = selectionEnd;
  }
}
