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

import de.lessvoid.nifty.api.NiftyRuntimeException;
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

  @Test(expected = NiftyRuntimeException.class)
  public void testNullRootNode() {
    tree = new InternalNiftyTree(null);
  }

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
        "root",
        "  child");
  }

  @Test
  public void testAddMultipleChildsToRoot() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
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
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    assertTree(
        "root",
        "  c1",
        "    c2");
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testRemoveRoot() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    tree.remove(node("root"));
  }

  @Test
  public void testRemoveLastChild() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    tree.remove(node("c2"));
    assertTree(
        "root",
        "  c1");
  }

  @Test
  public void testRemoveMiddleChild() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    tree.remove(node("c1"));
    assertTree(
        "root",
        "  c2");
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testRemoveNoneExistingChild() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"))
        .addChild(node("c1"), node("c2"));
    tree.remove(node("c1"));
    tree.remove(node("c1"));
  }

  @Test
  public void testChildNodes() {
    tree = new InternalNiftyTree(node("root"));
    assertChildNodes(node("root"));
  }

  @Test
  public void testChildNodesSingleChild() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("child"));
    assertChildNodes(node("root"), node("child"));
  }

  @Test
  public void testChildNodesMultipleChilds() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
    assertChildNodes(node("root"), node("c1"), node("c2"), node("c2-1"), node("c3"), node("c4"));
  }

  @Test
  public void testChildNodesMultipleChildsAfterRemove() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
    tree.remove(node("c1"));
    tree.remove(node("c2-1"));
    tree.remove(node("c3"));
    assertChildNodes(node("root"), node("c2"), node("c4"));
  }

  @Test
  public void testChildNodesFromSpecificParentRoot() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
    assertChildNodesFromParent(node("root"), node("root"), node("c1"), node("c2"), node("c2-1"), node("c3"), node("c4"));
  }

  @Test
  public void testChildNodesFromSpecificParent() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node("c2-1"));
    assertChildNodesFromParent(node("c2"), node("c2"), node("c2-1"));
  }

  @Test
  public void testDifferentChildNodeTypes() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
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
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertFilteredChildNodes(TestLongNode.class, node(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNode() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertFilteredChildNodes(TestStringNode.class, node("root"), node("c1"), node("c2"), node("c3"), node("c4"));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNodeFromParent() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertFilteredChildNodesFromParent(TestLongNode.class, node("c2"), node(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNodeFromParent() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertFilteredChildNodesFromParent(TestStringNode.class, node("c2"), node("c2"));
  }

  @Test
  public void testGetParentOfRootNode() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertNull(tree.getParent(node("root")));
  }

  @Test
  public void testGetParentOfNiftyNode() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node("c1"), node("c2"), node("c3"), node("c4"))
        .addChild(node("c2"), node(46L));
    assertEquals(node("root"), tree.getParent(node("c1")));
    assertEquals(node("c2"), tree.getParent(node(46L)));
  }

  @Test
  public void testGetParentOfNiftyNodeFiltered() {
    tree = new InternalNiftyTree(node("root"))
        .addChild(node("root"), node(46L))
        .addChild(node(46L), node("c2"))
        .addChild(node("c2"), node("46"));
    assertEquals(node(46L), tree.getParent(TestLongNode.class, node("46")));
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

  private void assertChildNodes(final TestStringNode... expected) {
    assertEqualList(makeList(tree.childNodes()), expected);
  }

  private void assertChildNodesFromParent(final TestStringNode parent, final TestStringNode... expected) {
    assertEqualList(makeList(tree.childNodes(parent)), expected);
  }

  private <X extends NiftyNode> void assertFilteredChildNodes(final Class<X> clazz, final X ... expected) {
    assertEqualList(makeList(tree.filteredChildNodes(clazz)), expected);
  }

  private <X extends NiftyNode> void assertFilteredChildNodesFromParent(final Class<X> clazz, final NiftyNode parent, final X... expected) {
    assertEqualList(makeList(tree.filteredChildNodes(clazz, parent)), expected);
  }

  private TestStringNode node(final String value) {
    return new TestStringNode(value);
  }

  private TestLongNode node(final long value) {
    return new TestLongNode(value);
  }

  private void assertEqualNiftyNodeList(final List<NiftyNode> actual, final Object... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private <X> void assertEqualList(final List<X> actual, final X... expected) {
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