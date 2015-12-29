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
import de.lessvoid.nifty.node.NiftyReferenceNode;
import de.lessvoid.nifty.types.NiftyColor;

import static de.lessvoid.nifty.node.DockLayoutChildNode.*;
import static de.lessvoid.nifty.node.DockLayoutNode.dockLayoutNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundColorNode.backgroundColorNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SimpleLayoutNode.simpleLayoutNode;

/**
 * This example demonstrates the capabilities of the dock layout with a centered central node.
 *
 * <p><b>Expected result:</b></p>
 * <ul>
 *   <li>A blue bar on the top with the full width and 50 pixels height</li>
 *   <li>A green bar on the bottom with the full width and 50 pixels height</li>
 *   <li>A yellow bar on the left side width full height minus the height of the top and bottom bar and with a
 *   width of 50 pixels.</li>
 *   <li>A red bar on the right side width full height minus the height of the top and bottom bar and with a
 *   width of 50 pixels.</li>
 *   <li>The center is covered by a white node.</li>
 * </ul>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class UseCase_l03_DockLayoutCenter {

  public UseCase_l03_DockLayoutCenter(final Nifty nifty) {
    nifty
        .addNode(dockLayoutNode(true))
          .addNode(NiftyReferenceNode.referenceNode("dockNode"))
            .addNode(dockNorthLayoutNode())
              .addNode(fixedSizeLayoutNode(0.f, 50.f))
                .addNode(backgroundColorNode(NiftyColor.blue()))
                  .addNode(contentNode())
          .addAsChildOf("dockNode")
            .addNode(dockSouthLayoutNode())
              .addNode(fixedSizeLayoutNode(0.f, 50.f))
                .addNode(backgroundColorNode(NiftyColor.green()))
                  .addNode(contentNode())
          .addAsChildOf("dockNode")
            .addNode(dockWestLayoutNode())
              .addNode(fixedSizeLayoutNode(50.f, 0.f))
                .addNode(backgroundColorNode(NiftyColor.yellow()))
                  .addNode(contentNode())
          .addAsChildOf("dockNode")
            .addNode(dockEastLayoutNode())
              .addNode(fixedSizeLayoutNode(50.f, 0.f))
                .addNode(backgroundColorNode(NiftyColor.red()))
                  .addNode(contentNode())
          .addAsChildOf("dockNode")
            .addNode(simpleLayoutNode())
              .addNode(backgroundColorNode(NiftyColor.white()))
                .addNode(contentNode());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_l03_DockLayoutCenter.class, args);
  }
}
