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

import de.lessvoid.nifty.api.*;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.spi.NiftyNodeImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.api.NiftyNodeLong.niftyNodeLong;
import static de.lessvoid.nifty.api.NiftyNodeLongImpl.niftyNodeLongImpl;
import static de.lessvoid.nifty.api.NiftyNodeString.niftyNodeString;
import static de.lessvoid.nifty.api.NiftyNodeStringImpl.niftyNodeStringImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by void on 23.07.15.
 */
public class InternalNiftyTreeTest {
  private InternalNiftyTree tree;

  @Test(expected = NiftyRuntimeException.class)
  public void testNullRootNode() {
    createTree(null);
  }

  @Test
  public void testGetRootNode() {
    createTree(niftyNodeStringImpl("root"));
    assertEquals(niftyNodeString("root"), tree.getRootNode());
  }

  @Test
  public void testAddSingleChildToRoot() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("child"));
    assertTree(
        "root",
        "  child");
  }

  @Test
  public void testAddMultipleChildsToRoot() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    assertTree(
        "root",
        "  c1",
        "  c2",
        "    c2-1",
        "  c3",
        "  c4");
  }

  @Test
  public void testAddChildsToParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"))
        .addChild(niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"));
    assertTree(
        "root",
        "  c1",
        "    c2");
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testRemoveRoot() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"))
        .addChild(niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"));
    tree.remove(niftyNodeStringImpl("root"));
  }

  @Test
  public void testRemoveLastChild() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"))
        .addChild(niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"));
    tree.remove(niftyNodeStringImpl("c2"));
    assertTree(
        "root",
        "  c1");
  }

  @Test
  public void testRemoveMiddleChild() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"))
        .addChild(niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"));
    tree.remove(niftyNodeStringImpl("c1"));
    assertTree(
        "root",
        "  c2");
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testRemoveNoneExistingChild() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"))
        .addChild(niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"));
    tree.remove(niftyNodeStringImpl("c1"));
    tree.remove(niftyNodeStringImpl("c1"));
  }

  @Test
  public void testChildNodes() {
    createTree(niftyNodeStringImpl("root"));
    assertChildNodes(niftyNodeStringImpl("root"));
  }

  @Test
  public void testChildNodesSingleChild() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("child"));
    assertChildNodes(niftyNodeStringImpl("root"), niftyNodeStringImpl("child"));
  }

  @Test
  public void testChildNodesMultipleChilds() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    assertChildNodes(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"));
  }

  @Test
  public void testChildNodesMultipleChildsAfterRemove() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    tree.remove(niftyNodeStringImpl("c1"));
    tree.remove(niftyNodeStringImpl("c2-1"));
    tree.remove(niftyNodeStringImpl("c3"));
    assertChildNodes(niftyNodeStringImpl("root"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c4"));
  }

  @Test
  public void testChildNodesFromSpecificParentRoot() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    assertChildNodesFromParent(niftyNodeStringImpl("root"), niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"));
  }

  @Test
  public void testChildNodesFromSpecificImplParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    assertChildNodesFromParent(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
  }

  @Test
  public void testChildNodesFromSpecificParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
    assertChildNodesFromParent(niftyNodeString("c2"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c2-1"));
  }

  @Test
  public void testDifferentChildNodeTypes() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertTree(
        "root",
        "  c1",
        "  c2",
        "    46",
        "  c3",
        "  c4");
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNode() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodes(NiftyNodeLongImpl.class, niftyNodeLongImpl(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNodeImpl() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodesImpl(NiftyNodeLongImpl.class, niftyNodeLongImpl(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNode() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodes(NiftyNodeStringImpl.class, niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNodeFromImplParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodesFromParent(NiftyNodeLong.class, niftyNodeStringImpl("c2"), niftyNodeLong(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNodeFromParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodesFromParent(NiftyNodeLong.class, niftyNodeStringImpl("c2"), niftyNodeLong(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongImplNodeFromParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodesFromParentImpl(NiftyNodeLongImpl.class, niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNodeFromParent() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertFilteredChildNodesFromParent(NiftyNodeString.class, niftyNodeStringImpl("c2"), niftyNodeString("c2"));
  }

  @Test
  public void testGetParentOfRootNode() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertNull(tree.getParent(niftyNodeStringImpl("root")));
  }

  @Test
  public void testGetParentOfNiftyNode() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeStringImpl("c1"), niftyNodeStringImpl("c2"), niftyNodeStringImpl("c3"), niftyNodeStringImpl("c4"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeLongImpl(46L));
    assertEquals(niftyNodeStringImpl("root"), tree.getParent(niftyNodeStringImpl("c1")));
    assertEquals(niftyNodeStringImpl("c2"), tree.getParent(niftyNodeLongImpl(46L)));
  }

  @Test
  public void testGetParentOfNiftyNodeFiltered() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeLongImpl(46L))
        .addChild(niftyNodeLongImpl(46L), niftyNodeStringImpl("c2"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("46"));
    assertEquals(niftyNodeLongImpl(46L), tree.getParent(NiftyNodeLongImpl.class, niftyNodeStringImpl("46")));
  }

  @Test
  public void testGetParentOfNiftyNodeFilteredImpl() {
    tree = createTree(niftyNodeStringImpl("root"))
        .addChild(niftyNodeStringImpl("root"), niftyNodeLongImpl(46L))
        .addChild(niftyNodeLongImpl(46L), niftyNodeStringImpl("c2"))
        .addChild(niftyNodeStringImpl("c2"), niftyNodeStringImpl("46"));
    assertEquals(niftyNodeLong(46L), tree.getParentImpl(NiftyNodeLongImpl.class, niftyNodeStringImpl("46")).getNiftyNode());
  }

  private InternalNiftyTree createTree(final NiftyNodeImpl<? extends NiftyNode> root) {
    tree = new InternalNiftyTree(root);
    return tree;
  }

  private void assertTree(final String ... expected) {
    assertEquals(buildExpected(expected), tree.toString());
  }

  private String buildExpected(final String[] expected) {
    StringBuilder result = new StringBuilder();
    for (int i=0; i<expected.length; i++) {
      result.append(expected[i]);
      if (i < expected.length - 1) {
        result.append("\n");
      }
    }
    return result.toString();
  }

  private void assertChildNodes(final NiftyNodeImpl... expected) {
    assertEqualList(makeList(tree.childNodes()), expected);
  }

  private void assertChildNodesFromParent(final NiftyNodeImpl parent, final NiftyNodeImpl... expected) {
    assertEqualList(makeList(tree.childNodes(parent)), expected);
  }

  private void assertChildNodesFromParent(final NiftyNode parent, final NiftyNodeImpl... expected) {
    assertEqualList(makeList(tree.childNodes(parent)), expected);
  }

  private <Y extends NiftyNodeImpl> void assertFilteredChildNodes(final Class<Y> clazz, final Y ... expected) {
    assertEqualList(makeList(tree.filteredChildNodesImpl(clazz)), expected);
  }

  private <X extends NiftyNodeImpl> void assertFilteredChildNodesImpl(final Class<X> clazz, final X ... expected) {
    assertEqualList(makeList(tree.filteredChildNodesImpl(clazz)), expected);
  }

  private <Y extends NiftyNode> void assertFilteredChildNodesFromParent(final Class<Y> clazz, final NiftyNodeImpl<?> parent, final Y... expected) {
    assertEqualList(makeList(tree.filteredChildNodes(clazz, parent)), expected);
  }

  private <Y extends NiftyNode> void assertFilteredChildNodesFromParent(final Class<Y> clazz, final NiftyNode parent, final Y... expected) {
    assertEqualList(makeList(tree.filteredChildNodes(clazz, parent)), expected);
  }

  private <X extends NiftyNodeImpl<? extends NiftyNode>> void assertFilteredChildNodesFromParentImpl(final Class<X> clazz, final NiftyNodeImpl<? extends NiftyNode> parent, final X... expected) {
    assertEqualList(makeList(tree.filteredChildNodesImpl(clazz, parent)), expected);
  }

  private <X> void assertEqualList(final List<X> actual, final NiftyNodeImpl ... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private <X> void assertEqualList(final List<X> actual, final NiftyNode ... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private static <E> List<E> makeList(final Iterable<E> iter) {
    List<E> list = new ArrayList<>();
    for (E item : iter) {
      list.add(item);
    }
    return list;
  }
}