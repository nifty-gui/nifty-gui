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
import de.lessvoid.nifty.NiftyNodeCallback;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.types.NiftyColor;

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.FixedSizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundColorNode.backgroundColorNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;

/**
 * A single root node of a fixed size with a background color that is constantly rotating.
 * @author void
 */
public class UseCase_a04_RotatingRootNode {

  public UseCase_a04_RotatingRootNode(final Nifty nifty) {
    NiftyTransformationNode rootTransformation = transformationNode();
    nifty.startAnimated(0, 15, rootTransformation, new NiftyNodeCallback<Float, NiftyTransformationNode>() {
      private float angle = 0;
      @Override
      public void execute(final Float time, final NiftyTransformationNode niftyTransformationNode) {
        niftyTransformationNode.setAngleZ(angle++);
      }
    });

    nifty.clearScreenBeforeRender();
    nifty
        .addNode(absoluteLayoutNode())
          .addNode(absoluteLayoutChildNode(nifty.getScreenWidth() / 2 - 50, nifty.getScreenHeight() / 2 - 50))
            .addNode(fixedSizeLayoutNode(100.f, 100.f))
              .addNode(rootTransformation)
                .addNode(backgroundColorNode(NiftyColor.green()))
                  .addNode(contentNode());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a04_RotatingRootNode.class, args);
  }
}
