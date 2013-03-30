package de.lessvoid.nifty.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.InternalBoxConstraints;

public class InternalBoxConstraintsTest {

  @Test
  public void testDefaultConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints();
    assertNull(box.getX());
    assertNull(box.getY());
    assertNull(box.getWidth());
    assertNull(box.getHeight());
    assertEquals(HorizontalAlignment.horizontalDefault, box.getHorizontalAlign());
    assertEquals(VerticalAlignment.verticalDefault, box.getVerticalAlign());
  }

  @Test
  public void testNormalConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints(
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
  public void testToString() {
    InternalBoxConstraints box = new InternalBoxConstraints(
        new UnitValue("100px"),
        new UnitValue("200px"),
        new UnitValue("300px"),
        new UnitValue("400px"),
        HorizontalAlignment.right,
        VerticalAlignment.bottom);
    assertTrue(box.toString().startsWith(
        "InternalBoxConstraints [x=100px, y=200px, width=300px, height=400px, " +
        "horizontalAlign=right, verticalAlign=bottom, " +
        "paddingLeft=0px, paddingRight=0px, paddingTop=0px, paddingBottom=0px, " +
        "marginLeft=0px, marginRight=0px, marginTop=0px, marginBottom=0px] "));
  }

  @Test
  public void testCopyConstructor() {
    InternalBoxConstraints box = new InternalBoxConstraints();
    InternalBoxConstraints copy = new InternalBoxConstraints(box);
    assertNull(copy.getX());
    assertNull(copy.getY());
    assertNull(copy.getWidth());
    assertNull(copy.getHeight());
    assertEquals(HorizontalAlignment.horizontalDefault, copy.getHorizontalAlign());
    assertEquals(VerticalAlignment.verticalDefault, copy.getVerticalAlign());
  }

  @Test
  public void testSetter() {
    InternalBoxConstraints b = new InternalBoxConstraints();
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
