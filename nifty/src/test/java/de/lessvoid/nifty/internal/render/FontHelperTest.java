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

import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.anyFloat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyFont;

public class FontHelperTest {
  private FontHelper fontHelper = new FontHelper();
  private NiftyFont font;

  @Before
  public void before() {
    font = createMock(NiftyFont.class);
    expect(font.getCharacterWidth(anyChar(), anyChar(), anyFloat())).andReturn(10).anyTimes();
    replay(font);
  }

  @After
  public void after() {
    verify(font);
  }

  @Test
  public void testHitFirstChar() {
    assertEquals(0, fontHelper.getVisibleCharactersFromStart(font, "Hello", 0, 1.f));
  }

  @Test
  public void testHitFirstCharInBetween() {
    assertEquals(0, fontHelper.getVisibleCharactersFromStart(font, "Hello", 5, 1.f));
  }

  @Test
  public void testHitSecondChar() {
    assertEquals(1, fontHelper.getVisibleCharactersFromStart(font, "Hello", 10, 1.f));
  }

  @Test
  public void testHitLastChar() {
    assertEquals(5, fontHelper.getVisibleCharactersFromStart(font, "Hello", 1000, 1.f));
  }

}
