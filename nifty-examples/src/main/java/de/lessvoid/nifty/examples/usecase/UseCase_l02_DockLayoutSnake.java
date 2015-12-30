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
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyDock;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static de.lessvoid.nifty.node.DockLayoutChildNode.dockLayoutChildNode;
import static de.lessvoid.nifty.node.DockLayoutNode.dockLayoutNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.NiftyBackgroundColorNode.backgroundColorNode;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.types.NiftyColor.*;
import static de.lessvoid.nifty.types.NiftySize.newNiftySize;

/**
 * This example demonstrates the capabilities of the dock layout. It is expected to build a snake like layout three
 * rounds around space that is available.
 *
 * <p><b>Expected result:</b></p>
 * <ul>
 *   <li>All bars are colored randomly.</li>
 *   <li>The bars at the top and the bottom have a height of 50 pixels.</li>
 *   <li>The bars at the left and the right side have a width of 50 pixels.</li>
 *   <li>The snake turns clockwise.</li>
 *   <li>The first bar is on the top and has the full width.</li>
 *   <li>The second bar is on the right side and has the full height minus the height of the first bar.</li>
 * </ul>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UseCase_l02_DockLayoutSnake {
  public UseCase_l02_DockLayoutSnake(@Nonnull final Nifty nifty) {
    NiftyNodeBuilder dockLayout = nifty.addNode(dockLayoutNode());

    List<NiftyDock> dockPositions = new ArrayList<>();
    dockPositions.add(NiftyDock.North);
    dockPositions.add(NiftyDock.East);
    dockPositions.add(NiftyDock.South);
    dockPositions.add(NiftyDock.West);

    NiftySize topBottomSize = newNiftySize(0.f, 50.f);
    NiftySize leftRightSize = newNiftySize(50.f, 0.f);

    Queue<NiftyColor> colors = new LinkedList<>();
    colors.add(red());
    colors.add(green());
    colors.add(blue());
    colors.add(yellow());

    colors.add(aqua());
    colors.add(maroon());
    colors.add(navy());
    colors.add(fuchsia());

    colors.add(purple());
    colors.add(silver());
    colors.add(teal());
    colors.add(white());

    for (int i = 0; i < 3; i++) {
      for (NiftyDock dock : dockPositions) {
        NiftySize usedSize = ((dock == NiftyDock.North) || (dock == NiftyDock.South)) ? topBottomSize : leftRightSize;

        dockLayout.addNode(dockLayoutChildNode(dock))
            .addNode(fixedSizeLayoutNode(usedSize))
            .addNode(backgroundColorNode(colors.isEmpty() ? randomColor() : colors.poll()))
            .addNode(contentNode());
      }
    }
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_l02_DockLayoutSnake.class, args);
  }
}
