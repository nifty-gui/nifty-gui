package de.lessvoid.nifty.elements.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.elements.tools.TextBreak;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

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
   * this is the original text.
   */
  private String originalText;

  /**
   * the text to output.
   */
  private String[] textLines = new String[0];

  /**
   * max width of all text strings.
   */
  private int maxWidth;

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
   * color.
   */
  private Color color = Color.WHITE;

  /**
   * This TextRenderer will automatically wrap lines when the element it is
   * attached to has a width constraint.
   */
  private boolean lineWrapping = false;
  private boolean isCalculatedLineWrapping = false;

  /**
   * default constructor.
   */
  public TextRenderer() {
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
   * set Text.
   * @param newText text
   */
  public void setText(final String newText) {
    if (newText == null) {
      return;
    }
    initText(newText);
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
    this.originalText = newText;
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
  public void render(final Element w, final NiftyRenderEngine r) {
    if (textLines.length == 0 || (textLines.length == 1 && textLines[0].length() == 0)) {
      return;
    }
    renderLines(w, r, textLines);
  }

  private void renderLines(final Element w, final NiftyRenderEngine r, String[] lines) {
    RenderFont font = ensureFont(r);
    int y = getStartYWithVerticalAlign(lines.length * font.getHeight(), w.getHeight(), textVAlign);
    for (String line : lines) {
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

  private RenderFont ensureFont(final NiftyRenderEngine r) {
    RenderFont font = r.getFont();
    if (font == null) {
      font = this.font;
    }
    return font;
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

  /**
   * render line.
   * @param xx x
   * @param yy y
   * @param line line
   * @param r RenderEngine
   * @param selStart sel start
   * @param selEnd sel end
   */
  private void renderLine(
      final int xx,
      final int yy,
      final String line,
      final NiftyRenderEngine r,
      final int selStart,
      final int selEnd) {
    if (!r.isColorChanged()) {
      if (r.isColorAlphaChanged()) {
        r.setColorIgnoreAlpha(color);
      } else {
        r.setColor(color);
      }
    }
    boolean stateSaved = false;
    if (r.getFont() == null) {
      r.saveState(RenderStateType.allStates());
      r.setFont(font);
      stateSaved = true;
    }
    r.renderText(
        line,
        xx,
        yy,
        selStart,
        selEnd,
        textSelectionColor);
    if (stateSaved) {
      r.restoreState();
    }
  }

  /**
   * Helper method to get width of text.
   * @return the width in pixel of the current set text.
   */
  public int getTextWidth() {
    return maxWidth;
  }

  /**
   * Helper method to get height of text.
   * @return the height in pixel of the current set text.
   */
  public int getTextHeight() {
    return font.getHeight() * textLines.length;
  }

  /**
   * Change the text.
   * @param newText the new text
   */
  public void changeText(final String newText) {
    initText(newText);
  }

  /**
   * set thing.
   * @param newXoffsetHack xoffset
   */
  public void setXoffsetHack(final int newXoffsetHack) {
    this.xoffsetHack = newXoffsetHack;
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
   * set font.
   * @param fontParam font
   */
  public void setFont(final RenderFont fontParam) {
    if (fontParam == null) {
      return;
    }
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

  /**
   * set color.
   * @param newColor color
   */
  public void setColor(final Color newColor) {
    if (newColor != null) {
      this.color = newColor;
    }
  }

  /**
   * get original text.
   * @return original text
   */
  public String getOriginalText() {
    return originalText;
  }

  /**
   * RenderFont Null Object.
   * @author void
   */
  public class RenderFontNull implements RenderFont {
    public int getHeight() {
      return 0;
    }
    public int getWidth(final String text) {
      return 0;
    }
    public void render(final String text, final int x, final int y, final Color fontColor, final float size) {
    }
    public Integer getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
      return null;
    }
  }

  private String[] bla(final int width, final NiftyRenderEngine r, final String[] textLines) {
    RenderFont font = ensureFont(r);
    List < String > lines = new ArrayList < String > ();
    for (String line : textLines) {
      int lineLengthInPixel = font.getWidth(line);
      if (lineLengthInPixel > width) {
        lines.addAll(new TextBreak(line, width, font).split());
      } else {
        lines.add(line);
      }
    }
    return lines.toArray(new String[0]);
  }

  public void setWidthConstraint(final Element element, final SizeValue elementConstraintWidth, final int parentWidth, final NiftyRenderEngine renderEngine) {
    if (elementConstraintWidth == null || parentWidth == 0 || !lineWrapping || isCalculatedLineWrapping) {
      return;
    }
    int valueAsInt = elementConstraintWidth.getValueAsInt(parentWidth);
    this.textLines = bla(valueAsInt, renderEngine, originalText.split("\n", -1));
    maxWidth = 0;
    for (String line : textLines) {
      int lineWidth = font.getWidth(line);
      if (lineWidth > maxWidth) {
        maxWidth = lineWidth;
      }
    }
    element.setConstraintWidth(new SizeValue(getTextWidth() + "px"));
    element.setConstraintHeight(new SizeValue(getTextHeight() + "px"));
    isCalculatedLineWrapping = true;
  }

  public void setLineWrapping(final boolean lineWrapping) {
    this.lineWrapping = lineWrapping; 
  }
}
