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
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutHorizontal;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutHorizontalTest {
  private InternalLayoutHorizontal layout = new InternalLayoutHorizontal();
  private InternalLayoutableTestImpl rootPanel;
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl left;
  private InternalLayoutableTestImpl right;

  @Before
  public void setUp() throws Exception {
    rootPanel = new InternalLayoutableTestImpl(new Box(0, 0, 640, 480), new InternalBoxConstraints());
    left = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    right = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());

    elements = new ArrayList<InternalLayoutable>();
    elements.add(left);
    elements.add(right);
  }

  @Test
  public void testUpdateEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  @Test
  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements(rootPanel, null);
  }

  @Test
  public void testUpdateWithEmptyChildren() {
    layout.layoutElements(rootPanel, new ArrayList<InternalLayoutable>());
  }

  @Test
  public void testLayoutDefault() {
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 320, 480);
    Assert.assertBox(right.getLayoutPos(), 320, 0, 320, 480);
  }

  @Test
  public void testLayoutFixedHeight() {
    left.getBoxConstraints().setHeight(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 320, 20);
    Assert.assertBox(right.getLayoutPos(), 320, 0, 320, 480);
  }

  @Test
  public void testLayoutMaxHeight() {
    left.getBoxConstraints().setHeight(new UnitValue("100%"));
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 320, 480);
    Assert.assertBox(right.getLayoutPos(), 320, 0, 320, 480);
  }

  @Test
  public void testLayoutMaxHeightWildcard() {
    left.getBoxConstraints().setHeight(new UnitValue("*"));
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 320, 480);
    Assert.assertBox(right.getLayoutPos(), 320, 0, 320, 480);
  }

  @Test
  public void testLayoutFixedWidth() {
    left.getBoxConstraints().setWidth(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 20, 480);
  }

  @Test
  public void testLayoutFixedWidthTopAlign() {
    left.getBoxConstraints().setWidth(new UnitValue("20px"));
    left.getBoxConstraints().setVerticalAlign(VAlign.top);
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 20, 480);
  }

  @Test
  public void testLayoutFixedHeightCenterAlign() {
    left.getBoxConstraints().setHeight(new UnitValue("20px"));
    left.getBoxConstraints().setVerticalAlign(VAlign.center);
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 230, 320, 20);
  }

  @Test
  public void testLayoutFixedHeightBottomAlign() {
    left.getBoxConstraints().setHeight(new UnitValue("20px"));
    left.getBoxConstraints().setVerticalAlign(VAlign.bottom);
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 460, 320, 20);
  }

  @Test
  public void testLayoutWithPercentage() throws Exception {
    left.getBoxConstraints().setWidth(new UnitValue("25%"));
    right.getBoxConstraints().setWidth(new UnitValue("75%"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 160, 480);
    Assert.assertBox(right.getLayoutPos(), 160, 0, 480, 480);
  }

  @Test
  public void testLayoutWithMixedFixedAndPercentage() throws Exception {
    left.getBoxConstraints().setWidth(new UnitValue("40px"));
    right.getBoxConstraints().setWidth(new UnitValue("*"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 40, 480);
    Assert.assertBox(right.getLayoutPos(), 40, 0, 600, 480);
  }

  @Test
  public void testLayoutDefaultWithAllEqualPadding() {
    rootPanel.getBoxConstraints().setPadding(new UnitValue("10px"));
    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(), 10, 10, 310, 460);
    Assert.assertBox(right.getLayoutPos(), 320, 10, 310, 460);
  }

  @Test
  public void testLayoutWithWidthSuffixLeft() {
    left.getBoxConstraints().setWidth(new UnitValue("50%h"));
    left.getBoxConstraints().setHeight(new UnitValue("200px"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(),    0, 0, 100, 200);
    Assert.assertBox(right.getLayoutPos(), 100, 0, 540, 480);
  }

  @Test
  public void testLayoutWithWidthSuffixRight() {
    right.getBoxConstraints().setWidth(new UnitValue("50%h"));
    right.getBoxConstraints().setHeight(new UnitValue("200px"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(),    0, 0, 540, 480);
    Assert.assertBox(right.getLayoutPos(), 540, 0, 100, 200);
  }

  @Test
  public void testLayoutWithHeightSuffixLeft() {
    left.getBoxConstraints().setWidth(new UnitValue("200px"));
    left.getBoxConstraints().setHeight(new UnitValue("50%w"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(),    0, 0, 200, 100);
    Assert.assertBox(right.getLayoutPos(), 200, 0, 440, 480);
  }

  @Test
  public void testLayoutWithHeightSuffixRight() {
    right.getBoxConstraints().setWidth(new UnitValue("200px"));
    right.getBoxConstraints().setHeight(new UnitValue("50%w"));

    layout.layoutElements(rootPanel, elements);

    Assert.assertBox(left.getLayoutPos(),    0, 0, 440, 480);
    Assert.assertBox(right.getLayoutPos(), 440, 0, 200, 100);
  }
}
