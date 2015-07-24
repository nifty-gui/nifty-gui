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

package de.lessvoid.nifty.api;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the class that supports the layout nodes to do their work. It is the primary entry point to trigger the
 * layout process.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class NiftyLayout {
  /**
   * The nodes that where reported to have a invalid measure.
   */
  @Nonnull
  private final Queue<NiftyLayoutNode> invalidMeasureReports;

  /**
   * The nodes that were reported to have a invalid arrangement.
   */
  @Nonnull
  private final Queue<NiftyLayoutNode> invalidArrangeReports;

  NiftyLayout() {
    invalidMeasureReports = new LinkedList<>();
    invalidArrangeReports = new LinkedList<>();
  }

  /**
   * Report the measure of the specific node as invalid. The layout master will mark the measure value of every parent
   * layout node as invalid during the next update of the layout.
   *
   * @param node the node that got a invalid measure
   */
  public void reportMeasureInvalid(@Nonnull final NiftyLayoutNode node) {
    invalidMeasureReports.add(node);
  }

  /**
   * Report the arrangement of the specific node as invalid. The arranging will be triggered for the highest leveled
   * nodes that are marked invalid in the tree to make the rearrange their sub-tree.
   *
   * @param node the node that got a invalid child arrangement
   */
  public void reportArrageInvalid(@Nonnull final NiftyLayoutNode node) {
    invalidArrangeReports.add(node);
  }

  /**
   * This function triggers the entire layout process.
   */
  void update() {

  }
}
