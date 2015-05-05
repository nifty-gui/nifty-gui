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
package de.lessvoid.nifty.internal.render.sync;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.render.RenderNode;

public class TreeSyncTest {
  private final TreeSyncRenderNodeFactory renderNodeFactory = createMock(TreeSyncRenderNodeFactory.class);
  private final TreeSync treeSync = new TreeSync(renderNodeFactory);

  @After
  public void after() {
    verify(renderNodeFactory);
  }

  @Test
  public void testEmpty() {
    replay(renderNodeFactory);

    assertNotChanged(
        internalNiftyNodes(),
        renderNodes());
  }

  @Test
  public void testNewRenderNode() {
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200);

    RenderNode renderNode = renderNode(niftyNode, 0);
    expect(renderNodeFactory.createRenderNode(niftyNode)).andReturn(renderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(),
        renderNode(niftyNode, 0));
  }

  @Test
  public void testExistingRenderNode() {
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200);
    replay(renderNodeFactory);

    assertNotChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode(niftyNode, 0)),
        renderNode(niftyNode, 0));
  }

  @Test
  public void testNewRenderNodeWithNewChild() {
    InternalNiftyNode childNode = internalNiftyNode("2", 100, 200);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0);
    expect(renderNodeFactory.createRenderNode(niftyNode)).andReturn(renderNode);
    expect(renderNodeFactory.createRenderNode(childNode)).andReturn(childRenderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(),
        renderNode(niftyNode, 0, renderNode(childNode, 0)));
  }

  @Test
  public void testExistingRenderNodeWithNewChild() {
    InternalNiftyNode childNode = internalNiftyNode("2", 100, 200);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0, childRenderNode);
    replay(renderNodeFactory);

    assertNotChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode),
        renderNode(niftyNode, 0, renderNode(childNode, 0)));
  }

  @Test
  public void testExistingRenderNodeWithChildHasZeroWidth() {
    InternalNiftyNode childNode = internalNiftyNode("2", 0, 200);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0, childRenderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode),
        renderNode(niftyNode, 0)); // note: a new renderNode with the same NodeId but without any child elements
  }

  @Test
  public void testExistingRenderNodeWithChildHasZeroHeight() {
    InternalNiftyNode childNode = internalNiftyNode("2", 100, 0);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 200, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0, childRenderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode),
        renderNode(niftyNode, 0)); // note: a new renderNode with the same NodeId but without any child elements
  }

  @Test
  public void testExistingRenderNodeHasZeroWidth() {
    InternalNiftyNode childNode = internalNiftyNode("2", 100, 200);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 0, 200, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0, childRenderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode));
  }

  @Test
  public void testExistingRenderNodeHasZeroHeight() {
    InternalNiftyNode childNode = internalNiftyNode("2", 100, 200);
    InternalNiftyNode niftyNode = internalNiftyNode("1", 100, 0, childNode);

    RenderNode childRenderNode = renderNode(childNode, 0);
    RenderNode renderNode = renderNode(niftyNode, 0, childRenderNode);
    replay(renderNodeFactory);

    assertChanged(
        internalNiftyNodes(niftyNode),
        renderNodes(renderNode));
  }

  private void assertNotChanged(
      final List<InternalNiftyNode> inputSource,
      final List<RenderNode> inputDest,
      final RenderNode ... expectedNodes) {
    assertFalse(treeSync.synchronizeTree(inputSource, inputDest));
    assertRenderNodes(inputDest, expectedNodes);
  }

  private void assertChanged(
      final List<InternalNiftyNode> inputSource,
      final List<RenderNode> inputDest,
      final RenderNode ... expectedNodes) {
    assertTrue(treeSync.synchronizeTree(inputSource, inputDest));
    assertRenderNodes(inputDest, expectedNodes);
  }

  private void assertRenderNodes(final List<RenderNode> actual, final RenderNode ... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i].getNodeId(), actual.get(i).getNodeId());
      assertRenderNodes(expected[i].getChildren(), actual.get(i).getChildren().toArray(new RenderNode[actual.get(i).getChildren().size()]));
    }
  }

  private RenderNode renderNode(final InternalNiftyNode src, final int expectedIndexInParent, final RenderNode ... childs) {
    RenderNode renderNode = createMock(RenderNode.class);
    expect(renderNode.getNodeId()).andReturn(src.getId().hashCode()).anyTimes();
    expect(renderNode.getChildren()).andReturn(new ArrayList<RenderNode>(Arrays.asList(childs))).anyTimes();
    renderNode.setIndexInParent(expectedIndexInParent);
    replay(renderNode);
    return renderNode;
  }

  private InternalNiftyNode internalNiftyNode(final String id, final int width, final int height, final InternalNiftyNode ... childs) {
    InternalNiftyNode result = createMock(InternalNiftyNode.class);
    expect(result.getId()).andReturn(id).anyTimes();
    expect(result.getWidth()).andReturn(width).anyTimes();
    expect(result.getHeight()).andReturn(height).anyTimes();
    expect(result.getChildren()).andReturn(new ArrayList<InternalNiftyNode>(Arrays.asList(childs))).anyTimes();
    replay(result);
    return result;
  }

  private List<InternalNiftyNode> internalNiftyNodes(final InternalNiftyNode ... srcNodes) {
    return Arrays.asList(srcNodes);
  }

  private List<RenderNode> renderNodes(final RenderNode ... renderNodes) {
    List<RenderNode> result = new ArrayList<RenderNode>();
    result.addAll(Arrays.asList(renderNodes));
    return result;
  }

}
