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
package de.lessvoid.nifty.internal.layout;

import org.junit.Test;

import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute.KeepInsidePostProcess;

public class InternalKeepInsidePostProcessTest {
  private KeepInsidePostProcess keepInside = new KeepInsidePostProcess();
  private int rootBoxX = 100;
  private int rootBoxY = 100;
  private int rootBoxWidth = 200;
  private int rootBoxHeight = 200;
  private Box box = new Box();

  @Test
  public void testWidth() {
    Assert.initBox(box, 100, 100, 400, 10);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 200, 10);
  }

  @Test
  public void testHeight() {
    Assert.initBox(box, 100, 100, 40, 400);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 200);
  }

  @Test
  public void testX() {
    Assert.initBox(box, 10, 100, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testY() {
    Assert.initBox(box, 100, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testXWidth() {
    Assert.initBox(box, 400, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 260, 100, 40, 40);
  }

  @Test
  public void testXHeight() {
    Assert.initBox(box, 100, 400, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 260, 40, 40);
  }
}
