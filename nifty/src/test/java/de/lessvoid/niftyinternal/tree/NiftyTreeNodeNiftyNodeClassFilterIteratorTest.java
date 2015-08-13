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

import de.lessvoid.nifty.NiftyNodeString;
import de.lessvoid.nifty.spi.NiftyNode;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static de.lessvoid.nifty.NiftyNodeLongImpl.niftyNodeLongImpl;
import static de.lessvoid.nifty.NiftyNodeStringImpl.niftyNodeStringImpl;
import static org.junit.Assert.*;

/**
 * Created by void on 23.07.15.
 */
public class NiftyTreeNodeNiftyNodeClassFilterIteratorTest {
  @Test
  public void testIterateRoot() {
    NiftyTreeNode root = new NiftyTreeNode(niftyNodeLongImpl(26L));
    Iterator<NiftyNode> it = createIterator(root);

    assertFalse(it.hasNext());
    try {
      it.next();
      fail("expected exception");
    } catch (NoSuchElementException e) {
    }
  }

  @Test
  public void testIterateOneChild() {
    NiftyTreeNode root = new NiftyTreeNode(niftyNodeLongImpl(26L));
    NiftyTreeNode child = new NiftyTreeNode(niftyNodeStringImpl("child"));
    root.addChild(child);

    Iterator<NiftyNode> it = createIterator(root);
    assertTrue(it.hasNext());
    assertEquals("child", it.next().toString());

    assertFalse(it.hasNext());
    try {
      it.next();
      fail("expected exception");
    } catch (NoSuchElementException e) {
    }
  }

  private Iterator<NiftyNode> createIterator(final NiftyTreeNode root) {
    return
        new NiftyTreeNodeNiftyNodeIterator(
          new NiftyTreeNodeNiftyNodeClassFilterIterator<>(
              new NiftyTreeNodeDepthFirstIterator(root),
              NiftyNodeString.class));
  }

}