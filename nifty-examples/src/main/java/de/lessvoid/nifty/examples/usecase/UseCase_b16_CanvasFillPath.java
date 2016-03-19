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

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;

/**
 * Demonstrate filled path elements.
 *
 * @author void
 */
public class UseCase_b16_CanvasFillPath {
  public UseCase_b16_CanvasFillPath(final Nifty nifty) {
    final NiftyContentNode contentNode = contentNode().setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.blue());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setFillStyle(NiftyColor.green());
        canvas.fillRect(100, 100, 200, 200);

        canvas.beginPath();
        canvas.arc(200, 200, 100, 0., 2 * Math.PI - .5 * Math.PI);
        canvas.lineTo(200, 200);

        canvas.setFillStyle(NiftyColor.red());
        canvas.fill();
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

  private UseCase_b16_CanvasFillPath() {
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b16_CanvasFillPath.class, args);
  }
}
