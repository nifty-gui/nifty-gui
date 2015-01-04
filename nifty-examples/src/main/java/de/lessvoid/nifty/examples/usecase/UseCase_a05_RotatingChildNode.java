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
import de.lessvoid.nifty.api.NiftyCallback;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.NiftyStatisticsMode;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_a05_RotatingChildNode {

  public UseCase_a05_RotatingChildNode(final Nifty nifty) throws IOException {
    nifty.clearScreenBeforeRender();
    nifty.showStatistics(NiftyStatisticsMode.ShowFPS);

    final NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Center);
    niftyNode.setBackgroundColor(NiftyColor.green());
    niftyNode.setPivot(0.5, 0.5);

    final NiftyNode childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.black());
    childNode.setPivot(0.5, 0.5);

    final NiftyNode grandChildNode = childNode.newChildNode(UnitValue.px(25), UnitValue.px(25));
    grandChildNode.setBackgroundColor(NiftyColor.red());
    grandChildNode.setPivot(0.5, 0.5);

    niftyNode.startAnimated(0, 25, new NiftyCallback<Float>() {
      private float rot = 0;

      @Override
      public void execute(final Float totalTime) {
        rot += 1.f;

        childNode.setRotationX(rot);
        childNode.setRotationY(rot);
        childNode.setRotationZ(rot);

        grandChildNode.setRotationZ(rot*10);

        niftyNode.setScaleX((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
        niftyNode.setScaleY((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a05_RotatingChildNode.class, args);
  }
}
