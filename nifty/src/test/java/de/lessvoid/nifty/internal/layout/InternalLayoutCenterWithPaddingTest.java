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
package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutCenterWithPaddingTest {
  private InternalLayoutableTestImpl rootPanel;
  private InternalLayoutCenter layout;
  private InternalBoxConstraints constraint;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    InternalBoxConstraints boxConstraint = new InternalBoxConstraints();

    rootPanel = new InternalLayoutableTestImpl(box, boxConstraint);
    rootPanel.getBoxConstraints().setPadding(new UnitValue("50px"));

    layout = new InternalLayoutCenter();
    constraint = new InternalBoxConstraints();
  }

  @Test
  public void testHorizontalAlignCenterWithBorder() throws Exception {
    constraint.setWidth(new UnitValue("100px"));
    constraint.setHeight(new UnitValue("100px"));
    constraint.setHorizontalAlign(HAlign.center);

    InternalLayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testHorizontalAlignCenterWithBorderNoConstraint() throws Exception {
    constraint.setHorizontalAlign(HAlign.center);

    InternalLayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 50, 50, 540, 380);
  }

  private InternalLayoutableTestImpl prepare(final InternalBoxConstraints constraint) {
    Box box = new Box();
    InternalLayoutableTestImpl child = new InternalLayoutableTestImpl(box, constraint);
    ArrayList<InternalLayoutable> elements = new ArrayList<InternalLayoutable>();
    elements.add(child);
    layout.layoutElements(rootPanel, elements);
    return child;
  }
}
