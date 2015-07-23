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
package de.lessvoid.nifty.internal.node;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Depth-first, on tree elements
 *
 * @author jkee
 */
public class NiftyTreeNodeDepthFirstIterator<T> implements Iterator<NiftyTreeNode<T>> {
  private final Stack<Integer> stack = new Stack<>();
  private NiftyTreeNode<T> current;

  public NiftyTreeNodeDepthFirstIterator(final NiftyTreeNode<T> tree) {
    current = tree;
  }

  @Override
  public boolean hasNext() {
    return current != null;
  }

  @Override
  public NiftyTreeNode<T> next() {
    if (current == null) throw new NoSuchElementException();
    NiftyTreeNode<T> toReturn = current;
    List<NiftyTreeNode<T>> children = current.getChildren();
    if (children != null && !children.isEmpty()) {
      //starting next level
      NiftyTreeNode<T> firstChild = children.get(0);
      stack.push(0);
      current = firstChild;
    } else {
      //moving up
      NiftyTreeNode<T> localCurrent = current;
      while (!stack.empty()) {
        NiftyTreeNode<T> parent = localCurrent.getParent();
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
