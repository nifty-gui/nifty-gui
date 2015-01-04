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
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * An example how to draw arcs and lines as part of one path into a NiftyCanvas.
 * @author void
 */
public class UseCase_b13_CanvasLineAndArc {
  public UseCase_b13_CanvasLineAndArc(final Nifty nifty) throws IOException {
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Vertical);
    rootNode.setBackgroundColor(NiftyColor.black());

    NiftyNode top = rootNode.newChildNode(UnitValue.percent(100), UnitValue.percent(50), ChildLayout.Horizontal);
    NiftyNode bottom = rootNode.newChildNode(UnitValue.percent(100), UnitValue.percent(50), ChildLayout.Horizontal);

    // render arc -> arc (which will be connected automatically by a line)
    NiftyNode childNode1 = top.newChildNode(UnitValue.percent(50), UnitValue.percent(100));
    childNode1.startAnimatedRedraw(0, 10);
    childNode1.addCanvasPainter(new NiftyCanvasPainter() {
      float v = 0;
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.blue());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setLineWidth(25);
        canvas.setStrokeColor(NiftyColor.white());

        canvas.beginPath();
        canvas.arc(100., 100., 50., v * Math.PI, 2 * Math.PI);
        canvas.arc(412., 284., 50., v * Math.PI, 2 * Math.PI);
        canvas.stroke();

        v += 0.005;
        if (v > 2) {
          v -= 2;
        }
      }
    });

    // render arc -> line -> arc
    NiftyNode childNode2 = top.newChildNode(UnitValue.percent(50), UnitValue.percent(100));
    childNode2.startAnimatedRedraw(0, 10);
    childNode2.addCanvasPainter(new NiftyCanvasPainter() {
      float v = 0;
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.green());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setLineWidth(25);
        canvas.setStrokeColor(NiftyColor.white());

        canvas.beginPath();
        canvas.arc(100., 100., 50., v * Math.PI, 2 * Math.PI);
        canvas.lineTo(1024/4, 768/4);
        canvas.arc(412., 284., 50., v * Math.PI, 2 * Math.PI);
        canvas.stroke();

        v += 0.005;
        if (v > 2) {
          v -= 2;
        }
      }
    });

    // render moveTo -> arc
    NiftyNode childNode3 = bottom.newChildNode(UnitValue.percent(50), UnitValue.percent(100));
    childNode3.startAnimatedRedraw(0, 10);
    childNode3.addCanvasPainter(new NiftyCanvasPainter() {
      float v = 0;
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.red());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setLineWidth(25);
        canvas.setStrokeColor(NiftyColor.white());

        canvas.beginPath();
        canvas.moveTo(50, 50);
        canvas.arc(412., 284., 50., v * Math.PI, 2 * Math.PI);
        canvas.stroke();

        v += 0.005;
        if (v > 2) {
          v -= 2;
        }
      }
    });

    // render moveTo -> lineTo -> arc -> lineTo
    NiftyNode childNode4 = bottom.newChildNode(UnitValue.percent(50), UnitValue.percent(100));
    childNode4.startAnimatedRedraw(0, 10);
    childNode4.addCanvasPainter(new NiftyCanvasPainter() {
      float v = 0;
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.black());
        canvas.fillRect(0, 0, node.getWidth(), node.getHeight());

        canvas.setLineWidth(25);
        canvas.setStrokeColor(NiftyColor.white());

        canvas.beginPath();
        canvas.moveTo(50, 50);
        canvas.lineTo(100, 50);
        canvas.arc(256., 256., 100., v * Math.PI, 2 * Math.PI);
        canvas.lineTo(412, 284);
        canvas.stroke();

        v += 0.005;
        if (v > 2) {
          v -= 2;
        }
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b13_CanvasLineAndArc.class, args);
  }
}
