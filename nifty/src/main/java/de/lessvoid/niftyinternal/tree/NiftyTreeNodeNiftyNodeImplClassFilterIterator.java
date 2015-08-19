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

import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wrapper iterator to return only NiftyTreeNodes which value class matches a given class.
 */
public class NiftyTreeNodeNiftyNodeImplClassFilterIterator<T extends NiftyNodeImpl> implements Iterator<NiftyTreeNode> {
  private final Iterator<NiftyTreeNode> it;
  private final Class<T> clazz;
  private NiftyTreeNode cached;

  public NiftyTreeNodeNiftyNodeImplClassFilterIterator(final Iterator<NiftyTreeNode> it, final Class<T> clazz) {
    this.it = it;
    this.clazz = clazz;
  }

  @Override
  public boolean hasNext() {
    if (cached != null) {
      return true;
    }
    cached = findNext();
    return cached != null;
  }

  @Override
  public NiftyTreeNode next() {
    if (cached != null) {
      NiftyTreeNode result = cached;
      cached = null;
      return result;
    }
    NiftyTreeNode result = findNext();
    if (result == null) {
      throw new NoSuchElementException();
    }
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  private NiftyTreeNode findNext() {
    while (it.hasNext()) {
      NiftyTreeNode next = it.next();
      NiftyNodeImpl value = next.getValue();
      if (clazz.isAssignableFrom(value.getClass())) {
        return next;
      }
    }
    return null;
  }
}
