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
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.nifty.NiftyImage;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLinearGradient;

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillGradient;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.types.NiftyLinearGradient.linearGradientFromAngleInDeg;

/**
 * Complex Demo / Example ... DeviantArt Viewer.
 * 
 * @author void
 */
public class UseCase_z00_DeviantArtViewer {

  public UseCase_z00_DeviantArtViewer(final Nifty nifty) {
    final NiftyImage image = nifty.createNiftyImage("nifty.png");
    final NiftyTransformationNode transformationNode = transformationNode();

    nifty
      .addNode(
        backgroundFillGradient(
          linearGradientFromAngleInDeg(0)
            .addColorStop(0.0, NiftyColor.aqua())
            .addColorStop(1.0, NiftyColor.blue())))
        .addNode(contentNode())
          .addNode(absoluteLayoutNode())
            .addNode(absoluteLayoutChildNode(nifty.getScreenWidth() / 2 - 400, nifty.getScreenHeight() / 2 - 300))
              .addNode(fixedSizeLayoutNode(800.f, 600.f))
                .addNode(transformationNode)
                  .addNode(backgroundFillColor(NiftyColor.fromString("#eeef")))
                    .addNode(contentNode().setCanvasPainter(new NiftyCanvasPainter() {
                      @Override
                      public void paint(final NiftyContentNode node, final NiftyCanvas canvas) {
                        canvas.drawImage(image, node.getWidth()/2 - image.getWidth()/2, node.getHeight()/2 - image.getHeight()/2);
                      }
                    }));
    nifty.startAnimated(0, 16, new NiftyCallback<Float>() {
      private float rot = 0;
      @Override public void execute(final Float totalTime) {
        rot += 0.01;
        transformationNode.setPosX(Math.sin(rot) * 512.);
        double scale = (Math.sin((rot/2-Math.PI/4+Math.PI/2)*2.0)+1.0)/2.0;
        transformationNode.setScaleX(scale);
        transformationNode.setScaleY(scale);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_z00_DeviantArtViewer.class, args);
  }
}
