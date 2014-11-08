/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal.render;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.VAlign;

public class TextRenderer {
  // the font to use
  @Nonnull private NiftyFont font;

  // the original text string
  @Nonnull private String originalText;

  // the text to output
  @Nonnull private String[] textLines = new String[0];

  // max width of all text strings
  private int maxWidth;

  // vertical alignment
  @Nonnull private VAlign textVAlign = VAlign.center;

  // horizontal alignment
  @Nonnull private HAlign textHAlign = HAlign.center;

  /**
   * Initialize the TextRenderer with the given font and text.
   * @param font the font to use
   * @param text the text to render
   */
  public void initialize(final @Nonnull NiftyFont font, final @Nonnull String text) {
    this.font = font;
    this.originalText = text;
    update();
  }

  /**
   * Get the maximum text width of all text lines.
   * @return the max width
   */
  public int getTextWidth() {
    return maxWidth;
  }

  /**
   * Set horizontal alignment.
   * @param textHAlign new textHAlign
   */
  public void setTextHAlign(final HAlign textHAlign) {
    this.textHAlign = textHAlign;
  }

  /**
   * Set vertical alignment.
   * @param textVAlign new textVAlign
   */
  public void setTextVAlign(final VAlign textVAlign) {
    this.textVAlign = textVAlign;
  }

  /**
   * Render the text into the given NiftyCanvas using NiftyNode as the parent element.
   *
   * @param node NiftyNode
   * @param canvas NiftyCanvas
   */
  public void renderText(
      @Nonnull final NiftyNode node,
      @Nonnull final NiftyCanvas canvas) {
    if (textLines.length == 0) {
      return;
    }

    int y = getStartYWithVerticalAlign(textLines.length * font.getHeight(), node.getHeight());
    for (String line : textLines) {
      int textPosY = y;
      /*
      if (Math.abs(xOffsetHack) > 0) {
        int fittingOffset = FontHelper.getVisibleCharactersFromStart(font, line, Math.abs(xOffsetHack), 1.0f);
        String cut = line.substring(0, fittingOffset);
        String substring = line.substring(fittingOffset, line.length());
        int xx = node.getX() + xOffsetHack + font.getWidth(cut);
        renderLine(xx, yy, substring, r, selectionStart - fittingOffset, selectionEnd - fittingOffset);
      } else {
      */
      int textPosX = getStartXWithHorizontalAlign(font.getWidth(line), node.getWidth());
      renderLine(textPosX, textPosY, line, -1, -1, canvas);
      //}
      y += font.getHeight();
    }
  }

  /**
   * Enable line wrapping of text. This requires a NiftyNode with a fixed width using a
   * width constraint. If the given NiftyNode does not have a fixed width no line wrapping
   * will be performed.
   *
   * @param node the NiftyNode
   */
  /*
  public void enableLineWrapping(@Nonnull final NiftyNode node) {
    int parentWidth = node.get .getWidth();
    if (parentWidth == 0) {
      return;
    }

// FIXME
//    if (parentWidth == 0 || !lineWrapping || isCalculatedLineWrapping) {
//      return;
//    }

    int wrapWidth = node.getWidth();
    if (wrapWidth == 0) {
      wrapWidth = elementConstraintWidth.getValueAsInt(parentWidth);
    }
    if (wrapWidth <= 0) {
      return;
    }

    // remember some values so that we can correctly do auto word wrapping when someone changes the text
    this.hasBeenLayoutedElement = node;

    this.textLines = wrapText(wrapWidth, renderEngine, originalText.split("\n", -1));

    maxWidth = wrapWidth;

    // we'll now modify the element constraints so that the layout mechanism can later take this word wrapping
    // business correctly into account when the elements will be layouted. to make sure we're able to reset this
    // effect later, we'll remember that we've artificially calculated those values in here. so that we're able to
    // actually reset this later.
    isCalculatedLineWrapping = true;
    originalConstraintWidth = node.getConstraintWidth();
    originalConstraintHeight = node.getConstraintHeight();

    node.setConstraintWidth(elementConstraintWidth.hasWildcard() ? UnitValue.wildcard(getTextWidth()) : UnitValue.px(getTextWidth()));
    node.setConstraintHeight(UnitValue.px(getTextHeight()));
  }
*/
  private void update() {
    if (!originalText.isEmpty()) {
      this.textLines = originalText.split("\n", -1);
    }
    this.maxWidth = calcMaxWidth(textLines);
  }

  private int calcMaxWidth(final String[] lines) {
    int width = 0;
    for (int i = 0; i < lines.length; i++) {
      width = Math.max(font.getWidth(lines[i]), width);
    }
    return width;
  }

  private int getStartXWithHorizontalAlign(final int textWidth, final int nodeWidth) {
    if (HAlign.left == textHAlign) {
      return 0;
    }
    if (HAlign.center == textHAlign) {
      return (nodeWidth - textWidth) / 2;
    }
    if (HAlign.right == textHAlign) {
      return nodeWidth - textWidth;
    }
    // default is 0
    return 0;
  }

  private int getStartYWithVerticalAlign(final int textHeight, final int nodeHeight) {
    if (VAlign.top == textVAlign) {
      return 0;
    }
    if (VAlign.center == textVAlign) {
      return (nodeHeight - textHeight) / 2;
    }
    if (VAlign.bottom == textVAlign) {
      return nodeHeight - textHeight;
    }
    // default is top in here
    return 0;
  }

  private void renderLine(
      final int textPosX,
      final int textPosY,
      @Nonnull final String line,
      final int selStart,
      final int selEnd,
      @Nonnull final NiftyCanvas canvas) {
    canvas.text(font, textPosX, textPosY, line);
  }
}
