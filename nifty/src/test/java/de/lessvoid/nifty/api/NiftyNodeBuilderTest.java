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
import org.junit.After;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by void on 09.08.15.
 */
public class NiftyNodeBuilderTest {
  private Nifty nifty = createMock(Nifty.class);
  private NiftyNode child = createMock(NiftyNode.class);
  private NiftyNode parent = createMock(NiftyNode.class);
  private NiftyNodeBuilder builder = new NiftyNodeBuilder(nifty, parent);

  @After
  public void after() {
    verify(nifty);
    verify(child);
    verify(parent);
  }

  @Test
  public void testAddNode() {
    replay(parent);
    replay(child);

    expect(nifty.node(child)).andReturn(builder);
    replay(nifty);

    assertEquals(builder, builder.node(child));
  }

  @Test
  public void testAddNodeToChild() {
    replay(parent);
    replay(child);

    expect(nifty.node(parent, child)).andReturn(builder);
    replay(nifty);

    assertEquals(builder, builder.node(parent, child));
  }

  @Test
  public void testAddChildNode() {
    replay(parent);
    replay(child);

    expect(nifty.node(parent, child)).andReturn(builder);
    replay(nifty);

    assertEquals(builder, builder.childNode(child));
  }
}
