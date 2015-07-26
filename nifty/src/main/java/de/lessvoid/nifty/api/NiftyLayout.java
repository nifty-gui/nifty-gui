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

import de.lessvoid.nifty.api.node.NiftyLayoutNode;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.api.types.Rect;
import de.lessvoid.nifty.api.types.Size;
import de.lessvoid.nifty.internal.InternalNiftyTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;

/**
 * This is the class that supports the layout nodes to do their work. It is the primary entry point to trigger the
 * layout process.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class NiftyLayout {
  @Nonnull
  private final static Logger logger = Logger.getLogger(NiftyLayout.class.getName());

  /**
   * The nodes that where reported to have a invalid measure.
   */
  @Nonnull
  private final Queue<NiftyLayoutNode> invalidMeasureReports;

  /**
   * The nodes that were reported to have a invalid arrangement.
   */
  @Nonnull
  private final List<NiftyLayoutNode> invalidArrangeReports;

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
  public void reportArrangeInvalid(@Nonnull final NiftyLayoutNode node) {
    invalidArrangeReports.add(node);
  }

  /**
   * This function triggers the entire layout process.
   */
  void update() {
    measure();
    arrange();
  }

  /**
   * Perform the measuring operation as part of the update loop of the Nifty-Layout.
   * <p />
   * This function invalidates the measure values if required and performs the measure operation itself.
   */
  private void measure() {
    if (invalidMeasureReports.isEmpty()) {
      /* No reported dirty measure values. We are all good! */
      return;
    }

    @Nullable NiftyLayoutNode currentLayoutNode;
    while ((currentLayoutNode = invalidMeasureReports.poll()) != null) {
      NiftyLayoutNode parentNode = nodeTree.getParent(NiftyLayoutNode.class, currentLayoutNode);
      if (parentNode != null && parentNode.isMeasureValid()) {
        parentNode.invalidateMeasure();
      }
    }

    Size size = new Size(nifty.getScreenWidth(), nifty.getScreenHeight());
    measure(nodeTree.getRootNode(), size);
  }

  /**
   * Measure the node that is set as parameter or the first layout need in the tree below.
   *
   * @param node the node that is the root for this operation
   * @param availableSize the available size that is forwarded to {@link NiftyLayoutNode#measure(Size)}
   * @return size returned by the {@link NiftyLayoutNode#measure(Size)} function that is called for the node(s)
   */
  @Nonnull
  public Size measure(@Nonnull final NiftyNode node, @Nonnull final Size availableSize) {
    if (node instanceof NiftyLayoutNode) {
      return measure((NiftyLayoutNode) node, availableSize);
    } else {
      Size currentSize = Size.ZERO;
      for (NiftyLayoutNode childNode : nodeTree.filteredChildNodes(NiftyLayoutNode.class, node)) {
        Size nodeSize = measure(childNode, availableSize);
        currentSize = Size.max(nodeSize, currentSize);
      }
      return currentSize;
    }
  }

  /**
   * Measure a specific node.
   * <p />
   * This function performs some additional runtime checks to verify the return value of the
   * {@link NiftyLayoutNode#measure(Size) function.
   *
   * @param node the node that is measured
   * @param availableSize the available size that is forwarded to {@link NiftyLayoutNode#measure(Size)}
   * @return the result of the measure operation.
   */
  @Nonnull
  public Size measure(@Nonnull final NiftyLayoutNode node, @Nonnull final Size availableSize) {
    Size desiredSize = node.measure(availableSize);

    if (desiredSize.isInfinite() || desiredSize.isInvalid()) {
      logger.severe("Measuring node " + node.toString() + " gave a illegal result. Size must be finite.");
      desiredSize = Size.ZERO;
    }
    return desiredSize;
  }

  @Nonnull
  public Size getDesiredSize(@Nonnull final NiftyNode node) {
    if (node instanceof NiftyLayoutNode) {
      return ((NiftyLayoutNode) node).getDesiredSize();
    } else {
      Size currentSize = Size.ZERO;
      for (NiftyLayoutNode childNode : nodeTree.filteredChildNodes(NiftyLayoutNode.class, node)) {
        Size nodeSize = childNode.getDesiredSize();
        currentSize = Size.max(nodeSize, currentSize);
      }
      return currentSize;
    }
  }

  private void arrange() {
    if (invalidArrangeReports.isEmpty()) {
      return; // There are no dirty arrange entries reported. So we are done here.
    }

    for (NiftyLayoutNode layoutNode : nodeTree.filteredChildNodes(NiftyLayoutNode.class)) {
      if (layoutNode.isArrangeValid()) {
        layoutNode.arrange(layoutNode.getArrangedRect());
        removeArranged();
        if (invalidArrangeReports.isEmpty()) {
          return; // Early exit in case we are done.
        }
      }
    }
  }

  public void arrange(@Nonnull final NiftyNode node, @Nonnull final Rect area) {
    if (node instanceof NiftyLayoutNode) {
      arrange((NiftyLayoutNode) node, area);
    } else {
      for (NiftyLayoutNode layoutNode : nodeTree.filteredChildNodes(NiftyLayoutNode.class, node)) {
        layoutNode.arrange(layoutNode.getArrangedRect());
      }
    }
  }

  public void arrange(@Nonnull final NiftyLayoutNode node, @Nonnull final Rect area) {
    node.arrange(area);
  }

  private void removeArranged() {
    Iterator<NiftyLayoutNode> itr = invalidArrangeReports.iterator();
    while (itr.hasNext()) {
      if (itr.next().isArrangeValid()) {
        itr.remove();
      }
    }
  }

  @Nonnull
  public List<NiftyNode> getDirectChildren(@Nonnull final NiftyLayoutNode node) {
    /* TODO: The actual implementation. */
    return Collections.emptyList();
  }
}
