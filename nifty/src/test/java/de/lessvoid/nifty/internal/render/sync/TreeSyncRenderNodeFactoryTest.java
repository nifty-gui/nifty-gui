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
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertEquals;

public class TreeSyncRenderNodeFactoryTest {
  /* FIXME
  private NiftyRenderDevice renderDevice = createNiceMock(NiftyRenderDevice.class);
  private NiftyTexture niftyTexture = createNiceMock(NiftyTexture.class);
  private InternalNiftyCanvas niftyCanvas = createNiceMock(InternalNiftyCanvas.class);
  private TreeSyncRenderNodeFactory factory;

  @Before
  public void before() {
    replay(niftyTexture);

    expect(niftyCanvas.getCommands()).andReturn(new ArrayList<Command>()).anyTimes();
    replay(niftyCanvas);

    expect(renderDevice.createTexture(anyInt(), anyInt(), eq(FilterMode.Linear))).andReturn(niftyTexture).anyTimes();
    replay(renderDevice);

    factory = new TreeSyncRenderNodeFactory(renderDevice);
  }

  @After
  public void after() {
    verify(niftyTexture);
    verify(renderDevice);
    verify(niftyCanvas);
  }

  @Test
  public void testWithoutChildren() {
    InternalNiftyNode node = niftyNode("1", 100, 200);

    RenderNode renderNode = factory.createRenderNode(node);

    assertEquals(
        "- [49] indexInParent [0]\n" +
        "  width [100] height [200] content [true] render [true]\n" +
        "  local [1.0 0.0 0.0 0.0]\n" +
        "  local [0.0 1.0 0.0 0.0]\n" +
        "  local [0.0 0.0 1.0 0.0]\n" +
        "  local [0.0 0.0 0.0 1.0]\n" +
        "  AABB old [x=0, y=0, width=0, height=0]\n" +
        "  AABB cur [x=0, y=0, width=100, height=200]\n" +
        "  command count [0]\n", stateInfo(renderNode));
  }

  private String stateInfo(RenderNode renderNode) {
    StringBuilder result = new StringBuilder();
    renderNode.outputStateInfo(result, "");
    return result.toString();
  }

  private InternalNiftyNode niftyNode(final String id, final int width, final int height, final InternalNiftyNode ... childs) {
    InternalNiftyNode result = createMock(InternalNiftyNode.class);
    expect(result.getId()).andReturn(id);
    expect(result.getLocalTransformation()).andReturn(Mat4.createIdentity());
    expect(result.getWidth()).andReturn(width).anyTimes();
    expect(result.getHeight()).andReturn(height).anyTimes();
    expect(result.getCanvas()).andReturn(niftyCanvas);
    expect(result.getCompositeOperation()).andReturn(NiftyCompositeOperation.Clear);
    expect(result.getRenderOrder()).andReturn(2);
    expect(result.getChildren()).andReturn(Arrays.asList(childs)).anyTimes();
    replay(result);
    return result;
  }
  */
}
