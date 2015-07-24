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

import de.lessvoid.nifty.api.NiftyTree;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by void on 23.07.15.
 */
public class NiftyTreeNodeDepthFirstIteratorTest {

  @Test
  public void testIterateRoot() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    Iterator<NiftyTreeNode<String>> it = createIterator(root);
    assertTrue(it.hasNext());
    assertEquals("root", it.next().getValue());

    assertFalse(it.hasNext());
    try {
      it.next();
      fail("expected exception");
    } catch (NoSuchElementException e) {
    }
  }

  @Test
  public void testIterateOneChild() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child = new NiftyTreeNode<>("child");
    root.addChild(child);

    Iterator<NiftyTreeNode<String>> it = createIterator(root);
    assertTrue(it.hasNext());
    assertEquals("root", it.next().getValue());

    assertTrue(it.hasNext());
    assertEquals("child", it.next().getValue());

    assertFalse(it.hasNext());
    try {
      it.next();
      fail("expected exception");
    } catch (NoSuchElementException e) {
    }
  }

  private Iterator<NiftyTreeNode<String>> createIterator(final NiftyTreeNode<String> root) {
    return new NiftyTreeNodeDepthFirstIterator<>(root);
  }

}