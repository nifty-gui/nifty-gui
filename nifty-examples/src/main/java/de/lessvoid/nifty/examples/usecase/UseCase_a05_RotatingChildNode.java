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
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundColorNode.backgroundColorNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyReferenceNode.referenceNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;

/**
 * A root node with two child nodes.
 * @author void
 */
public class UseCase_a05_RotatingChildNode {

  public UseCase_a05_RotatingChildNode(final Nifty nifty) throws IOException {
    //nifty.setShowRenderBuckets(true);
    //nifty.setShowRenderNodes(true);

    final NiftyTransformationNode rootTransformation = transformationNode();
    final NiftyTransformationNode childTransformation = transformationNode();
    childTransformation.setPivotX(0.5);
    childTransformation.setPivotY(0.5);

    final NiftyTransformationNode grandChildNodeTransformation = transformationNode();
    grandChildNodeTransformation.setPivotX(0.5);
    grandChildNodeTransformation.setPivotY(0.5);

    nifty.clearScreenBeforeRender();
    nifty
        .addNode(absoluteLayoutNode())
          .addNode(absoluteLayoutChildNode(nifty.getScreenWidth() / 2 - 200, nifty.getScreenHeight() / 2 - 200))
            .addNode(fixedSizeLayoutNode(400.f, 400.f))
              .addNode(rootTransformation)
                .addNode(backgroundColorNode(NiftyColor.green()))
                  .addNode(contentNode())
                    .addNode(referenceNode("parent"))
                      .addNode(absoluteLayoutNode())
                        .addNode(absoluteLayoutChildNode())
                          .addNode(fixedSizeLayoutNode(100.f, 100.f))
                            .addNode(childTransformation)
                              .addNode(backgroundColorNode(NiftyColor.blue()))
                                .addNode(contentNode())
                                  .addNode(absoluteLayoutNode())
                                    .addNode(absoluteLayoutChildNode())
                                      .addNode(fixedSizeLayoutNode(25.f, 25.f))
                                        .addNode(grandChildNodeTransformation)
                                          .addNode(backgroundColorNode(NiftyColor.red()))
                                            .addNode(contentNode())
                    .addAsChildOf("parent")
                      .addNode(absoluteLayoutNode())
                        .addNode(absoluteLayoutChildNode(NiftyPoint.newNiftyPoint(150.f, 150.f)))
                          .addNode(fixedSizeLayoutNode(100.f, 100.f))
                            .addNode(backgroundColorNode(NiftyColor.yellow()))
                              //.addNode(transformationNode())
                                .addNode(contentNode())
                    .addAsChildOf("parent")
                      .addNode(absoluteLayoutNode())
                        .addNode(absoluteLayoutChildNode(NiftyPoint.newNiftyPoint(400.f, 400.f)))
                          .addNode(fixedSizeLayoutNode(60.f, 60.f))
                            .addNode(backgroundColorNode(NiftyColor.fuchsia()))
                              .addNode(transformationNode())
                                .addNode(contentNode());
    nifty.startAnimated(0, 25, new NiftyCallback<Float>() {
      private float rot = 0;

      @Override
      public void execute(final Float totalTime) {
        rot += 1.f;

        childTransformation.setAngleZ(rot);

        grandChildNodeTransformation.setAngleZ(rot * 10);

        rootTransformation.setScaleX((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
        rootTransformation.setScaleY((Math.sin(rot/50.f) + 1.0f) / 2.f + 0.25f);
        rootTransformation.setAngleZ(rot);
      }
    });
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a05_RotatingChildNode.class, args);
  }
}
