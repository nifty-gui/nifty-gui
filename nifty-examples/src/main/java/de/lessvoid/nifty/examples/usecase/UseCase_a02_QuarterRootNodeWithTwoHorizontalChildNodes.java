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
import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.UnitValue;

/**
 * A single root node that is only a quarter of the screen, placed in the upper right corner and contains two child
 * nodes using a horizontal child layout.
 *
 * @author void
 */
public class UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes {
/* FIXME
  public UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes(final Nifty nifty) {
    // By changing the rootNode horizontal alignment we move it to the right. This makes it appear in the upper right.
    NiftyNode rootNode = nifty.createRootNode(ChildLayout.Vertical, UnitValue.percent(50), UnitValue.percent(50), ChildLayout.Horizontal);
    rootNode.setHAlign(HAlign.right);

    // add two child nodes to the root node
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff08"));
    rootNode.newChildNode().setBackgroundColor(NiftyColor.fromString("#ff0f"));
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_a02_QuarterRootNodeWithTwoHorizontalChildNodes.class, args);
  }
  */
}
