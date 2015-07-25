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
package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.node.NiftyNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by void on 23.07.15.
 */
public class InternalNiftyTreeTest {
  private InternalNiftyTree tree;

  @Test
  public void testGetRootNode() {
    tree = new InternalNiftyTree(node("root"));
    assertEquals(node("root"), tree.getRootNode());
  }

  @Test
  public void testAddSingleChildToRoot() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("child"));
    assertTree(
        "root\n" +
        "  child");
  }

  @Test
  public void testAddMultipleChildsToRoot() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
    assertTree(
        "root\n" +
        "  c1\n" +
        "  c2\n" +
        "    c2-1\n" +
        "  c3\n" +
        "  c4");
  }

  @Test
  public void testAddChildsToParent() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    assertTree(
        "root\n" +
            "  c1\n" +
            "    c2");
  }

  private void assertTree(final String expected) {
    assertEquals(expected, tree.toString());
  }

  private void assertNodes(final InternalNiftyTree tree, final TestNode ... expected) {
    assertEqualList(makeList(tree.childNodes()), expected);
  }

  private TestNode node(final String value) {
    return new TestNode(value);
  }

  private void assertEqualList(final List<NiftyNode> actual, final TestNode ... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private static <E> List<E> makeList(final Iterable<E> iter) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    return list;
  }
}