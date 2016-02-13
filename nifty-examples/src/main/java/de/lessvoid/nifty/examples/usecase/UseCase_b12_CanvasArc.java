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
import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.NiftyFont;
import de.lessvoid.nifty.NiftyNodeCallback;
import de.lessvoid.nifty.node.Horizontal;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.node.Vertical;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLineCapType;

import java.io.IOException;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyReferenceNode.referenceNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.Vertical.Bottom;
import static de.lessvoid.nifty.node.Vertical.Top;

/**
 * An example how to draw arcs into a NiftyCanvas.
 * @author void
 */
public class UseCase_b12_CanvasArc {
  private static NiftyFont font;
  private static float v = 1.99f;

  public UseCase_b12_CanvasArc(final Nifty nifty) throws IOException {
    font = nifty.createFont("fonts/aurulent-sans-16.fnt");

    final NiftyContentNode animatedContentNode = animatedContentNode();
    nifty.startAnimated(0, 16, animatedContentNode, new NiftyNodeCallback<Float, NiftyNode>() {
      @Override
      public void execute(final Float aFloat, final NiftyNode node) {
        v += 0.005;
        if (v > 2) {
          v -= 2;
        }

        animatedContentNode.redraw();
      }
    });

    nifty
      .addNode(alignmentLayoutNode())
        .addNode(alignmentLayoutChildNode(Horizontal.Center, Vertical.Middle))
          .addNode(fixedSizeLayoutNode(512.f, 512.f))
            .addNode(alignmentLayoutNode())
              .addNode(referenceNode("parent"))
                .addNode(alignmentLayoutChildNode(Horizontal.Left, Top))
                  .addNode(fixedSizeLayoutNode(256, 256))
                    .addNode(contentNode().setCanvasPainter(new NiftyCanvasPainter() {
                      @Override
                      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
                        draw(node, canvas, NiftyLineCapType.Butt);
                      }
                    }))
              .addAsChildOf("parent")
                .addNode(alignmentLayoutChildNode(Horizontal.Right, Top))
                  .addNode(fixedSizeLayoutNode(256, 256))
                    .addNode(contentNode().setCanvasPainter(new NiftyCanvasPainter() {
                      @Override
                      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
                        draw(node, canvas, NiftyLineCapType.Square);
                    }
                  }))
              .addAsChildOf("parent")
                .addNode(alignmentLayoutChildNode(Horizontal.Left, Bottom))
                  .addNode(fixedSizeLayoutNode(256, 256))
                    .addNode(contentNode().setCanvasPainter(new NiftyCanvasPainter() {
                      @Override
                      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
                        draw(node, canvas, NiftyLineCapType.Round);
                      }
                    }))
              .addAsChildOf("parent")
                .addNode(alignmentLayoutChildNode(Horizontal.Right, Bottom))
                  .addNode(fixedSizeLayoutNode(256, 256))
                    .addNode(animatedContentNode);
  }

  private NiftyContentNode animatedContentNode() {
    return contentNode().setCanvasPainter(new NiftyCanvasPainter() {
        @Override
        public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
          canvas.setLineCap(NiftyLineCapType.Square);
          canvas.setFillStyle(NiftyColor.blue());
          canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

          canvas.setTextColor(NiftyColor.white());
          canvas.text(font, 5, 5, "Animated");

          canvas.beginPath();
          canvas.arc(128., 128., 100., v * Math.PI, (v + 1.25) * Math.PI);
          canvas.setLineWidth(24);
          canvas.setStrokeColor(NiftyColor.white());
          canvas.stroke();

          canvas.beginPath();
          canvas.arc(128., 128., 100., v * Math.PI, (v + 1.25) * Math.PI);
          canvas.setLineWidth(1);
          canvas.setStrokeColor(NiftyColor.black());
          canvas.stroke();
        }
      });
  }

  private static void draw(final NiftyContentNode node, final NiftyCanvas canvas, final NiftyLineCapType cap) {
    canvas.setLineCap(cap);
    canvas.setFillStyle(NiftyColor.blue());
    canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

    canvas.setTextColor(NiftyColor.white());
    canvas.text(font, 5, 5, cap.toString());

    canvas.beginPath();
    canvas.arc(128., 128., 100., 0.5 * Math.PI, 1.25 * Math.PI);
    canvas.setLineWidth(24);
    canvas.setStrokeColor(NiftyColor.white());
    canvas.stroke();

    canvas.beginPath();
    canvas.arc(128., 128., 100., 0.5 * Math.PI, 1.25 * Math.PI);
    canvas.setLineWidth(1);
    canvas.setStrokeColor(NiftyColor.black());
    canvas.stroke();
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b12_CanvasArc.class, args);
  }
}
