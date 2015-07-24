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

import de.lessvoid.nifty.api.node.*;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.api.types.Size;
import de.lessvoid.nifty.internal.InternalNiftyTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
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

  /**
   * The node tree that is used to locate parent and children nodes.
   * <p />
   * TODO: Verify if the internal implementation for the public implementation should be used
   */
  @Nonnull
  private final InternalNiftyTree nodeTree;

  /**
   * Nifty... everyone needs some.
   */
  @Nonnull
  private final Nifty nifty;

  NiftyLayout(@Nonnull final Nifty nifty, @Nonnull final InternalNiftyTree tree) {
    invalidMeasureReports = new LinkedList<>();
    invalidArrangeReports = new LinkedList<>();
    this.nifty = nifty;
    nodeTree = tree;
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
    updateInvalidMeasureFlags();
  }

  /**
   * Spread the invalid measure flags inside the tree.
   */
  private void updateInvalidMeasureFlags() {
    @Nullable NiftyLayoutNode currentLayoutNode;
    while ((currentLayoutNode = invalidMeasureReports.poll()) != null) {
      NiftyLayoutNode parentNode = nodeTree.getParent(NiftyLayoutNode.class, currentLayoutNode);
      if (parentNode != null && parentNode.isMeasureValid()) {
        parentNode.invalidateMeasure();
      }
    }
  }

  private void measure() {
    /*
     TODO:
     It needs to be verified if that is a acceptable way to get the size of the root node or if anything should
     be done differently.
    */
    Size size = new Size(nifty.getScreenWidth(), nifty.getScreenHeight());
    de.lessvoid.nifty.api.node.NiftyNode rootNode = nodeTree.getRootNode();
    if (rootNode instanceof NiftyLayoutNode) {
      measure((NiftyLayoutNode) rootNode, size);
    } else {
      Iterator<de.lessvoid.nifty.api.node.NiftyNode> itr = nodeTree.filteredChildIterator(NiftyLayoutNode.class);
      while (itr.hasNext()) {
        NiftyNode node = itr.next();
        if (node instanceof NiftyLayoutNode) {
          measure((NiftyLayoutNode) node, size);
        }
      }
    }
  }

  private void measure(@Nonnull final NiftyLayoutNode node, @Nonnull final Size size) {
    if (!node.isMeasureValid()) {
      node.measure(size);
    }
  }
}
