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
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyNodeState;
import de.lessvoid.nifty.api.NiftyStateManager;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.api.annotation.NiftyStyleProperty;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterHAlign;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyColor;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterNiftyFont;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterVAlign;

public class Button extends NiftyAbstractControl {
  private Label label;

  @Override
  public void init(final NiftyNode niftyNode, final NiftyStateManager stateManager) {
    super.init(niftyNode, stateManager);

    niftyNode.setChildLayout(ChildLayout.Center);
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
  @NiftyStyleProperty(name = "text")
  public void setText(@Nonnull final String text) {
    setText(text, NiftyNodeState.Regular);
  }

  public void setText(@Nonnull final String text, final NiftyNodeState ... states) {
    label.setText(text, states);
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
  @NiftyStyleProperty(name = "font", converter = NiftyStyleStringConverterNiftyFont.class)
  public void setFont(final NiftyFont font) {
    setFont(font, NiftyNodeState.Regular);
  }

  public void setFont(final NiftyFont font, final NiftyNodeState ... states) {
    label.setFont(font, states);
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
  @NiftyStyleProperty(name = "text-valign", converter = NiftyStyleStringConverterVAlign.class)
  public void setTextVAlign(final VAlign newTextVAlign) {
    setTextVAlign(newTextVAlign, NiftyNodeState.Regular);
  }

  public void setTextVAlign(final VAlign newTextVAlign, final NiftyNodeState ... states) {
    label.setTextVAlign(newTextVAlign, states);
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
  @NiftyStyleProperty(name = "text-halign", converter = NiftyStyleStringConverterHAlign.class)
  public void setTextHAlign(@Nonnull final HAlign newTextHAlign) {
    setTextHAlign(newTextHAlign, NiftyNodeState.Regular);
  }

  public void setTextHAlign(@Nonnull final HAlign newTextHAlign, final NiftyNodeState ... states) {
    label.setTextHAlign(newTextHAlign, states);
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
  @NiftyStyleProperty(name = "text-color", converter = NiftyStyleStringConverterNiftyColor.class)
  public void setTextColor(final NiftyColor newColor) {
    setTextColor(newColor, NiftyNodeState.Regular);
  }

  public void setTextColor(final NiftyColor newColor, final NiftyNodeState ... states) {
    label.setTextColor(newColor, states);
  }
}
