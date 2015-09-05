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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Wrapper iterator to return only NiftyTreeNodes which value class matches a given class.
 */
public class NiftyTreeNodeIterator<T> implements Iterator<T> {
  @Nonnull
  private final Iterator<NiftyTreeNode> it;
  @Nonnull
  private final NiftyTreeNodePredicate niftyTreeNodePredicate;
  @Nonnull
  private final NiftyTreeNodeConverter<T> niftyTreeNodeConverter;
  @Nullable
  private NiftyTreeNode cached;

  public NiftyTreeNodeIterator(
      @Nonnull final Iterator<NiftyTreeNode> it,
      @Nonnull final NiftyTreeNodePredicate niftyTreeNodePredicate,
      @Nonnull final NiftyTreeNodeConverter<T> niftyTreeNodeConverter) {
    this.it = it;
    this.niftyTreeNodePredicate = niftyTreeNodePredicate;
    this.niftyTreeNodeConverter = niftyTreeNodeConverter;
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
  public T next() {
    if (cached != null) {
      NiftyTreeNode result = cached;
      cached = null;
      return niftyTreeNodeConverter.convert(result.getValue());
    }
    NiftyTreeNode result = findNext();
    if (result == null) {
      throw new NoSuchElementException();
    }
    return niftyTreeNodeConverter.convert(result.getValue());
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  private NiftyTreeNode findNext() {
    while (it.hasNext()) {
      NiftyTreeNode next = it.next();
      if (niftyTreeNodePredicate.accept(next.getValue())) {
        return next;
      }
    }
    return null;
  }
}
