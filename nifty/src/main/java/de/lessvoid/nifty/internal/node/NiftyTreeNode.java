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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NiftyTreeNode is the base of a NiftyTree.
 *
 * Heavily based on https://github.com/jkee/gtree
 *
 * Created by void on 21.07.15.
 */
public class NiftyTreeNode<T> {
  private final T value;
  private NiftyTreeNode<T> parent;
  private List<NiftyTreeNode<T>> children;

  public NiftyTreeNode(final T value) {
    this.value = value;
  }

  public List<NiftyTreeNode<T>> getChildren() {
    return children;
  }

  public T getValue() {
    return value;
  }

  public NiftyTreeNode<T> getParent() {
    return parent;
  }

  public void addChild(final NiftyTreeNode<T> child) {
    if (children == null) {
      children = new ArrayList<>();
    }
    children.add(child);
    child.setParent(this);
  }

  public void setParent(final NiftyTreeNode<T> parent) {
    this.parent = parent;
  }

  public Iterator<NiftyTreeNode<T>> treeIterator() {
    return new NiftyTreeNodeDepthFirstIterator<>(this);
  }

  public Iterator<T> valueIterator() {
    return new NiftyTreeNodeValueIterator<>(new NiftyTreeNodeDepthFirstIterator<>(this));
  }

  public <X> Iterator<X> filteredChildIterator(final Class<X> clazz) {
    return new NiftyTreeNodeValueIterator<>(new NiftyTreeNodeClassFilterIterator<>(treeIterator(), clazz));
  }

  public void remove() {
    if (parent == null) {
      return;
    }
    parent.children.remove(this);
    if (parent.children.isEmpty()) {
      parent.children = null;
    }
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        parent.addChild(children.get(i));
      }
    }
    parent = null;
  }

  public void remove(final NiftyTreeNode<T> child) {
    children.remove(child);
    if (children.isEmpty()) {
      children = null;
    }
    child.setParent(null);
  }

  @Override
  public String toString() {
    return "node{" + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyTreeNode<?> that = (NiftyTreeNode<?>) o;

    if (value != null ? !value.equals(that.value) : that.value != null) return false;
    return !(children != null ? !children.equals(that.children) : that.children != null);
  }

  @Override
  public int hashCode() {
    int result = value != null ? value.hashCode() : 0;
    result = 31 * result + (children != null ? children.hashCode() : 0);
    return result;
  }

  public String toStringTree() {
    StringBuilder stringBuilder = new StringBuilder();
    append(stringBuilder, 0);
    return stringBuilder.toString();
  }

  private void append(final StringBuilder stringBuilder, final int depth) {
    if (depth != 0) {
      stringBuilder.append("\n");
    }
    for (int i = 0; i < depth; i++) {
      stringBuilder.append("  ");
    }
    stringBuilder.append(value);
    if (children == null) {
      return;
    }
    for (NiftyTreeNode<T> child : children) {
      child.append(stringBuilder, depth + 1);
    }
  }
}
