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

import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.spi.NiftyFont;
import de.lessvoid.nifty.NiftyNodeBuilder;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.spi.node.NiftyControlNodeImpl;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLinearGradient;

import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;

/**
 * Created by void on 07.02.16.
 */
final class ButtonNodeImpl implements NiftyControlNodeImpl<ButtonNode, ButtonNodeControlBuilder> {
  private final ButtonCanvasPainter buttonCanvas = new ButtonCanvasPainter();
  private final NiftyContentNode content = contentNode().setCanvasPainter(buttonCanvas);

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // ButtonNode
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public String getText() {
    return buttonCanvas.text;
  }

  public void setText(final String text) {
    buttonCanvas.text = text;
    content.redraw();
  }

  public NiftyFont getFont() {
    return buttonCanvas.font;
  }

  public void setFont(final NiftyFont font) {
    buttonCanvas.font = font;
    content.redraw();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyNodeImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public ButtonNode getNiftyNode() {
    return new ButtonNode(this);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // NiftyControlNodeImpl
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public ButtonNodeControlBuilder getBuilder() {
    return new ButtonNodeControlBuilder();
  }

  @Override
  public void explode(final NiftyNodeBuilder nodeBuilder) {
    nodeBuilder
        .addNode(absoluteLayoutNode())
            .addNode(fixedSizeLayoutNode(140.f, 30.f))
              .addNode(content);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Internals
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static class ButtonCanvasPainter implements NiftyCanvasPainter {
    private String text;
    private NiftyFont font;

    @Override
    public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
      //canvas.setFillStyle(NiftyColor.white());
      //canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

      double radius = 8.0;
      double inset = 4.0;
      double width = node.getWidth();
      canvas.beginPath();
      canvas.moveTo(        10.0 + inset, 0.0 + inset);
      canvas.lineTo(width - 10.0 - inset, 0.0 + inset);
      canvas.arcTo(        width - inset, 0.0 + inset, width - inset, 10.0 + inset, radius);
      canvas.lineTo(       width - inset, node.getHeight() - 10.0 - inset);
      canvas.arcTo(        width - inset, node.getHeight() - inset, width - 10.0 - inset, node.getHeight() - inset, radius);
      canvas.lineTo(        10.0 + inset, node.getHeight() - inset);
      canvas.arcTo(          0.0 + inset, node.getHeight() - inset, 0.0 + inset, node.getHeight() - 10.0 - inset, radius);
      canvas.lineTo(         0.0 + inset, 10.0 + inset);
      canvas.arcTo(          0.0 + inset, 0.0 + inset, 10.0 + inset, 0.0 + inset, radius);

      NiftyLinearGradient gradient = NiftyLinearGradient.linearGradientFromtPoints(0.0, 30.0, 0.0, 0.f);
      gradient.addColorStop( 0.0,   NiftyColor.fromString("#703434"));
      gradient.addColorStop( 0.458, NiftyColor.fromString("#211"));
      gradient.addColorStop( 0.508, NiftyColor.fromString("#211"));
      gradient.addColorStop( 0.545, NiftyColor.fromString("#343434"));
      gradient.addColorStop( 1.0,   NiftyColor.fromString("#737373"));
      canvas.setFillStyle(gradient);

      canvas.setStrokeColor(NiftyColor.black());
      canvas.setLineWidth(4.0);
      canvas.stroke();

      canvas.fill();

      canvas.setTextColor(NiftyColor.white());
      canvas.text(font, node.getWidth()/2 - font.getWidth(text)/2, node.getHeight()/2 - font.getHeight()/2, text);
    }
  }
}
