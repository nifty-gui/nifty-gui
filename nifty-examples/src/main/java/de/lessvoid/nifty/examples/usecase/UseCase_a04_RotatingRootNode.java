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

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_a04_RotatingRootNode {
/* FIXME
  public UseCase_a04_RotatingRootNode(final Nifty nifty) {
    nifty.clearScreenBeforeRender();

    final NiftyNode niftyNode = nifty.createRootNode(UnitValue.px(400), UnitValue.px(400), ChildLayout.Absolute);
    niftyNode.setBackgroundColor(NiftyColor.green());
    niftyNode.startAnimated(0, 15, new NiftyCallback<Float>() {
      private float angle = 0;

      @Override
      public void execute(final Float time) {
        niftyNode.setRotationZ(angle++);
      }
    });

    NiftyNode childNode = niftyNode.newChildNode(UnitValue.px(100), UnitValue.px(100), ChildLayout.Center);
    childNode.setBackgroundColor(NiftyColor.red());
    childNode.setXConstraint(UnitValue.px(50));
    childNode.setYConstraint(UnitValue.px(50));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a04_RotatingRootNode.class, args);
  }
  */
}
