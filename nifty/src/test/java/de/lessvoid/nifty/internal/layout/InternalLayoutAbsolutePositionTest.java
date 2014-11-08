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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute.KeepInsidePostProcess;

public class InternalLayoutAbsolutePositionTest {
  private InternalLayoutAbsolute layout = new InternalLayoutAbsolute();
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl rootPanel;
  private InternalLayoutableTestImpl element;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    InternalBoxConstraints boxConstraint = new InternalBoxConstraints();
    rootPanel = new InternalLayoutableTestImpl(box, boxConstraint);

    box = new Box();
    boxConstraint = new InternalBoxConstraints();
    element = new InternalLayoutableTestImpl(box, boxConstraint);

    elements = new ArrayList<InternalLayoutable>();
    elements.add(element);
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
  public void testUpdateWithEmptyElements() {
    layout.layoutElements(rootPanel, new ArrayList<InternalLayoutable>());
  }

  @Test
  public void testLayoutFixedHeight() {
    element.getBoxConstraints().setHeight(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(20, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedWidth() {
    element.getBoxConstraints().setWidth(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(20, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutMissingWidthAndHeight() {
    try {
      layout.layoutElements(rootPanel, elements);
    } catch (IllegalArgumentException e) {
      assertEquals("InternalLayoutAbsolute requires at least width or height constraints set", e.getMessage());
    }
  }

  @Test
  public void testLayoutFixedX() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    element.getBoxConstraints().setWidth(UnitValue.px(10));
    element.getBoxConstraints().setHeight(UnitValue.px(10));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedY() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    element.getBoxConstraints().setWidth(UnitValue.px(10));
    element.getBoxConstraints().setHeight(UnitValue.px(10));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedXandY() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    element.getBoxConstraints().setY(new UnitValue("40px"));
    element.getBoxConstraints().setWidth(UnitValue.px(10));
    element.getBoxConstraints().setHeight(UnitValue.px(10));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(40, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithPercentageWidth() throws Exception {
    element.getBoxConstraints().setWidth(new UnitValue("25%"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(160, element.getLayoutPos().getWidth());
  }

  @Test
  public void testLayoutHeightSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("50%h"));
    element.getBoxConstraints().setHeight(new UnitValue("100px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(50, element.getLayoutPos().getWidth());
    assertEquals(100, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutHeightSuffixWithoutHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("50%h"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("100px"));
    element.getBoxConstraints().setHeight(new UnitValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(100, element.getLayoutPos().getWidth());
    assertEquals(50, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithoutWidth() {
    element.getBoxConstraints().setHeight(new UnitValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithKeepInsideMode() {
    element.getBoxConstraints().setX(new UnitValue("640px"));
    element.getBoxConstraints().setY(new UnitValue("480px"));
    element.getBoxConstraints().setWidth(new UnitValue("10px"));
    element.getBoxConstraints().setHeight(new UnitValue("10px"));

    layout = new InternalLayoutAbsolute(new KeepInsidePostProcess());
    layout.layoutElements(rootPanel, elements);

    assertEquals(630, element.getLayoutPos().getX());
    assertEquals(470, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }
}
