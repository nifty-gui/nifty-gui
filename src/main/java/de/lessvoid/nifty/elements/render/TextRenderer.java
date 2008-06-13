package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.render.RenderFontNull;
import de.lessvoid.nifty.render.helper.FontHelper;
import de.lessvoid.nifty.tools.Color;

/**
 * The TextRenderer implementation.
 * @author void
 */
public class TextRenderer implements ElementRenderer {

  /**
   * the font to use.
   */
  private RenderFont font = new RenderFontNull();

  /**
   * the text to output.
   */
  private String[] textLines = new String[0];

  /**
   * max width of all text strings.
   */
  private int maxWidth;

  /**
   * the font color.
   */
  private Color color;

  /**
   * can't remember what this is :>.
   */
  private int xoffsetHack = 0;

  /**
   * selection start.
   */
  private int selectionStart = -1;

  /**
   * selection end.
   */
  private int selectionEnd = -1;

  /**
   * text selection corlor.
   */
  private Color textSelectionColor;

  /**
   * default constructor.
   */
  public TextRenderer() {
  }

  /**
   * set Text.
   * @param newText text
   */
  public void setText(final String newText) {
    initText(newText);
  }

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
   * @param newText new text
   */
  private void initText(final String newText) {
    this.textLines = newText.split("\n", -1);

    maxWidth = 0;
    for (String line : textLines) {
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
  public final void render(final Element w, final RenderEngine r) {
    int x = 0;
    int y = 0;
    for (String line : textLines) {
      if (Math.abs(xoffsetHack) > 0) {
        int fittingOffset = FontHelper.getVisibleCharactersFromStart(font, line, Math.abs(xoffsetHack), 1.0f);
        String cut = line.substring(0, fittingOffset);
        String substring = line.substring(fittingOffset, line.length());
        // font.setSelection(selectionStart - fittingOffset, selectionEnd - fittingOffset);
        r.renderText(
            font,
            substring,
            w.getX() + x + xoffsetHack + font.getWidth(cut),
            w.getY() + y,
            selectionStart - fittingOffset,
            selectionEnd - fittingOffset,
            textSelectionColor);
      } else {
        int xx = w.getX() + x + xoffsetHack;
        xx = w.getX() + (w.getWidth() - font.getWidth(line)) / 2;
        r.renderText(
            font,
            line,
            xx,
            w.getY() + y,
            selectionStart,
            selectionEnd,
            textSelectionColor);
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
    return font.getHeight() * textLines.length;
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

  /**
   * Get RenderFont.
   * @return render font
   */
  public RenderFont getFont() {
    return font;
  }

  /**
   * set a new selection.
   * @param selectionStartParam start
   * @param selectionEndParam end
   */
  public void setSelection(final int selectionStartParam, final int selectionEndParam) {
    this.selectionStart = selectionStartParam;
    this.selectionEnd = selectionEndParam;
  }

  /**
   * set color.
   * @param colorParam color
   */
  public void setColor(final Color colorParam) {
    this.color = colorParam;
  }

  /**
   * set font.
   * @param fontParam font
   */
  public void setFont(final RenderFont fontParam) {
    this.font = fontParam;
  }

  /**
   * set new text selection color.
   * @param textSelectionColorParam text selection color
   */
  public void setTextSelectionColor(final Color textSelectionColorParam) {
    this.textSelectionColor = textSelectionColorParam;
  }
}
