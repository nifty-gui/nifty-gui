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
package de.lessvoid.nifty.api.controls;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.VAlign;

public class Button extends NiftyAbstractControl {
  private Label label;

  @Override
  public void init(final NiftyNode niftyNode) {
    super.init(niftyNode);

    niftyNode.setChildLayout(ChildLayout.Center);
    niftyNode.addCanvasPainter(new ButtonCanvasPainter());
    niftyNode.setStyleClass("button");

    label = niftyNode.newControl(Label.class);
  }

  /**
   * Activate/Click this button.
   */
  public void activate() {
  }

  /**
   * Get the current text the button shows.
   * 
   * @return text
   */
  public String getText() {
    return label.getText();
  }

  /**
   * Set the current text the button shows.
   * 
   * @param text
   *          new text to show
   */
  public void setText(@Nonnull final String text) {
    label.setText(text);
  }

  /**
   * Get the width of the text.
   * 
   * @return width of text in px
   */
  public int getTextWidth() {
    return label.getTextWidth();
  }

  /**
   * Get the height of the text.
   * 
   * @return height of text in px
   */
  public int getTextHeight() {
    return label.getTextHeight();
  }

  /**
   * Get the current Font of the button text.
   * 
   * @return the current Font
   */
  public NiftyFont getFont() {
    return label.getFont();
  }

  /**
   * Set the Font of the button text.
   * 
   * @param font
   *          new font or {@code null} to use the default font
   */
  public void setFont(final NiftyFont font) {
    label.setFont(font);
  }

  /**
   * Get the Vertical Align of the Button text.
   * 
   * @return VerticalAlign
   */
  public VAlign getTextVAlign() {
    return label.getTextVAlign();
  }

  /**
   * Set the Vertical Align of the Button text.
   * 
   * @param newTextVAlign
   *          VerticalAlign
   */
  public void setTextVAlign(final VAlign newTextVAlign) {
    label.setTextVAlign(newTextVAlign);
  }

  /**
   * Get the Horizontal Align of the Button text.
   * 
   * @return HorizontalAlign
   */
  public HAlign getTextHAlign() {
    return label.getTextHAlign();
  }

  /**
   * Set the Horizontal Align of the Button text.
   * 
   * @param newTextHAlign
   *          HorizontalAlign
   */
  public void setTextHAlign(@Nonnull final HAlign newTextHAlign) {
    label.setTextHAlign(newTextHAlign);
  }

  /**
   * Get the text color of the Button Text.
   * 
   * @return Color of the Text
   */
  public NiftyColor getTextColor() {
    return label.getTextColor();
  }

  /**
   * Set the text color of the Button Text.
   * 
   * @param newColor
   *          new Color for the button text
   */
  void setTextColor(final NiftyColor newColor) {
    label.setTextColor(newColor);
  }

  private class ButtonCanvasPainter implements NiftyCanvasPainter {
    @Override
    public void paint(final NiftyNode node, final NiftyCanvas canvas) {
      /*
      canvas.setFillStyle(NiftyColor.fromString("#800a"));
      canvas.fillRect(0., 0., node.getWidth(), node.getHeight());
      */
    }
  }
}
