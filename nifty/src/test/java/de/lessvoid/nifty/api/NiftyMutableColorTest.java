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

import static de.lessvoid.nifty.AssertColor.assertColor;

import de.lessvoid.nifty.api.types.NiftyColor;
import de.lessvoid.nifty.api.types.NiftyMutableColor;
import org.junit.Test;

public class NiftyMutableColorTest {
  private NiftyMutableColor color = new NiftyMutableColor(NiftyColor.transparent());

  @Test
  public void testSet() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, color.setRed(0.1f).setGreen(0.2f).setBlue(0.3f).setAlpha(0.4f));
  }

  @Test
  public void testUpdate() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, color.update(new NiftyColor(0.1f, 0.2f, 0.3f, 0.4f)));
  }

  @Test
  public void testLinear() {
    NiftyColor start = NiftyColor.none();
    NiftyColor end = NiftyColor.white();
    assertColor(0.5f, 0.5f, 0.5f, 0.5f, color.linear(start, end, 0.5f));
  }

  @Test
  public void testMultiply() {
    assertColor(0.f, 0.f, 0.f, 0.f, color.mutiply(0.25f));
  }

}
