/*
 * Copyright (c) 2015, Nifty GUI Community
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
package de.lessvoid.nifty.control;

import de.lessvoid.nifty.spi.NiftyFont;
import de.lessvoid.nifty.spi.node.NiftyControlNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * The public API of the Button NiftyControlNode.
 *
 * Created by void on 07.02.16.
 */
public final class ButtonNode implements NiftyControlNode {
  private final ButtonNodeImpl impl;

  public static ButtonNode button(final NiftyFont font, final String text) {
    ButtonNode buttonNode = new ButtonNode();
    buttonNode.setFont(font);
    buttonNode.setText(text);
    return buttonNode;
  }

  public static ButtonNode button() {
    return new ButtonNode();
  }

  private ButtonNode() {
    this.impl = new ButtonNodeImpl();
  }

  ButtonNode(final ButtonNodeImpl buttonNode) {
    this.impl = buttonNode;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Public API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public String getText() {
    return impl.getText();
  }

  public void setText(final String text) {
    impl.setText(text);
  }

  public NiftyFont getFont() {
    return impl.getFont();
  }

  public void setFont(final NiftyFont font) {
    impl.setFont(font);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyControlNode
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void setEnabled(final boolean enabled) {

  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // friend api
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Nonnull
  NiftyNodeImpl<ButtonNode> getImpl() {
    return impl;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // standard
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ButtonNode that = (ButtonNode) o;
    return impl.equals(that.impl);
  }

  @Override
  public int hashCode() {
    return impl.hashCode();
  }
}
