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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutVertical;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutVerticalMarginTest {
  private InternalLayoutVertical layout= new InternalLayoutVertical();
  private InternalLayoutableTestImpl root;
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl top;
  private InternalLayoutableTestImpl bottom;

  @Before
  public void before() throws Exception {
    root = new InternalLayoutableTestImpl(new Box(0, 0, 100, 200), new InternalBoxConstraints());
    elements = new ArrayList<InternalLayoutable>();
    top = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    elements.add(top);
    bottom = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    elements.add(bottom);
  }

  @Test
  public void testTopMargin() throws Exception {
    top.getBoxConstraints().setMarginTop(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 0, 50, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 150, 100, 100);
  }

  @Test
  public void testBottomMargin() throws Exception {
    top.getBoxConstraints().setMarginBottom(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 0, 0, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 150, 100, 100);
  }

  @Test
  public void testLeftMargin() throws Exception {
    top.getBoxConstraints().setMarginLeft(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 50, 0, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 100, 100, 100);
  }
}
