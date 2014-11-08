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
package de.lessvoid.nifty.internal.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.VAlign;

public class TextRendererAlignTest {
  private TextRenderer textRenderer;
  private NiftyFont font;
  private NiftyNode node;
  private NiftyCanvas canvas;

  @Before
  public void before() {
    textRenderer = new TextRenderer();
    font = createMock(NiftyFont.class);
  }

  @After
  public void after() {
    verify(font);
    verify(node);
    verify(canvas);
  }

  @Test
  public void testRenderEmptyText() {
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createEmptyNodeMock();
    canvas = createEmptyCanvas();
    textRenderer.initialize(font, "");
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextDefaultVAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextCenterVAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.setTextVAlign(VAlign.center);
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextTopVAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 0);
    textRenderer.initialize(font, "Hello");
    textRenderer.setTextVAlign(VAlign.top);
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextBottomVAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 9);
    textRenderer.initialize(font, "Hello");
    textRenderer.setTextVAlign(VAlign.bottom);
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextDefaultHAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextCenterHAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(2, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextLeftHAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(0, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.setTextHAlign(HAlign.left);
    textRenderer.renderText(node, canvas);
  }

  @Test
  public void testRenderOneLineTextRightHAlign() {
    expectFontGetWidth("Hello");
    expect(font.getHeight()).andReturn(1).anyTimes();
    replay(font);

    node = createNodeMock();
    canvas = createCanvas(5, 4);
    textRenderer.initialize(font, "Hello");
    textRenderer.setTextHAlign(HAlign.right);
    textRenderer.renderText(node, canvas);
  }

  private void expectFontGetWidth(final String expected) {
    expect(font.getWidth(expected)).andReturn(expected.length()).anyTimes();
  }

  private NiftyNode createEmptyNodeMock() {
    NiftyNode node = createMock(NiftyNode.class);
    replay(node);
    return node;
  }

  private NiftyNode createNodeMock() {
    NiftyNode node = createMock(NiftyNode.class);
    expect(node.getHeight()).andReturn(10);
    expect(node.getWidth()).andReturn(10);
    replay(node);
    return node;
  }

  private NiftyCanvas createEmptyCanvas() {
    NiftyCanvas canvas = createMock(NiftyCanvas.class);
    replay(canvas);
    return canvas;
  }

  private NiftyCanvas createCanvas(final int x, final int y) {
    NiftyCanvas canvas = createMock(NiftyCanvas.class);
    canvas.text(font, x, y, "Hello");
    replay(canvas);
    return canvas;
  }
}
