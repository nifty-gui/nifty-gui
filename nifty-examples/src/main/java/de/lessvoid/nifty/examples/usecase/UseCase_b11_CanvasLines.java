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
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;

import java.io.IOException;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.Horizontal.Center;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.Vertical.Middle;

/**
 * An example how to draw lines into a NiftyCanvas.
 * @author void
 */
public class UseCase_b11_CanvasLines {
  private static final float Y_POS = 60.f;

  public UseCase_b11_CanvasLines(final Nifty nifty) throws IOException {
    final NiftyContentNode contentNode = contentNode().setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
        long time = nifty.getTimeProvider().getMsTime();

        canvas.setFillStyle(NiftyColor.blue());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        // NiftyLineJoinType.Miter

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 0 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Round);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.white());
        canvas.stroke();

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 1 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Square);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.red());
        canvas.stroke();

        canvas.beginPath();
        addPath(canvas, time, Y_POS + 2 * 75.f);
        canvas.setLineWidth((Math.sin(time / 750.) + 1.f) / 2.f * 24.f + 1.f);
        canvas.setLineCap(NiftyLineCapType.Butt);
        canvas.setLineJoin(NiftyLineJoinType.Miter);
        canvas.setStrokeColor(NiftyColor.green());
        canvas.stroke();
      }

      private void addPath(final NiftyCanvas canvas, long time, final float y) {
        canvas.moveTo(10.f, y);
        canvas.lineTo(100.f, y + 50.f * (float) Math.sin(time / 900.));
        canvas.lineTo(200.f, y);
        canvas.lineTo(300.f, y + 50.f * (float) Math.sin(time / 750.));
        canvas.lineTo(400.f, y);
        canvas.lineTo(500.f, y + 50.f * (float) Math.sin(time / 650.));
      }
    });
    nifty
      .addNode(alignmentLayoutNode())
        .addNode(alignmentLayoutChildNode(Center, Middle))
          .addNode(fixedSizeLayoutNode(512, 512))
            .addNode(contentNode);
    nifty.startAnimated(0, 16, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float aFloat) {
        contentNode.redraw();
      }
    });
  }

  private UseCase_b11_CanvasLines() {
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b11_CanvasLines.class, args);
  }
}
