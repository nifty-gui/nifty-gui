/*
 * Copyright (c) 2014, Jens Hohmuth 
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

import java.io.IOException;

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * Demonstrate the rotate transform of the NiftyCanvas.
 * @author void
 */
public class UseCase_b08_CanvasTransformRotate {

  public UseCase_b08_CanvasTransformRotate(final Nifty nifty) throws IOException {
    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);

    NiftyNode child = rootNode.newChildNode(UnitValue.percent(50), UnitValue.percent(50));
    child.setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        // fill the whole node content with a plain white color
        canvas.setFillStyle(NiftyColor.white());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setFillStyle(NiftyColor.black());
        canvas.fillRect(10, 10, 50, 50);

        canvas.setFillStyle(NiftyColor.red());
        canvas.rotateDegrees(20.f);
        canvas.fillRect(10, 10, 50, 50);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b08_CanvasTransformRotate.class, args);
  }
}
