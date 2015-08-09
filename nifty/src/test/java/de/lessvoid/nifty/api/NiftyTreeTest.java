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
package de.lessvoid.nifty.api;

import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.spi.NiftyInputDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.TimeProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.lessvoid.nifty.api.NiftyNodeLong.niftyNodeLong;
import static de.lessvoid.nifty.api.NiftyNodeString.niftyNodeString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

/**
 * Created by void on 26.07.15.
 */
public class NiftyTreeTest {
  private Nifty nifty;

  @Before
  public void before() {
    NiftyRenderDevice a = createNiceMock(NiftyRenderDevice.class);
    NiftyInputDevice b = createNiceMock(NiftyInputDevice.class);
    TimeProvider c = createNiceMock(TimeProvider.class);
    replay(a);
    replay(b);
    replay(c);
    nifty = new Nifty(a, b, c);
  }

  @Test
  public void testAddSingleChildToRoot() {
    nifty
        .node(niftyNodeString("child"));
    assertTree(
        "root",
        "  child");
  }

  @Test
  public void testAddMultipleChildsToRoot() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeString("c2-1"))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
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
    nifty
        .node(niftyNodeString("c1"))
          .childNode(niftyNodeString("c2"));
    assertTree(
        "root",
        "  c1",
        "    c2");
  }

  @Test
  public void testRemoveLastChild() {
    nifty
        .node(niftyNodeString("c1"))
          .childNode(niftyNodeString("c2"));
    nifty.remove(niftyNodeString("c2"));
    assertTree(
        "root",
        "  c1");
  }

  @Test
  public void testRemoveMiddleChild() {
    nifty
        .node(niftyNodeString("c1"))
          .childNode(niftyNodeString("c2"));
    nifty.remove(niftyNodeString("c1"));
    assertTree(
        "root",
        "  c2");
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testRemoveNoneExistingChild() {
    nifty
        .node(niftyNodeString("c1"))
          .childNode(niftyNodeString("c2"));
    nifty.remove(niftyNodeString("c1"));
    nifty.remove(niftyNodeString("c1"));
  }

  @Test
  public void testChildNodes() {
    assertChildNodes(niftyNodeString("root"));
  }

  @Test
  public void testChildNodesSingleChild() {
    nifty
        .node(niftyNodeString("child"));
    assertChildNodes(
        niftyNodeString("root"),
        niftyNodeString("child"));
  }

  @Test
  public void testChildNodesMultipleChilds() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeString("c2-1"))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertChildNodes(
        niftyNodeString("root"),
        niftyNodeString("c1"),
        niftyNodeString("c2"),
        niftyNodeString("c2-1"),
        niftyNodeString("c3"),
        niftyNodeString("c4"));
  }

  @Test
  public void testChildNodesMultipleChildsAfterRemove() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeString("c2-1"))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    nifty.remove(niftyNodeString("c1"));
    nifty.remove(niftyNodeString("c2-1"));
    nifty.remove(niftyNodeString("c3"));
    assertChildNodes(
        niftyNodeString("root"),
        niftyNodeString("c2"),
        niftyNodeString("c4"));
  }

  @Test
  public void testChildNodesFromSpecificParent() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeString("c2-1"))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertChildNodesFromParent(
        niftyNodeString("c2"),
        niftyNodeString("c2"),
        niftyNodeString("c2-1"));
  }

  @Test
  public void testDifferentChildNodeTypes() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeLong(46L))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
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
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeLong(46L))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertFilteredChildNodes(
        NiftyNodeLong.class,
        niftyNodeLong(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNode() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeLong(46L))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertFilteredChildNodes(
        NiftyNodeString.class,
        niftyNodeString("c1"),
        niftyNodeString("c2"),
        niftyNodeString("c3"),
        niftyNodeString("c4"));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestLongNodeFromParent() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeLong(46L))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertFilteredChildNodesFromParent(
        NiftyNodeLong.class,
        niftyNodeString("c2"),
        niftyNodeLong(46L));
  }

  @Test
  public void testDifferentChildNodeTypesFilteredTestStringNodeFromParent() {
    nifty
        .node(niftyNodeString("c1"))
        .node(niftyNodeString("c2"))
          .childNode(niftyNodeLong(46L))
        .node(niftyNodeString("c3"))
        .node(niftyNodeString("c4"));
    assertFilteredChildNodesFromParent(
        NiftyNodeString.class,
        niftyNodeString("c2"),
        niftyNodeString("c2"));
  }

  private void assertTree(final String ... expected) {
    assertEquals("Nifty scene info log\n" + buildExpected(expected), nifty.getSceneInfoLog());
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

  private void assertChildNodes(final NiftyNodeString... expected) {
    assertEqualList(makeList(nifty.childNodes()), expected);
  }

  private void assertChildNodesFromParent(final NiftyNodeString parent, final NiftyNodeString... expected) {
    assertEqualList(makeList(nifty.childNodes(parent)), expected);
  }

  private <X extends NiftyNode> void assertFilteredChildNodes(final Class<X> clazz, final X ... expected) {
    assertEqualList(makeList(nifty.filteredChildNodes(clazz)), expected);
  }

  private <X extends NiftyNode> void assertFilteredChildNodesFromParent(final Class<X> clazz, final NiftyNode parent, final X... expected) {
    assertEqualList(makeList(nifty.filteredChildNodes(clazz, parent)), expected);
  }

  private <X> void assertEqualList(final List<X> actual, final X... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i].toString(), actual.get(i).toString());
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