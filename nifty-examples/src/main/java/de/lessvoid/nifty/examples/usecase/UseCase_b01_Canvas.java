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

import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;

/**
 * Custom canvas painter example.
 *
 * @author void
 */
public class UseCase_b01_Canvas {

  public UseCase_b01_Canvas(final Nifty nifty) {
    final NiftyContentNode customContentNode = customContentNode();
    nifty
        .addNode(backgroundFillColor(NiftyColor.green()))
          .addNode(contentNode())
            .addNode(customContentNode);
    nifty.startAnimated(0, 50, new NiftyCallback<Float>() {
      @Override
      public void execute(final Float aFloat) {
        customContentNode.redraw();
      }
    });
  }

  private NiftyContentNode customContentNode() {
    return contentNode().setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
        // fill the whole node content with a plain white color
        canvas.setFillStyle(NiftyColor.white());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        // create a funky black rectangle inside the white
        canvas.setFillStyle(NiftyColor.black());
        canvas.fillRect(
            node.getWidth() / 2 - Math.random() * node.getWidth() / 2,
            node.getHeight() / 2 - Math.random() * node.getHeight() / 2,
            node.getWidth() - node.getWidth() / 2 + Math.random() * node.getWidth() / 2,
            node.getHeight() - node.getHeight() / 2 + Math.random() * node.getHeight() / 2);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b01_Canvas.class, args);
  }
}
