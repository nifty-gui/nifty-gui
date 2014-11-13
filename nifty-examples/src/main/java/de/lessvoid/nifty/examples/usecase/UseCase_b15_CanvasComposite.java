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
import de.lessvoid.nifty.api.NiftyCompositeOperation;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * An example how to use the different composite operations.
 * @author void
 */
public class UseCase_b15_CanvasComposite {

  public UseCase_b15_CanvasComposite(final Nifty nifty) throws IOException {
    final NiftyFont font = nifty.createFont("fonts/aurulent-sans-16.fnt");
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    NiftyNode rootNode = nifty.createRootNodeFullscreen(ChildLayout.Center);
    rootNode.setBackgroundColor(NiftyColor.BLACK());

    final NiftyNode centerNode = rootNode.newChildNode(UnitValue.percent(60), UnitValue.percent(50), ChildLayout.Vertical);
    centerNode.setBackgroundColor(NiftyColor.WHITE());

    NiftyNode rowNode = centerNode.newChildNode(ChildLayout.Horizontal);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.SourceOver);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.SourceAtop);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.SourceIn);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.SourceOut);

    rowNode = centerNode.newChildNode(ChildLayout.Horizontal);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.DestinationOver);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.DestinationAtop);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.DestinationIn);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.DestinationOut);

    rowNode = centerNode.newChildNode(ChildLayout.Horizontal);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.Lighter);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.Copy);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.XOR);
    compositeExampleNode(rowNode, font, NiftyCompositeOperation.Clear);
  }

  private void compositeExampleNode(final NiftyNode rootNode, final NiftyFont font, final NiftyCompositeOperation operation) {
    final NiftyNode canvasNode = rootNode.newChildNode(UnitValue.px(150), UnitValue.px(120), ChildLayout.Absolute);
    canvasNode.startAnimatedRedraw(0, 16);
    canvasNode.addCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
        canvas.setFillStyle(NiftyColor.BLUE());
        canvas.fillRect(0, 20, 75, 50);
  
        canvas.setGlobalCompositeOperation(operation);
        canvas.setFillStyle(NiftyColor.RED());
        canvas.fillRect(30, 50, 75, 50);

        canvas.setGlobalCompositeOperation(NiftyCompositeOperation.SourceOver);
        canvas.setTextColor(NiftyColor.BLACK());
        canvas.setTextSize(1.0f);
        canvas.text(font, 0, 0, operation.toString());
      }
    });
  }
    
  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b15_CanvasComposite.class, args);
  }
}
