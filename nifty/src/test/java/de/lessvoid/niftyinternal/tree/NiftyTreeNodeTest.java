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

import de.lessvoid.nifty.NiftyNodeLong;
import de.lessvoid.nifty.NiftyNodeLongImpl;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static de.lessvoid.nifty.NiftyNodeLongImpl.niftyNodeLongImpl;
import static de.lessvoid.nifty.NiftyNodeStringImpl.niftyNodeStringImpl;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.*;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplAny;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplClass;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeClass;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;

/**
 * Created by void on 19.07.15.
 */
public class NiftyTreeNodeTest {

  @Test
  public void testSimpleConstructor() {
    NiftyTreeNode root = niftyTreeNode("root");
    assertEquals("node{root}", root.toString());
  }

  @Test
  public void testGetValue() {
    NiftyTreeNode root = niftyTreeNode("root");
    assertEquals("root", root.getValue().getNiftyNode().toString());
  }

  @Test
  public void testGetParentForRootNode() {
    NiftyTreeNode root = niftyTreeNode("root");
    assertNull(root.getParent());
  }

  @Test
  public void testAddChildren() {
    NiftyTreeNode root = niftyTreeNode("root");
    root.addChild(niftyTreeNode("child-1"));
  }

  @Test
  public void testAddChildrenParent() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child = niftyTreeNode("child-1");
    root.addChild(child);
    assertEquals(root, child.getParent());
    assertListEquals(toTreeNodeList("child-1"), root.getChildren());
  }

  @Test
  public void testToStringTree() {
    NiftyTreeNode root = niftyTreeNode("root");
    assertEquals("root", root.toStringTree());
  }

  @Test
  public void testToStringTreeWithChildren() {
    NiftyTreeNode root = niftyTreeNode("root");
    root.addChild(niftyTreeNode("hello"));
    assertEquals(
        "root\n" +
            "  hello", root.toStringTree());
  }

  @Test
  public void testGetChildrenWithNoChildren() {
    NiftyTreeNode root = niftyTreeNode("root");
    assertNull(root.getChildren());
  }

  @Test
  public void testGetChildren() {
    NiftyTreeNode root = niftyTreeNode("root");
    root.addChild(niftyTreeNode("hello"));
    assertListEquals(toTreeNodeList("hello"), root.getChildren());
  }

  @Test
  public void testRemoveRoot() {
    NiftyTreeNode root = niftyTreeNode("root");
    root.remove();
  }

  @Test
  public void testRemoveSelf() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child = niftyTreeNode("hello");
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
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child = niftyTreeNode("hello");
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
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    child1.addChild(child2);

    assertEquals(
        "root\n" +
        "  hello-1\n" +
        "    hello-2", root.toStringTree());

    child1.remove();
    assertListEquals(toTreeNodeList("hello-2"), root.getChildren());
    assertEquals(root, child2.getParent());

    assertEquals(
        "root\n" +
        "  hello-2", root.toStringTree());

  }

  @Test
  public void testNiftyNodeImplIterator() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child11 = niftyTreeNode("hello-1-1");
    child1.addChild(child11);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    root.addChild(child2);

    Iterator<NiftyNodeImpl> it = root.iterator(nodeImplAny(), toNodeImpl());
    assertEquals("root", it.next().toString());
    assertEquals("hello-1", it.next().toString());
    assertEquals("hello-1-1", it.next().toString());
    assertEquals("hello-2", it.next().toString());
    assertFalse(it.hasNext());
  }

  @Test
  public void testFilteredNiftyNodeImplIterator() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child11 = niftyTreeNode(42L);
    child1.addChild(child11);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    root.addChild(child2);

    Iterator<NiftyNodeLongImpl> it = root.iterator(nodeImplClass(NiftyNodeLongImpl.class), toNodeImplClass(NiftyNodeLongImpl.class));
    assertEquals(42L, it.next().getNiftyNode().getValue());
    assertFalse(it.hasNext());
  }

  @Test
  public void testNiftyNodeIterator() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child11 = niftyTreeNode("hello-1-1");
    child1.addChild(child11);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    root.addChild(child2);

    Iterator<? extends NiftyNode> it = root.iterator(nodeImplAny(), toNiftyNode());
    assertEquals("root", it.next().toString());
    assertEquals("hello-1", it.next().toString());
    assertEquals("hello-1-1", it.next().toString());
    assertEquals("hello-2", it.next().toString());
    assertFalse(it.hasNext());
  }

  @Test
  public void testFilteredNiftyNodeIterator() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child11 = niftyTreeNode(42L);
    child1.addChild(child11);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    root.addChild(child2);

    Iterator<NiftyNodeLong> it = root.iterator(nodeClass(NiftyNodeLong.class), toNiftyNodeClass(NiftyNodeLong.class));
    assertEquals(42L, it.next().getValue());
    assertFalse(it.hasNext());
  }

  @Test
  public void testFilteredIteratorGeneral() {
    NiftyTreeNode root = niftyTreeNode("root");
    NiftyTreeNode child1 = niftyTreeNode("hello-1");
    root.addChild(child1);
    NiftyTreeNode child11 = niftyTreeNode(42L);
    child1.addChild(child11);
    NiftyTreeNode child2 = niftyTreeNode("hello-2");
    root.addChild(child2);

    Iterator<NiftyNodeLongImpl> it = root.iterator(nodeImplClass(NiftyNodeLongImpl.class), toNodeImplClass(NiftyNodeLongImpl.class));
    assertEquals(42L, it.next().getNiftyNode().getValue());
    assertFalse(it.hasNext());
  }

  private NiftyTreeNode niftyTreeNode(final String value) {
    return new NiftyTreeNode(niftyNodeStringImpl(value));
  }

  private NiftyTreeNode niftyTreeNode(final long value) {
    return new NiftyTreeNode(niftyNodeLongImpl(value));
  }

  private void assertListEquals(final List<NiftyTreeNode> expected, final List<NiftyTreeNode> actual) {
    assertEquals(expected.size(), actual.size());
    for (int i=0; i<actual.size(); i++) {
      assertEquals(expected.get(i).getValue().toString(), actual.get(i).getValue().toString());
    }
  }

  private List<NiftyTreeNode> toTreeNodeList(final String... values) {
    List<NiftyTreeNode> list = new ArrayList<>();
    for (String v : values) {
      list.add(niftyTreeNode(v));
    }
    return list;
  }
}
