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

package de.lessvoid.nifty;

import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyLayoutReceiver;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;
import de.lessvoid.niftyinternal.tree.InternalNiftyTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;

import static de.lessvoid.nifty.types.NiftyPoint.ZERO;
import static de.lessvoid.nifty.types.NiftyRect.newNiftyRect;
import static de.lessvoid.nifty.types.NiftySize.newNiftySize;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeControls.downToFirstInstance;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeControls.onlyOneLevel;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNodeImpl;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNodeImplClass;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplAny;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplClass;

/**
 * This is the class that supports the layout nodes to do their work. It is the primary entry point to trigger the
 * layout process.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class NiftyLayout {
  @Nonnull
  private final static Logger logger = Logger.getLogger(NiftyLayout.class.getName());

  /**
   * The nodes that where reported to have a invalid measure.
   */
  @Nonnull
  private final Queue<NiftyLayoutNodeImpl<? extends NiftyNode>> invalidMeasureReports;

  /**
   * The nodes that were reported to have a invalid arrangement.
   */
  @Nonnull
  private final List<NiftyLayoutNodeImpl<? extends NiftyNode>> invalidArrangeReports;

  /**
   * This list stores the changed arrangements that were not published yet.
   */
  @Nonnull
  private final Queue<NiftyLayoutNodeImpl<? extends NiftyNode>> unpublishedArrangements;

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
    unpublishedArrangements = new LinkedList<>();
    this.nifty = nifty;
    nodeTree = tree;
  }


  @Nonnull
  public Nifty getNifty() {
    return nifty;
  }

  /**
   * Report the removal of a specific node. This causes the parent node to require a recalculation.
   *
   * @param node the node that was removed
   */
  public void reportRemoval(@Nonnull final NiftyLayoutNodeImpl<? extends NiftyNode> node) {
    NiftyLayoutNodeImpl<?> parentNode = nodeTree.getParent(NiftyLayoutNodeImpl.class, node);
    if (parentNode != null) {
      if (parentNode.isMeasureValid()) {
        reportMeasureInvalid(parentNode);
      }
      if (parentNode.isArrangeValid()) {
        reportArrangeInvalid(parentNode);
      }
    }
  }

  /**
   * Report the measure of the specific node as invalid. The layout master will mark the measure value of every parent
   * layout node as invalid during the next update of the layout.
   *
   * @param node the node that got a invalid measure
   */
  public void reportMeasureInvalid(@Nonnull final NiftyLayoutNodeImpl<? extends NiftyNode> node) {
    invalidMeasureReports.add(node);
  }

  /**
   * Report the arrangement of the specific node as invalid. The arranging will be triggered for the highest leveled
   * nodes that are marked invalid in the tree to make the rearrange their sub-tree.
   *
   * @param node the node that got a invalid child arrangement
   */
  public void reportArrangeInvalid(@Nonnull final NiftyLayoutNodeImpl<? extends NiftyNode> node) {
    invalidArrangeReports.add(node);
  }

  /**
   * Report that the arrangement of the node was changed and needs to be published to the receivers.
   *
   * @param node the node with the new arrangement
   */
  public void reportChangedArrangement(@Nonnull final NiftyLayoutNodeImpl<? extends NiftyNode> node) {
    unpublishedArrangements.add(node);
  }

  /**
   * This function triggers the entire layout process.
   */
  void update() {
    measure();
    arrange();
    publishArrangements();
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

    @Nullable NiftyLayoutNodeImpl<?> currentLayoutNode;
    while ((currentLayoutNode = invalidMeasureReports.poll()) != null) {
      NiftyLayoutNodeImpl parentNode = nodeTree.getParent(NiftyLayoutNodeImpl.class, currentLayoutNode);
      if (parentNode != null && parentNode.isMeasureValid()) {
        parentNode.invalidateMeasure();
      }
    }

    NiftySize size = newNiftySize(nifty.getScreenWidth(), nifty.getScreenHeight());
    measure(nodeTree.getRootNode(), size);
  }

  /**
   * Measure the node that is set as parameter or the first layout need in the tree below.
   *
   * @param node the node that is the root for this operation
   * @param availableSize the available size that is forwarded to {@link NiftyLayoutNodeImpl#measure(NiftySize)}
   * @return size returned by the {@link NiftyLayoutNodeImpl#measure(NiftySize)} function that is called for the node(s)
   */
  @Nonnull
  public NiftySize measure(@Nonnull final NiftyNodeImpl<? extends NiftyNode> node, @Nonnull final NiftySize availableSize) {
    if (node instanceof NiftyLayoutNodeImpl<?>) {
      NiftySize desiredSize =  ((NiftyLayoutNodeImpl<?>) node).measure(availableSize);
      if (desiredSize.isInfinite() || desiredSize.isInvalid()) {
        logger.severe("Measuring node " + node.toString() + " gave a illegal result. Size must be finite.");
        desiredSize = NiftySize.ZERO;
      }
      return desiredSize;
    }

    NiftySize currentSize = NiftySize.ZERO;
    for (NiftyLayoutNodeImpl<?> layoutNode : getChildLayoutNodes(node)) {
      NiftySize nodeSize = measure(layoutNode, availableSize);
      currentSize = NiftySize.max(nodeSize, currentSize);
    }
    return currentSize;
  }

  @Nonnull
  public NiftySize getDesiredSize(@Nonnull final NiftyNodeImpl<? extends NiftyNode> node) {
    if (node instanceof NiftyLayoutNodeImpl) {
      return ((NiftyLayoutNodeImpl) node).getDesiredSize();
    } else {
      NiftySize currentSize = NiftySize.ZERO;
      for (NiftyLayoutNodeImpl<?> layoutNode : getChildLayoutNodes(node)) {
        NiftySize nodeSize = layoutNode.getDesiredSize();
        currentSize = NiftySize.max(nodeSize, currentSize);
      }
      return currentSize;
    }
  }

  private void arrange() {
    if (invalidArrangeReports.isEmpty()) {
      return; // There are no dirty arrange entries reported. So we are done here.
    }

    for (NiftyLayoutNodeImpl<?> layoutNode : getChildLayoutNodes()) {

      layoutNode.arrange(newNiftyRect(ZERO, newNiftySize(nifty.getScreenWidth(), nifty.getScreenHeight())));
      removeArranged();
      if (invalidArrangeReports.isEmpty()) {
        return; // Early exit in case we are done.
      }
    }
  }

  public void arrange(@Nonnull final NiftyNodeImpl<? extends NiftyNode> node, @Nonnull final NiftyRect area) {
    if (node instanceof NiftyLayoutNodeImpl) {
      ((NiftyLayoutNodeImpl) node).arrange(area);
    } else {
      for (NiftyLayoutNodeImpl<?> layoutNode : getChildLayoutNodes(node)) {
        layoutNode.arrange(area);
      }
    }
  }

  private void removeArranged() {
    Iterator<NiftyLayoutNodeImpl<?>> itr = invalidArrangeReports.iterator();
    while (itr.hasNext()) {
      if (itr.next().isArrangeValid()) {
        itr.remove();
      }
    }
  }

  private void publishArrangements() {
    if (unpublishedArrangements.isEmpty()) {
      return;
    }

    NiftyLayoutNodeImpl<?> unpublishedNode;
    while ((unpublishedNode = unpublishedArrangements.poll()) != null) {
      NiftyRect arrangementRect = unpublishedNode.getArrangedRect();

      for (NiftyLayoutReceiver<?> layoutReceiver : nodeTree.childNodes(nodeImplClass(NiftyLayoutReceiver.class),
          toNodeImplClass(NiftyLayoutReceiver.class), downToFirstInstance(NiftyLayoutNodeImpl.class),
          unpublishedNode)) {
        layoutReceiver.setLayoutResult(arrangementRect);
      }
    }
  }

  @Nonnull
  public List<NiftyNodeImpl<? extends NiftyNode>> getDirectChildren(
      @Nonnull final NiftyLayoutNodeImpl<? extends NiftyNode> node) {
    Iterable<NiftyNodeImpl<?>> itr = nodeTree.childNodes(nodeImplAny(), toNodeImpl(), onlyOneLevel(), node);
    List<NiftyNodeImpl<?>> list = new LinkedList<>();
    for (NiftyNodeImpl<?> impl : itr) {
      list.add(impl);
    }
    return list;
  }

  public Iterable<NiftyLayoutNodeImpl> getChildLayoutNodes() {
    return nodeTree.childNodes(
        nodeImplClass(NiftyLayoutNodeImpl.class),
        toNodeImplClass(NiftyLayoutNodeImpl.class),
        downToFirstInstance(NiftyLayoutNodeImpl.class));
  }

  public Iterable<NiftyLayoutNodeImpl> getChildLayoutNodes(@Nonnull final NiftyNodeImpl<? extends NiftyNode> node) {
    return nodeTree.childNodes(
        nodeImplClass(NiftyLayoutNodeImpl.class),
        toNodeImplClass(NiftyLayoutNodeImpl.class),
        downToFirstInstance(NiftyLayoutNodeImpl.class),
        node);
  }
}
