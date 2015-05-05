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
package de.lessvoid.nifty.internal.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyFont;

public class TextRendererMultipleLinesTest {
  private TextRenderer textRenderer;
  private NiftyFont font;

  @Before
  public void before() {
    textRenderer = new TextRenderer();
    font = createMock(NiftyFont.class);
  }

  @After
  public void after() {
    verify(font);
  }

  @Test
  public void testTextWidthEmpty() {
    replay(font);

    textRenderer.initialize(font, "");
    assertEquals(0, textRenderer.getTextWidth());
  }

  @Test
  public void testTextWidthSingleLine() {
    expectFontGetWidth("Hello");
    replay(font);

    textRenderer.initialize(font, "Hello");
    assertEquals(5, textRenderer.getTextWidth());
  }

  @Test
  public void testTextWidthMultipleLines() {
    expectFontGetWidth("Hello");
    expectFontGetWidth("Nifty World");
    replay(font);

    textRenderer.initialize(font, "Hello\nNifty World");
    assertEquals(11, textRenderer.getTextWidth());
  }

  private void expectFontGetWidth(final String expected) {
    expect(font.getWidth(expected)).andReturn(expected.length());
  }

}
