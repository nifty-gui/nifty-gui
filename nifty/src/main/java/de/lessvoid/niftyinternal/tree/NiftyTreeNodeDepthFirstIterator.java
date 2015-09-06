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
package de.lessvoid.niftyinternal.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Depth-first, on tree elements
 *
 * @author jkee
 */
public class NiftyTreeNodeDepthFirstIterator implements Iterator<NiftyTreeNode> {
  @Nonnull
  private final Stack<Integer> stack;
  @Nullable
  private NiftyTreeNode current;
  @Nonnull
  private final NiftyTreeNodeControl control;

  public NiftyTreeNodeDepthFirstIterator(@Nullable final NiftyTreeNode tree,
                                         @Nonnull final NiftyTreeNodeControl control) {
    stack = new Stack<>();
    this.control = control;

    /* Select the first element of the first child node. So the root node is not returned. */
    current = null;
    if (tree != null) {
      List<NiftyTreeNode> children = tree.getChildren();
      if (!children.isEmpty()) {
        stack.push(0);
        current = children.get(0);
      }
    }
  }

  @Override
  public boolean hasNext() {
    return current != null;
  }

  @Override
  public NiftyTreeNode next() {
    if (current == null) throw new NoSuchElementException();
    NiftyTreeNode toReturn = current;

    List<NiftyTreeNode> children = null;
    int currentDepth = stack.size();
    int currentIndex = (currentDepth == 0 ? 0 : stack.peek());

    switch (control.visitNode(toReturn.getValue(), currentDepth, currentIndex)) {
      case Terminate: // Terminate the iteration
        current = null;
        stack.clear();
        break;
      case Continue: // continue normal
        children = current.getChildren();
        break;
      case SkipSubtree: // skip the entire tree below that node if there is any.
        children = null;
        break;
      case SkipSiblings: // continue the sub tree, but skip the remaining sub tree
        if (!stack.empty()) {
          // maximal value in the stack will ensure that any following sibling will leave.
          stack.pop();
          stack.push(Integer.MAX_VALUE);
        }
        children = current.getChildren();
        break;
    }

    if (children != null && !children.isEmpty()) {
      //starting next level
      NiftyTreeNode firstChild = children.get(0);
      stack.push(0);
      current = firstChild;
    } else {
      //moving up
      NiftyTreeNode localCurrent = current;
      while (!stack.empty()) {
        NiftyTreeNode parent = localCurrent.getParent();
        Integer parentIndex = stack.pop();
        int nextIndex = parentIndex + 1;
        if (nextIndex < parent.getChildren().size()) {
          stack.push(nextIndex);
          current = parent.getChildren().get(nextIndex);
          return toReturn;
        }
        localCurrent = parent;
      }
      current = null;
    }
    return toReturn;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
