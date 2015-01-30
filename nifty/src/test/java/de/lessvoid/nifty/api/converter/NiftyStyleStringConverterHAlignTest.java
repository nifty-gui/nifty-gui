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
package de.lessvoid.nifty.api.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.converter.NiftyStyleStringConverterHAlign;

public class NiftyStyleStringConverterHAlignTest {
  private final NiftyStyleStringConverterHAlign converter = new NiftyStyleStringConverterHAlign();

  @Test
  public void testToString() {
    assertEquals("horizontalDefault", converter.toString(HAlign.horizontalDefault));
    assertEquals("left", converter.toString(HAlign.left));
    assertEquals("center", converter.toString(HAlign.center));
    assertEquals("right", converter.toString(HAlign.right));
  }

  @Test
  public void testFromString() {
    assertEquals(HAlign.horizontalDefault, converter.fromString("horizontalDefault"));
    assertEquals(HAlign.left, converter.fromString("left"));
    assertEquals(HAlign.center, converter.fromString("center"));
    assertEquals(HAlign.right, converter.fromString("right"));
  }
}
