package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
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
   * vertical alignment.
   */
  private VerticalAlign textVAlign = VerticalAlign.center;

  /**
   * horizontal alignment.
   */
  private HorizontalAlign textHAlign = HorizontalAlign.center;

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
    int y = getStartYWithVerticalAlign(textLines.length * font.getHeight(), w.getHeight(), textVAlign);
    for (String line : textLines) {
      int yy = w.getY() + y;
      if (Math.abs(xoffsetHack) > 0) {
        int fittingOffset = FontHelper.getVisibleCharactersFromStart(font, line, Math.abs(xoffsetHack), 1.0f);
        String cut = line.substring(0, fittingOffset);
        String substring = line.substring(fittingOffset, line.length());
        int xx = w.getX() + xoffsetHack + font.getWidth(cut);
        renderLine(xx, yy, substring, r, selectionStart - fittingOffset, selectionEnd - fittingOffset);
      } else {
        int xx = w.getX() + getStartXWithHorizontalAlign(font.getWidth(line), w.getWidth(), textHAlign);
        renderLine(xx, yy, line, r, selectionStart, selectionEnd);
      }
      y += font.getHeight();
    }
  }

  /**
   * Get start Y for text rendering given the textHeight and the elementHeight.
   * @param textHeight text height
   * @param elementHeight element height
   * @param verticalAlign verticalAlign
   * @return start y for text rendering
   */
  protected static int getStartYWithVerticalAlign(
      final int textHeight,
      final int elementHeight,
      final VerticalAlign verticalAlign) {
    if (VerticalAlign.top == verticalAlign) {
      return 0;
    } else if (VerticalAlign.center == verticalAlign) {
      return (elementHeight - textHeight) / 2;
    } else if (VerticalAlign.bottom == verticalAlign) {
      return elementHeight - textHeight;
    } else {
      return 0;
    }
  }

  /**
   * Get start x for text rendering given the textWidth and the elementWidth.
   * @param textWidth text width
   * @param elementWidth element width
   * @param horizontalAlign horizontalAlign
   * @return start x for text rendering
   */
  protected static int getStartXWithHorizontalAlign(
      final int textWidth,
      final int elementWidth,
      final HorizontalAlign horizontalAlign) {
    if (HorizontalAlign.left == horizontalAlign) {
      return 0;
    } else if (HorizontalAlign.center == horizontalAlign) {
      return (elementWidth - textWidth) / 2;
    } else if (HorizontalAlign.right == horizontalAlign) {
      return elementWidth - textWidth;
    } else {
      return 0;
    }
  }

  private void renderLine(int xx, int yy, String line, final RenderEngine r, int selStart, int selEnd) {
    r.renderText(
        font,
        line,
        xx,
        yy,
        selStart,
        selEnd,
        textSelectionColor);
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

  /**
   * set text vertical alignment.
   * @param newTextVAlign text vertical alignment
   */
  public void setTextVAlign(final VerticalAlign newTextVAlign) {
    this.textVAlign = newTextVAlign;
  }

  /**
   * set text horizontal alignment.
   * @param newTextHAlign text horizontal alignment
   */
  public void setTextHAlign(final HorizontalAlign newTextHAlign) {
    this.textHAlign = newTextHAlign;
  }
}
