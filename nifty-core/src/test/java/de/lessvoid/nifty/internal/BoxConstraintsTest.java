package de.lessvoid.nifty.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.BoxConstraints;

public class BoxConstraintsTest {

  @Test
  public void testDefaultConstructor() {
    BoxConstraints box = new BoxConstraints();
    assertNull(box.getX());
    assertNull(box.getY());
    assertNull(box.getWidth());
    assertNull(box.getHeight());
    assertEquals(HorizontalAlignment.horizontalDefault, box.getHorizontalAlign());
    assertEquals(VerticalAlignment.verticalDefault, box.getVerticalAlign());
  }

  @Test
  public void testNormalConstructor() {
    BoxConstraints box = new BoxConstraints(
        new UnitValue("100px"),
        new UnitValue("200px"),
        new UnitValue("300px"),
        new UnitValue("400px"),
        HorizontalAlignment.right,
        VerticalAlignment.bottom);
    assertEquals("100px", box.getX().toString());
    assertEquals("200px", box.getY().toString());
    assertEquals("300px", box.getWidth().toString());
    assertEquals("400px", box.getHeight().toString());
    assertEquals(HorizontalAlignment.right, box.getHorizontalAlign());
    assertEquals(VerticalAlignment.bottom, box.getVerticalAlign());
  }

  @Test
  public void testCopyConstructor() {
    BoxConstraints box = new BoxConstraints();
    BoxConstraints copy = new BoxConstraints(box);
    assertNull(copy.getX());
    assertNull(copy.getY());
    assertNull(copy.getWidth());
    assertNull(copy.getHeight());
    assertEquals(HorizontalAlignment.horizontalDefault, copy.getHorizontalAlign());
    assertEquals(VerticalAlignment.verticalDefault, copy.getVerticalAlign());
  }

  @Test
  public void testSetter() {
    BoxConstraints b = new BoxConstraints();
    b.setX(new UnitValue("100px"));
    b.setY(new UnitValue("200px"));
    b.setWidth(new UnitValue("300px"));
    b.setHeight(new UnitValue("400px"));
    b.setHorizontalAlign(HorizontalAlignment.right);
    b.setVerticalAlign(VerticalAlignment.bottom);
    assertEquals("100px", b.getX().toString());
    assertEquals("200px", b.getY().toString());
    assertEquals("300px", b.getWidth().toString());
    assertEquals("400px", b.getHeight().toString());
    assertEquals(HorizontalAlignment.right, b.getHorizontalAlign());
    assertEquals(VerticalAlignment.bottom, b.getVerticalAlign());
  }
}
