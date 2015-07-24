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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by void on 19.07.15.
 */
public class NiftyTreeNodeTest {

  @Test
  public void testSimpleConstructor() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    assertEquals("node{root}", root.toString());
  }

  @Test
  public void testGetValue() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    assertEquals("root", root.getValue());
  }

  @Test
  public void testGetParentForRootNode() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    assertNull(root.getParent());
  }

  @Test
  public void testAddChildren() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    root.addChild(new NiftyTreeNode<>("child-1"));
  }

  @Test
  public void testAddChildrenParent() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child = new NiftyTreeNode<>("child-1");
    root.addChild(child);
    assertEquals(root, child.getParent());
    assertArrayEquals(toTreeNodeArray("child-1"), root.getChildren().toArray());
  }

  @Test
  public void testToStringTree() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    assertEquals("root", root.toStringTree());
  }

  @Test
  public void testToStringTreeWithChildren() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    root.addChild(new NiftyTreeNode<>("hello"));
    assertEquals(
        "root\n" +
        "  hello", root.toStringTree());
  }

  @Test
  public void testGetChildrenWithNoChildren() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    assertNull(root.getChildren());
  }

  @Test
  public void testGetChildren() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    root.addChild(new NiftyTreeNode<>("hello"));
    assertArrayEquals(toTreeNodeArray("hello"), root.getChildren().toArray());
  }

  @Test
  public void testRemoveRoot() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    root.remove();
  }

  @Test
  public void testRemoveSelf() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child = new NiftyTreeNode<>("hello");
    root.addChild(child);

    assertEquals(
        "root\n" +
        "  hello", root.toStringTree());

    child.remove();
    assertNull(root.getChildren());

    assertEquals(
        "root", root.toStringTree());
  }

  @Test
  public void testRemoveChild() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child = new NiftyTreeNode<>("hello");
    root.addChild(child);

    assertEquals(
        "root\n" +
            "  hello", root.toStringTree());

    root.remove(child);
    assertNull(root.getChildren());

    assertEquals(
        "root", root.toStringTree());
  }

  @Test
  public void testRemoveMiddleChildren() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child1 = new NiftyTreeNode<>("hello-1");
    root.addChild(child1);
    NiftyTreeNode<String> child2 = new NiftyTreeNode<>("hello-2");
    child1.addChild(child2);

    assertEquals(
        "root\n" +
        "  hello-1\n" +
        "    hello-2", root.toStringTree());

    child1.remove();
    assertArrayEquals(toTreeNodeArray("hello-2"), root.getChildren().toArray());
    assertEquals(root, child2.getParent());

    assertEquals(
        "root\n" +
        "  hello-2", root.toStringTree());

  }

  @Test
  public void testTreeIterator() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child1 = new NiftyTreeNode<>("hello-1");
    root.addChild(child1);
    NiftyTreeNode<String> child11 = new NiftyTreeNode<>("hello-1-1");
    child1.addChild(child11);
    NiftyTreeNode<String> child2 = new NiftyTreeNode<>("hello-2");
    root.addChild(child2);

    Iterator<NiftyTreeNode<String>> it = root.treeIterator();
    assertEquals("root", it.next().getValue());
    assertEquals("hello-1", it.next().getValue());
    assertEquals("hello-1-1", it.next().getValue());
    assertEquals("hello-2", it.next().getValue());
    assertFalse(it.hasNext());
  }

  @Test
  public void testTreeValueIterator() {
    NiftyTreeNode<String> root = new NiftyTreeNode<>("root");
    NiftyTreeNode<String> child1 = new NiftyTreeNode<>("hello-1");
    root.addChild(child1);
    NiftyTreeNode<String> child11 = new NiftyTreeNode<>("hello-1-1");
    child1.addChild(child11);
    NiftyTreeNode<String> child2 = new NiftyTreeNode<>("hello-2");
    root.addChild(child2);

    Iterator<String> it = root.valueIterator();
    assertEquals("root", it.next());
    assertEquals("hello-1", it.next());
    assertEquals("hello-1-1", it.next());
    assertEquals("hello-2", it.next());
    assertFalse(it.hasNext());
  }

  private Object[] toTreeNodeArray(final String... values) {
    List<NiftyTreeNode<String>> list = new ArrayList<>();
    for (String v : values) {
      list.add(new NiftyTreeNode<>(v));
    }
    return list.toArray();
  }
}
