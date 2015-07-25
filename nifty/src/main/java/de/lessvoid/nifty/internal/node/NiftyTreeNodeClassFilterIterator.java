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
import java.util.NoSuchElementException;

/**
 * Wrapper iterator to return only NiftyTreeNodes which value class matches a given class.
 */
public class NiftyTreeNodeClassFilterIterator<T, X> implements Iterator<NiftyTreeNode<X>> {
  private final Iterator<NiftyTreeNode<T>> it;
  private final Class<X> clazz;
  private NiftyTreeNode<X> cached;

  public NiftyTreeNodeClassFilterIterator(final Iterator<NiftyTreeNode<T>> it, final Class<X> clazz) {
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
  public NiftyTreeNode<X> next() {
    if (cached != null) {
      NiftyTreeNode<X> result = cached;
      cached = null;
      return result;
    }
    NiftyTreeNode<X> result = findNext();
    if (result == null) {
      throw new NoSuchElementException();
    }
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  private NiftyTreeNode<X> findNext() {
    while (it.hasNext()) {
      NiftyTreeNode<T> next = it.next();
      T value = next.getValue();
      if (clazz.isAssignableFrom(value.getClass())) {
        return (NiftyTreeNode<X>) next;
      }
    }
    return null;
  }
}
