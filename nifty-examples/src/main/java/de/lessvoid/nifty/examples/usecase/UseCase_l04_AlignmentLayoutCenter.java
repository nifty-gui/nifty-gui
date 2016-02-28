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
import de.lessvoid.nifty.types.NiftyColor;

import static de.lessvoid.nifty.node.AlignmentLayoutChildNode.alignmentLayoutChildNode;
import static de.lessvoid.nifty.node.AlignmentLayoutNode.alignmentLayoutNode;
import static de.lessvoid.nifty.node.Horizontal.Center;
import static de.lessvoid.nifty.node.NiftyBackgroundFillNode.backgroundFillColor;
import static de.lessvoid.nifty.node.NiftyContentNode.contentNode;
import static de.lessvoid.nifty.node.SizeLayoutNode.fixedSizeLayoutNode;
import static de.lessvoid.nifty.node.Vertical.Middle;

/**
 * This is a simple example of the alignment layout node.
 * <p />
 * This example is expected to show a red rectangle (100px width, 60px height) in the center of the screen.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UseCase_l04_AlignmentLayoutCenter {
  public UseCase_l04_AlignmentLayoutCenter(final Nifty nifty) {
    nifty
        .addNode(alignmentLayoutNode())
          .addNode(alignmentLayoutChildNode(Center, Middle))
            .addNode(fixedSizeLayoutNode(100.f, 50.f))
              .addNode(backgroundFillColor(NiftyColor.red()))
                .addNode(contentNode());
  }

  public static void main(final String[] args) throws Exception {
    UseCaseRunner.run(UseCase_l04_AlignmentLayoutCenter.class, args);
  }
}
