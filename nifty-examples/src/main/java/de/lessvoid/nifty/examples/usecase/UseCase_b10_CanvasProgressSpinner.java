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
package de.lessvoid.nifty.examples.usecase;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyCallback;
import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyMutableColor;

import java.io.IOException;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.Horizontal.Center;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.Vertical.Middle;

/**
 * An example how to use an animated NiftyCanvas for a custom progress spinner animation.
 * @author void
 */
public class UseCase_b10_CanvasProgressSpinner {
  private int pos = 0;

  public UseCase_b10_CanvasProgressSpinner(final Nifty nifty) throws IOException {
    final NiftyContentNode spinnerNode = createSpinnerNode();
    nifty
      .addNode(alignmentLayoutNode())
        .addNode(alignmentLayoutChildNode(Center, Middle))
          .addNode(fixedSizeLayoutNode(128.f, 128.f))
            .addNode(spinnerNode);
    nifty.startAnimated(0, 50, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float aFloat) {
        spinnerNode.redraw();
      }
    });
  }

  private NiftyContentNode createSpinnerNode() {
    return contentNode().setCanvasPainter(new NiftyCanvasPainter() {
      NiftyMutableColor color = NiftyMutableColor.fromColor(NiftyColor.fromString("#f000"));

      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
        canvas.setTransform();
        canvas.setFillStyle(NiftyColor.black());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        int max = 24;
        for (int i=0; i<max; i++) {
          int index = i + pos;
          if (index < 0) {
            index = index + max;
          }
          color.setAlpha(index / (float) max);
          canvas.setTransform();
          canvas.translate(node.getWidth() / 2, node.getHeight() / 2);
          canvas.rotateDegrees(i * 360.f / (float) max);
          canvas.translate(25.f, 0.f);
          canvas.setFillStyle(color.getColor());
          canvas.fillRect(0, -2.5, node.getWidth() * 0.3, 2.5);
        }

      pos--;
      if (pos <= -max) {
        pos = 0;
      }
    }});
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b10_CanvasProgressSpinner.class, args);
  }
}
