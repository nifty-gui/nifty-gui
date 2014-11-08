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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutVertical;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutVerticalTest {
  private InternalLayoutVertical layout = new InternalLayoutVertical();
  private InternalLayoutableTestImpl root;
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl top;
  private InternalLayoutableTestImpl bottom;

  @Before
  public void setUp() throws Exception {
    root = new InternalLayoutableTestImpl(new Box(0, 0, 640, 480), new InternalBoxConstraints());
    elements = new ArrayList<InternalLayoutable>();
    top = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    elements.add(top);
    bottom = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    elements.add(bottom);
  }

  @Test
  public void testUpdateEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  @Test
  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements(root, null);
  }

  @Test
  public void testUpdateWithWmptyList() {
    layout.layoutElements(root, new ArrayList<InternalLayoutable>());
  }

  @Test
  public void testLayoutDefault() {
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 240);
    Assert.assertBox(bottom.getLayoutPos(), 0, 240, 640, 240);
  }

  @Test
  public void testLayoutFixedHeight() {
    top.getBoxConstraints().setHeight(new UnitValue("20px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 20);
    Assert.assertBox(bottom.getLayoutPos(), 0, 20, 640, 460);
  }

  @Test
  public void testLayoutFixedWidth() {
    top.getBoxConstraints().setWidth(new UnitValue("20px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 20, 240);
  }

  @Test
  public void testLayoutMaxWidth() {
    top.getBoxConstraints().setWidth(new UnitValue("100%"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 240);
  }

  @Test
  public void testLayoutFixedWidthRightAlign() {
    top.getBoxConstraints().setWidth(new UnitValue("20px"));
    top.getBoxConstraints().setHorizontalAlign(HAlign.right);
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 620, 0, 20, 240);
  }

  @Test
  public void testLayoutFixedWidthLeftAlign() {
    top.getBoxConstraints().setWidth(new UnitValue("20px"));
    top.getBoxConstraints().setHorizontalAlign(HAlign.left);
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 20, 240);
  }

  @Test
  public void testLayoutMaxWidthWildcard() {
    top.getBoxConstraints().setWidth(new UnitValue("*"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 240);
  }

  @Test
  public void testLayoutWildcardHeight() {
    top.getBoxConstraints().setHeight(new UnitValue("*"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 240);
    Assert.assertBox(bottom.getLayoutPos(), 0, 240, 640, 240);
  }

  @Test
  public void testLayoutAllFixedHeight() {
    top.getBoxConstraints().setHeight(new UnitValue("100px"));
    bottom.getBoxConstraints().setHeight(new UnitValue("100px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 100, 640, 100);
  }

  @Test
  public void testLayoutFixedWidthCenterAlign() {
    top.getBoxConstraints().setWidth(new UnitValue("20px"));
    top.getBoxConstraints().setHorizontalAlign(HAlign.center);
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 310, 0, 20, 240);
  }

  @Test
  public void testLayoutWithPercentage() throws Exception {
    top.getBoxConstraints().setHeight(new UnitValue("25%"));
    bottom.getBoxConstraints().setHeight(new UnitValue("75%"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 120);
    Assert.assertBox(bottom.getLayoutPos(), 0, 120, 640, 360);
  }

  @Test
  public void testLayoutWithPaddingAllEqual() {
    root.getBoxConstraints().setPadding(new UnitValue("10px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 10, 10, 620, 230);
    Assert.assertBox(bottom.getLayoutPos(), 10, 240, 620, 230);
  }

  @Test
  public void testLayoutWithPaddingLeft() {
    root.getBoxConstraints().setPaddingLeft(new UnitValue("10px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 10, 0, 630, 240);
    Assert.assertBox(bottom.getLayoutPos(), 10, 240, 630, 240);
  }

  @Test
  public void testLayoutWithPaddingRight() {
    root.getBoxConstraints().setPaddingRight(new UnitValue("10px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 630, 240);
    Assert.assertBox(bottom.getLayoutPos(), 0, 240, 630, 240);
  }

  @Test
  public void testLayoutWithPaddingTop() {
    root.getBoxConstraints().setPaddingTop(new UnitValue("10px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 10, 640, 235);
    Assert.assertBox(bottom.getLayoutPos(), 0, 245, 640, 235);
  }

  @Test
  public void testLayoutWithPaddingBottom() {
    root.getBoxConstraints().setPaddingBottom(new UnitValue("10px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 235);
    Assert.assertBox(bottom.getLayoutPos(), 0, 235, 640, 235);
  }

  @Test
  public void testLayoutHeightSuffix() {
    top.getBoxConstraints().setWidth(new UnitValue("50%h"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 120, 240);
    Assert.assertBox(bottom.getLayoutPos(), 0, 240, 640, 240);
  }

  @Test
  public void testLayoutWidthWithHeightSuffix() {
    top.getBoxConstraints().setWidth(new UnitValue("50%h"));
    top.getBoxConstraints().setHeight(new UnitValue("100px"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 50, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 100, 640, 380);
  }

  @Test
  public void testLayoutWidthSuffix() {
    top.getBoxConstraints().setHeight(new UnitValue("50%w"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 640, 320);
    Assert.assertBox(bottom.getLayoutPos(), 0, 320, 640, 240);
  }

  @Test
  public void testLayoutHeightWithWidthSuffix() {
    top.getBoxConstraints().setWidth(new UnitValue("100px"));
    top.getBoxConstraints().setHeight(new UnitValue("50%w"));
    performLayout();
    Assert.assertBox(top.getLayoutPos(), 0, 0, 100, 50);
    Assert.assertBox(bottom.getLayoutPos(), 0, 50, 640, 240);
  }

  private void performLayout() {
    layout.layoutElements(root, elements);
  }
}
