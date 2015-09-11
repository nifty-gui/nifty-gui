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
import de.lessvoid.nifty.NiftyNodeBuilder;
import de.lessvoid.nifty.node.Orientation;
import de.lessvoid.nifty.node.SimpleLayoutNode;
import de.lessvoid.nifty.types.NiftyColor;

import static de.lessvoid.nifty.node.AbsoluteLayoutChildNode.absoluteLayoutChildNode;
import static de.lessvoid.nifty.node.AbsoluteLayoutNode.absoluteLayoutNode;
import static de.lessvoid.nifty.node.FixedSizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundColorNode.backgroundColorNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SimpleLayoutNode.simpleLayoutNode;
import static de.lessvoid.nifty.node.UniformStackLayoutNode.uniformStackLayoutNode;

/**
 * A single root node that is only a quarter of the screen, placed in the upper right corner and contains two child
 * nodes using a horizontal child layout.
 *
 * @author void
 */
public class UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes {
  public UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes(final Nifty nifty) {
    NiftyNodeBuilder bigNode = nifty.addNode(absoluteLayoutNode())
        .addNode(absoluteLayoutChildNode())
        .addNode(fixedSizeLayoutNode(nifty.getScreenWidth() / 2.f, nifty.getScreenHeight() / 2.f))
        .addNode(uniformStackLayoutNode(Orientation.Vertical));

    bigNode.addNode(backgroundColorNode(NiftyColor.fromString("#ff0f")))
        .addNode(simpleLayoutNode())
        .addNode(contentNode())
    bigNode.addNode(backgroundColorNode(NiftyColor.fromString("#00ff")))
        .addNode(simpleLayoutNode())
        .addNode(contentNode());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes.class, args);
  }
}
