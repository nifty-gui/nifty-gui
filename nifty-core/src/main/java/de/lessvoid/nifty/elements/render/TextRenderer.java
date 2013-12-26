package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.elements.tools.TextBreak;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The TextRenderer implementation.
 *
 * @author void
 */
public class TextRenderer implements ElementRenderer {
  /**
   * The default color used by the renderer.
   */
  @Nonnull
  public static final Color DEFAULT_COLOR = Color.WHITE;

  /**
   * the font to use.
   */
  @Nullable
  private RenderFont font;

  /**
   * this is the original text.
   */
  @Nonnull
  private String originalText;

  /**
   * the text to output.
   */
  @Nullable
  private String[] textLines;

  /**
   * max width of all text strings.
   */
  private int maxWidth;

  /**
   * can't remember what this is :>.
   */
  private int xOffsetHack = 0;

  /**
   * selection start.
   */
  private int selectionStart = -1;

  /**
   * selection end.
   */
  private int selectionEnd = -1;

  /**
   * text selection color.
   */
  @Nonnull
  private Color textSelectionColor = Color.BLACK;

  /**
   * vertical alignment.
   */
  @Nonnull
  private VerticalAlign textVAlign = VerticalAlign.center;

  /**
   * horizontal alignment.
   */
  @Nonnull
  private HorizontalAlign textHAlign = HorizontalAlign.center;

  /**
   * color.
   */
  @Nonnull
  private Color color = DEFAULT_COLOR;

  /**
   * This TextRenderer will automatically wrap lines when the element it is
   * attached to has a width constraint.
   */
  private boolean lineWrapping = false;

  /**
   * If the textLineHeight property is set it will override the font.getHeight() when
   * calculating the height of the text.
   */
  @Nonnull
  private SizeValue textLineHeight = SizeValue.def();

  /**
   * If the textMinLineHeight property is set the text will always be at least textMinLineHeight
   * pixel height.
   */
  @Nonnull
  private SizeValue textMinHeight = SizeValue.def();

  @Nonnull
  private final Nifty nifty;

  // in case we use word wrapping and are changing the elements width/height constraints we'll
  // remember the original values in here
  private boolean isCalculatedLineWrapping = false;
  @Nonnull
  private SizeValue originalConstraintWidth = SizeValue.def();
  @Nonnull
  private SizeValue originalConstraintHeight = SizeValue.def();

  /*
   * When the element this TextRenderer belongs to as been layouted at least once we remember the attached element
   * here so that we can later automatically relayout ourself correctly when someone changed this text.
   */
  @Nullable
  private Element hasBeenLayoutedElement;

  /**
   * default constructor.
   */
  public TextRenderer(@Nonnull final Nifty nifty) {
    this.nifty = nifty;
    originalText = "";
  }

  /**
   * create new renderer with the given font and text.
   *
   * @param newFont the font to use
   * @param newText the text to use
   */
  public TextRenderer(@Nonnull final Nifty nifty, @Nonnull final RenderFont newFont, @Nullable final String newText) {
    this.nifty = nifty;
    init(newFont, newText);
  }

  /**
   * set Text.
   *
   * @param newText text
   */
  public void setText(@Nullable final String newText) {
    initText(newText, true);
  }

  /**
   * initialize.
   *
   * @param newFont new font
   * @param newText new text
   */
  private void init(@Nonnull final RenderFont newFont, @Nullable final String newText) {
    this.font = newFont;
    initText(newText, false);
  }

  /**
   * @param text the text that is supposed to be used
   */
  private void initText(@Nullable final String text, final boolean changeExistingText) {
    String newText = nifty.specialValuesReplace(text);
    if (lineWrapping && isCalculatedLineWrapping) {
      isCalculatedLineWrapping = false;
    }

    this.originalText = newText;
    this.textLines = newText.split("\n", -1);
    if (changeExistingText && hasBeenLayoutedElement != null) {
      hasBeenLayoutedElement.getParent().layoutElements();
    }

    maxWidth = 0;
    if (font != null) {
      for (int i = 0; i < textLines.length; i++) {
        String line = textLines[i];
        int lineWidth = font.getWidth(line);
        if (lineWidth > maxWidth) {
          maxWidth = lineWidth;
        }
      }
    }
  }

  /**
   * render the stuff.
   *
   * @param w the widget we're connected to
   * @param r the renderDevice we should use
   */
  @Override
  public void render(@Nonnull final Element w, @Nonnull final NiftyRenderEngine r) {
    if (textLines == null) {
      return;
    }
    renderLines(w, r, textLines);
  }

  private void renderLines(@Nonnull final Element w, @Nonnull final NiftyRenderEngine r, @Nonnull String... lines) {
    RenderFont font = ensureFont(r);

    if (font == null) {
      return;
    }

    boolean stateSaved = prepareRenderEngine(r, font);

    int y = getStartYWithVerticalAlign(lines.length * font.getHeight(), w.getHeight(), textVAlign);
    for (String line : lines) {
      int yy = w.getY() + y;
      if (Math.abs(xOffsetHack) > 0) {
        int fittingOffset = FontHelper.getVisibleCharactersFromStart(font, line, Math.abs(xOffsetHack), 1.0f);
        String cut = line.substring(0, fittingOffset);
        String substring = line.substring(fittingOffset, line.length());
        int xx = w.getX() + xOffsetHack + font.getWidth(cut);
        renderLine(xx, yy, substring, r, selectionStart - fittingOffset, selectionEnd - fittingOffset);
      } else {
        int xx = w.getX() + getStartXWithHorizontalAlign(font.getWidth(line), w.getWidth(), textHAlign);
        renderLine(xx, yy, line, r, selectionStart, selectionEnd);
      }
      y += font.getHeight();
    }

    restoreRenderEngine(r, stateSaved);
  }

  private boolean prepareRenderEngine(@Nonnull final NiftyRenderEngine r, RenderFont font) {
    if (!r.isColorChanged()) {
      if (r.isColorAlphaChanged()) {
        r.setColorIgnoreAlpha(color);
      } else {
        r.setColor(color);
      }
    }
    boolean stateSaved = false;
    if (r.getFont() == null) {
      r.saveState(null);
      r.setFont(font);
      stateSaved = true;
    }
    return stateSaved;
  }

  private void restoreRenderEngine(@Nonnull final NiftyRenderEngine r, final boolean stateSaved) {
    if (stateSaved) {
      r.restoreState();
    }
  }

  @Nullable
  private RenderFont ensureFont(@Nonnull final NiftyRenderEngine r) {
    if (this.font == null) {
      return r.getFont();
    }
    return font;
  }

  /**
   * Get start Y for text rendering given the textHeight and the elementHeight.
   *
   * @param textHeight    text height
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
      // default is top in here
      return 0;
    }
  }

  /**
   * Get start x for text rendering given the textWidth and the elementWidth.
   *
   * @param textWidth       text width
   * @param elementWidth    element width
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
      // default is 0
      return 0;
    }
  }

  /**
   * render line.
   *
   * @param xx       x
   * @param yy       y
   * @param line     line
   * @param r        RenderEngine
   * @param selStart sel start
   * @param selEnd   sel end
   */
  private void renderLine(
      final int xx,
      final int yy,
      @Nonnull final String line,
      @Nonnull final NiftyRenderEngine r,
      final int selStart,
      final int selEnd) {
    r.renderText(line, xx, yy, selStart, selEnd, textSelectionColor);
  }

  /**
   * Helper method to get width of text.
   *
   * @return the width in pixel of the current set text.
   */
  public int getTextWidth() {
    return maxWidth;
  }

  /**
   * Helper method to get height of text.
   *
   * @return the height in pixel of the current set text.
   */
  public int getTextHeight() {
    RenderFont font = ensureFont(nifty.getRenderEngine());
    if (font == null || textLines == null) {
      return 0;
    }
    int calculatedHeight = font.getHeight() * textLines.length;
    if (textLineHeight.hasValue()) {
      calculatedHeight = textLineHeight.getValueAsInt(1.0f) * textLines.length;
    }

    if (textMinHeight.hasValue()) {
      if (calculatedHeight < textMinHeight.getValueAsInt(1.0f)) {
        return textMinHeight.getValueAsInt(1.0f);
      }
    }

    return calculatedHeight;
  }

  /**
   * set thing.
   *
   * @param newXoffsetHack xoffset
   */
  public void setxOffsetHack(final int newXoffsetHack) {
    this.xOffsetHack = newXoffsetHack;
  }

  /**
   * Get RenderFont.
   *
   * @return render font
   */
  @Nullable
  public RenderFont getFont() {
    return font;
  }

  /**
   * set a new selection.
   *
   * @param selectionStartParam start
   * @param selectionEndParam   end
   */
  public void setSelection(final int selectionStartParam, final int selectionEndParam) {
    this.selectionStart = selectionStartParam;
    this.selectionEnd = selectionEndParam;
  }

  /**
   * Set the font that is supposed to be used.
   *
   * @param fontParam the font or {@code null} in case the font of the render engine is supposed to be used
   */
  public void setFont(@Nullable final RenderFont fontParam) {
    this.font = fontParam;
  }

  /**
   * set new text selection color.
   *
   * @param textSelectionColorParam text selection color
   */
  public void setTextSelectionColor(@Nonnull final Color textSelectionColorParam) {
    this.textSelectionColor = textSelectionColorParam;
  }

  /**
   * set text vertical alignment.
   *
   * @param newTextVAlign text vertical alignment
   */
  public void setTextVAlign(@Nonnull final VerticalAlign newTextVAlign) {
    this.textVAlign = newTextVAlign;
  }

  /**
   * set text horizontal alignment.
   *
   * @param newTextHAlign text horizontal alignment
   */
  public void setTextHAlign(@Nonnull final HorizontalAlign newTextHAlign) {
    this.textHAlign = newTextHAlign;
  }

  /**
   * set color.
   *
   * @param newColor color
   */
  public void setColor(@Nonnull final Color newColor) {
    this.color = newColor;
  }

  /**
   * get original text.
   *
   * @return original text
   */
  @Nonnull
  public String getOriginalText() {
    return originalText;
  }

  @Nonnull
  public String getWrappedText() {
    StringBuilder result = new StringBuilder();
    if (textLines != null && textLines.length > 0) {
      result.append(textLines[0]);
      for (int i = 1; i < textLines.length; i++) {
        result.append('\n').append(textLines[i]);
      }
    }
    return result.toString();
  }

  public void setTextLineHeight(@Nonnull final SizeValue textLineHeight) {
    this.textLineHeight = textLineHeight;
  }

  public void setTextMinHeight(@Nonnull final SizeValue textMinHeight) {
    this.textMinHeight = textMinHeight;
  }

  @Nonnull
  private String[] wrapText(final int width, @Nonnull final NiftyRenderEngine r, @Nonnull final String... textLines) {
    RenderFont font = ensureFont(r);
    if (font == null) {
      return textLines;
    }
    List<String> lines = new ArrayList<String>();
    for (String line : textLines) {
      int lineLengthInPixel = font.getWidth(line);
      if (lineLengthInPixel > width) {
        lines.addAll(new TextBreak(line, width, font).split());
      } else {
        lines.add(line);
      }
    }
    return lines.toArray(new String[lines.size()]);
  }

  public void setWidthConstraint(
      @Nonnull final Element element,
      @Nonnull final SizeValue elementConstraintWidth,
      final int parentWidth,
      @Nonnull final NiftyRenderEngine renderEngine) {

    if (parentWidth == 0 || !lineWrapping || isCalculatedLineWrapping) {
      return;
    }

    int valueAsInt = element.getWidth();
    if (valueAsInt == 0) {
      valueAsInt = elementConstraintWidth.getValueAsInt(parentWidth);
    }
    if (valueAsInt <= 0) {
      return;
    }

    // remember some values so that we can correctly do auto word wrapping when someone changes the text
    this.hasBeenLayoutedElement = element;

    this.textLines = wrapText(valueAsInt, renderEngine, originalText.split("\n", -1));

    maxWidth = valueAsInt;

    // we'll now modify the element constraints so that the layout mechanism can later take this word wrapping
    // business correctly into account when the elements will be layouted. to make sure we're able to reset this
    // effect later, we'll remember that we've artificially calculated those values in here. so that we're able to
    // actually reset this later.
    isCalculatedLineWrapping = true;
    originalConstraintWidth = element.getConstraintWidth();
    originalConstraintHeight = element.getConstraintHeight();

    element.setConstraintWidth(
        elementConstraintWidth.hasWildcard() ? SizeValue.wildcard(getTextWidth()) : SizeValue.px(getTextWidth()));
    element.setConstraintHeight(SizeValue.px(getTextHeight()));
  }

  public void setLineWrapping(final boolean lineWrapping) {
    this.lineWrapping = lineWrapping;
  }

  public boolean isLineWrapping() {
    return lineWrapping;
  }

  @Nonnull
  public VerticalAlign getTextVAlign() {
    return textVAlign;
  }

  @Nonnull
  public HorizontalAlign getTextHAlign() {
    return textHAlign;
  }

  @Nonnull
  public Color getColor() {
    return color;
  }

  public void resetLayout(@Nonnull final Element element) {
    if (isCalculatedLineWrapping) {
      isCalculatedLineWrapping = false;

      element.setConstraintWidth(originalConstraintWidth);
      element.setConstraintHeight(originalConstraintHeight);
    }
  }

  @Nonnull
  public Color getTextSelectionColor() {
    return textSelectionColor;
  }
}
