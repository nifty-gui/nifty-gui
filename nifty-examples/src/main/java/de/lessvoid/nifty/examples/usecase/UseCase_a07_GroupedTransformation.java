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
import de.lessvoid.nifty.NiftyConfiguration;
import de.lessvoid.nifty.node.AbsoluteLayoutChildNode;
import de.lessvoid.nifty.node.AbsoluteLayoutNode;
import de.lessvoid.nifty.node.Horizontal;
import de.lessvoid.nifty.node.NiftyTransformationNode;
import de.lessvoid.nifty.node.Vertical;
import de.lessvoid.nifty.types.NiftyColor;

import java.io.IOException;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.NiftyReferenceNode.referenceNode;
import static de.lessvoid.nifty.node.NiftyTransformationNode.transformationNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;

/**
 * A root node with four child nodes and a transformation applied to all of them equally.
 * @author void
 */
public class UseCase_a07_GroupedTransformation {

  public UseCase_a07_GroupedTransformation(final Nifty nifty) throws IOException {
    final NiftyTransformationNode transformationNode = transformationNode();
    transformationNode.setPivotX(0.5);
    transformationNode.setPivotY(0.5);

    nifty
        .addNode(alignmentLayoutNode())
          .addNode(alignmentLayoutChildNode(Horizontal.Center, Vertical.Middle))
            .addNode(fixedSizeLayoutNode(100.f, 100.f))
              .addNode(AbsoluteLayoutNode.absoluteLayoutNode())
                .addNode(transformationNode)
                  .addNode(referenceNode("parent"))
                    .addNode(AbsoluteLayoutChildNode.absoluteLayoutChildNode(0.f, 0.f))
                      .addNode(fixedSizeLayoutNode(50.f, 50.f))
                        .addNode(backgroundFillColor(NiftyColor.green()))
                          .addNode(contentNode())
                  .addAsChildOf("parent")
                    .addNode(AbsoluteLayoutChildNode.absoluteLayoutChildNode(50.f, 0.f))
                      .addNode(fixedSizeLayoutNode(50.f, 50.f))
                        .addNode(backgroundFillColor(NiftyColor.blue()))
                          .addNode(contentNode())
                  .addAsChildOf("parent")
                    .addNode(AbsoluteLayoutChildNode.absoluteLayoutChildNode(0.f, 50.f))
                      .addNode(fixedSizeLayoutNode(50.f, 50.f))
                        .addNode(backgroundFillColor(NiftyColor.red()))
                          .addNode(contentNode())
                  .addAsChildOf("parent")
                    .addNode(AbsoluteLayoutChildNode.absoluteLayoutChildNode(50.f, 50.f))
                      .addNode(fixedSizeLayoutNode(50.f, 50.f))
                        .addNode(backgroundFillColor(NiftyColor.purple()))
                          .addNode(contentNode());
    nifty.startAnimated(0, 10, new NiftyCallback<Float>() {
      float rot = 0;
      @Override
      public void execute(Float aFloat) {
        rot += 1;
        transformationNode.setAngleZ(rot);
        transformationNode.setScaleX(Math.sin(rot/100.f) + 2).setScaleY(Math.sin(rot/100.f) + 2);
      }
    });
  }

  private UseCase_a07_GroupedTransformation() {
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a07_GroupedTransformation.class, args, new NiftyConfiguration().clearScreen(true));
  }
}
