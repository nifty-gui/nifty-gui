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

import de.lessvoid.nifty.api.ChildLayout;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * custom canvas painter rendering gradients.
 * @author void
 */
public class UseCase_b02_CanvasLinearGradient {

  public UseCase_b02_CanvasLinearGradient(final Nifty nifty) {
    NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.transparent());

    NiftyNode child = niftyNode.newChildNode(UnitValue.percent(100), UnitValue.percent(100));
    child.setBackgroundColor(NiftyColor.red());
    child.setCanvasPainter(new NiftyCanvasPainter() {
      @Override
      public void paint(final NiftyNode node, final NiftyCanvas canvas) {
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
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_b02_CanvasLinearGradient.class, args);
  }
}
