/*
 * Copyright (c) 2014, Jens Hohmuth 
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
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.RenderNode;
import de.lessvoid.nifty.spi.NiftyTexture;

public class RendererNodeSyncTest {
  private static final int WIDTH = 1;
  private static final int HEIGHT = 1;

  private RendererNodeSyncNodeFactory nodeFactory = createMock(RendererNodeSyncNodeFactory.class);
  private RenderNodeSync sync = new RenderNodeSync(nodeFactory, new RendererNodeSyncInternal());
  private Nifty nifty = createNiceMock(Nifty.class);
  private NiftyTexture contentTexture = createNiceMock(NiftyTexture.class);
  private NiftyTexture workingTexture = createNiceMock(NiftyTexture.class);

  @Before
  public void before() {
    expect(nifty.getScreenWidth()).andReturn(1024).anyTimes();
    expect(nifty.getScreenHeight()).andReturn(768).anyTimes();
    replay(nifty);

    replay(contentTexture);
    replay(workingTexture);
  }

  @After
  public void after() {
    verify(nodeFactory);
    verify(nifty);
    verify(contentTexture);
    verify(workingTexture);
  }

  @Test
  public void testEmpty() {
    replay(nodeFactory);

    assertFalse(sync.synchronize(asSrcList(), asDstList()));
  }

  @Test
  public void testNewEntry() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, false);
    RenderNode newDstNode1 = newRenderNode(0, "1".hashCode());

    expect(nodeFactory.createRenderNode(srcNode1)).andReturn(newDstNode1);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1), asDstList(), newDstNode1);
  }

  @Test
  public void testTwoNewEntries() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, false);
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, false);
    RenderNode newDstNode1 = newRenderNode(0, "1".hashCode());
    RenderNode newDstNode2 = newRenderNode(1, "2".hashCode());

    expect(nodeFactory.createRenderNode(srcNode1)).andReturn(newDstNode1);
    expect(nodeFactory.createRenderNode(srcNode2)).andReturn(newDstNode2);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1, srcNode2), asDstList(), newDstNode1, newDstNode2);
  }

  @Test
  public void testSingleEntryNoChange() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);

    replay(nodeFactory);

    assertNoChanged(asSrcList(srcNode1), asDstList(dstNode1), dstNode1);
  }

  @Test
  public void testSingleEntryWithChange() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, true);
    RenderNode dstNode1 = existingRenderNodeModified("1".hashCode(), WIDTH, HEIGHT);

    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1), asDstList(dstNode1), dstNode1);
  }

  @Test
  public void testTwoEntriesNoChange() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, false);
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertNoChanged(asSrcList(srcNode1, srcNode2), asDstList(dstNode1, dstNode2), dstNode1, dstNode2);
  }

  @Test
  public void testTwoEntriesWithChange() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, true);
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, true);
    RenderNode dstNode1 = existingRenderNodeModified("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNodeModified("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1, srcNode2), asDstList(dstNode1, dstNode2), dstNode1, dstNode2);
  }

  @Test
  public void testTwoExistingEntriesRemovedByZeroWidth() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", 0, HEIGHT, false);
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1, srcNode2), asDstList(dstNode1, dstNode2), dstNode2);
  }

  @Test
  public void testTwoExistingEntriesRemovedByZeroHeight() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, 0, false);
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1, srcNode2), asDstList(dstNode1, dstNode2), dstNode2);
  }

  @Test
  public void testTwoExistingEntriesFirstGetsRemoved() {
    InternalNiftyNode srcNode2 = internalNiftyNode("2", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode2), asDstList(dstNode1, dstNode2), dstNode2);
  }

  @Test
  public void testTwoExistingEntriesLastRemoved() {
    InternalNiftyNode srcNode1 = internalNiftyNode("1", WIDTH, HEIGHT, false);
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(srcNode1), asDstList(dstNode1, dstNode2), dstNode1);
  }

  @Test
  public void testTwoExistingEntriesAllRemoved() {
    RenderNode dstNode1 = existingRenderNode("1".hashCode(), WIDTH, HEIGHT);
    RenderNode dstNode2 = existingRenderNode("2".hashCode(), WIDTH, HEIGHT);
    replay(nodeFactory);

    assertChanged(asSrcList(), asDstList(dstNode1, dstNode2));
  }

  private void assertChanged(
      final List<InternalNiftyNode> srcList,
      final List<RenderNode> dstList,
      final RenderNode ... expected) {
    assertTrue(sync.synchronize(srcList, dstList));
    assertDstNodes(dstList, expected);
  }

  private void assertNoChanged(
      final List<InternalNiftyNode> srcList,
      final List<RenderNode> dstList,
      final RenderNode ... expected) {
    assertFalse(sync.synchronize(srcList, dstList));
    assertDstNodes(dstList, expected);
  }

  private void assertDstNodes(final List<RenderNode> actual, final RenderNode ... expected) {
    assertEquals(expected.length, actual.size());
    for (int i=0; i<expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private RenderNode newRenderNode(final int indexInParent, final int nodeId) {
    RenderNode renderNode = createMock(RenderNode.class);
    renderNode.setIndexInParent(indexInParent);
    expect(renderNode.getNodeId()).andReturn(nodeId).anyTimes();
    replay(renderNode);
    return renderNode;
  }

  private RenderNode existingRenderNode(final int nodeId, final int width, final int height) {
    RenderNode renderNode = createMock(RenderNode.class);
    expect(renderNode.getNodeId()).andReturn(nodeId).anyTimes();
    expect(renderNode.getWidth()).andReturn(width).anyTimes();
    expect(renderNode.getHeight()).andReturn(height).anyTimes();
    renderNode.setLocal(Mat4.createIdentity());
    expectLastCall().anyTimes();
    replay(renderNode);
    return renderNode;
  }

  private RenderNode existingRenderNodeModified(final int nodeId, final int width, final int height) {
    RenderNode renderNode = createMock(RenderNode.class);
    expect(renderNode.getNodeId()).andReturn(nodeId).anyTimes();
    expect(renderNode.getWidth()).andReturn(width).anyTimes();
    expect(renderNode.getHeight()).andReturn(height).anyTimes();
    renderNode.setLocal(isA(Mat4.class));
    renderNode.setWidth(width);
    renderNode.setHeight(height);
    renderNode.needsRender();
    replay(renderNode);
    return renderNode;
  }

  private InternalNiftyNode internalNiftyNode(final String id, final int width, final int height, final boolean transformationChanged) {
    InternalNiftyNode result = createMock(InternalNiftyNode.class);
    expect(result.getId()).andReturn(id).anyTimes();
    expect(result.getWidth()).andReturn(width).anyTimes();
    expect(result.getHeight()).andReturn(height).anyTimes();
    expect(result.getCanvas()).andReturn(unchangedCanvas()).anyTimes();
    expect(result.isTransformationChanged()).andReturn(transformationChanged).anyTimes();
    expect(result.getLocalTransformation()).andReturn(Mat4.createIdentity()).anyTimes();
    expect(result.getChildren()).andReturn(new ArrayList<InternalNiftyNode>()).anyTimes();
    replay(result);
    return result;
  }

  private InternalNiftyCanvas unchangedCanvas() {
    return canvas(false);
  }

  private InternalNiftyCanvas canvas(final boolean changed) {
    InternalNiftyCanvas result = createMock(InternalNiftyCanvas.class);
    expect(result.isChanged()).andReturn(changed);
    replay(result);
    return result;
  }

  private List<InternalNiftyNode> asSrcList(final InternalNiftyNode ... srcNodes) {
    return Arrays.asList(srcNodes);
  }

  private List<RenderNode> asDstList(final RenderNode ... renderNodes) {
    List<RenderNode> result = new ArrayList<RenderNode>();
    result.addAll(Arrays.asList(renderNodes));
    return result;
  }

}
