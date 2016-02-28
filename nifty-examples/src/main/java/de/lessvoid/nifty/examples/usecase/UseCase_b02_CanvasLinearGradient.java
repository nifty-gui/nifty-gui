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
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLinearGradient;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.Horizontal.Center;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.Vertical.Middle;

/**
 * custom canvas painter rendering gradients.
 * 
 * @author void
 */
public class UseCase_b02_CanvasLinearGradient {

  public UseCase_b02_CanvasLinearGradient(final Nifty nifty) {

    nifty.addNode(alignmentLayoutNode()).addNode(alignmentLayoutChildNode(Center, Middle))
        .addNode(fixedSizeLayoutNode(400.f, 400.f)).addNode(backgroundFillColor(NiftyColor.transparent()))
        .addNode(contentNode().setCanvasPainter(new NiftyCanvasPainter() {

          @Override
          public void paint(NiftyContentNode node, NiftyCanvas canvas) {
            canvas.setFillStyle(NiftyColor.blue());
            canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

            NiftyLinearGradient gradient = NiftyLinearGradient.createFromAngleInDeg(90.);
            gradient.addColorStop(0.0, NiftyColor.red());
            gradient.addColorStop(1.0, NiftyColor.white());
            canvas.setFillStyle(gradient);
            canvas.fillRect(50, 50, 300, 100);

            gradient = NiftyLinearGradient.createFromAngleInDeg(180.);
            gradient.addColorStop(0.0, NiftyColor.green());
            gradient.addColorStop(1.0, NiftyColor.black());
            canvas.setFillStyle(gradient);
            canvas.fillRect(50, 150, 300, 100);

            gradient = NiftyLinearGradient.createFromAngleInDeg(135.);
            gradient.addColorStop(0.0, NiftyColor.white());
            gradient.addColorStop(1.0, NiftyColor.black());
            canvas.setFillStyle(gradient);
            canvas.fillRect(50, 250, 300, 100);
          }
        }));

  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b02_CanvasLinearGradient.class, args);
  }

}
