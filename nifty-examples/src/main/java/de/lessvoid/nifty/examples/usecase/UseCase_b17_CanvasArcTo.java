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

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.types.NiftyColor.blue;
import static de.lessvoid.nifty.types.NiftyColor.green;
import static de.lessvoid.nifty.types.NiftyColor.red;

/**
 * Demonstrate the arcTo canvas method.
 *
 * @author void
 */
public class UseCase_b17_CanvasArcTo {
  public UseCase_b17_CanvasArcTo(final Nifty nifty) {
    final NiftyContentNode contentNode = contentNode().setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
        canvas.beginPath();
        canvas.moveTo(100, 225); // PO
        canvas.arcTo(300, 25, 500, 225, 75); // P1, P2 and radius
        canvas.lineTo(500, 225); // P2
        canvas.stroke();
      }
    });
    nifty
      .addNode(absoluteLayoutNode())
        .addNode(absoluteLayoutChildNode((nifty.getScreenWidth() - 512) / 2, (nifty.getScreenHeight() - 512) / 2))
          .addNode(fixedSizeLayoutNode(512.f, 512.f))
            .addNode(contentNode);
    nifty.startAnimated(0, 100, new NiftyCallback<Float>() {
      @Override
      public void execute(Float aFloat) {
        contentNode.redraw();
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    de.lessvoid.nifty.examples.usecase.UseCaseRunner.run(UseCase_b17_CanvasArcTo.class, args);
  }
}
