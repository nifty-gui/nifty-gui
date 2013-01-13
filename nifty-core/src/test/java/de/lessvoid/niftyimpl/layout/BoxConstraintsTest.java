package de.lessvoid.niftyimpl.layout;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.HorizontalAlign;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.nifty.layout.VerticalAlign;
import de.lessvoid.niftyimpl.layout.BoxConstraints;

public class BoxConstraintsTest extends TestCase {

  public void testDefaultConstructor() {
    BoxConstraints box = new BoxConstraints();
    assertNull(box.getX());
    assertNull(box.getY());
    assertNull(box.getWidth());
    assertNull(box.getHeight());
    assertEquals(HorizontalAlign.horizontalDefault, box.getHorizontalAlign());
    assertEquals(VerticalAlign.verticalDefault, box.getVerticalAlign());
  }

  public void testNormalConstructor() {
    BoxConstraints box = new BoxConstraints(
        new SizeValue("100px"),
        new SizeValue("200px"),
        new SizeValue("300px"),
        new SizeValue("400px"),
        HorizontalAlign.right,
        VerticalAlign.bottom);
    assertEquals("100px", box.getX().toString());
    assertEquals("200px", box.getY().toString());
    assertEquals("300px", box.getWidth().toString());
    assertEquals("400px", box.getHeight().toString());
    assertEquals(HorizontalAlign.right, box.getHorizontalAlign());
    assertEquals(VerticalAlign.bottom, box.getVerticalAlign());
  }

  public void testCopyConstructor() {
    BoxConstraints box = new BoxConstraints();
    BoxConstraints copy = new BoxConstraints(box);
    assertNull(copy.getX());
    assertNull(copy.getY());
    assertNull(copy.getWidth());
    assertNull(copy.getHeight());
    assertEquals(HorizontalAlign.horizontalDefault, copy.getHorizontalAlign());
    assertEquals(VerticalAlign.verticalDefault, copy.getVerticalAlign());
  }

  public void testSetter() {
    BoxConstraints b = new BoxConstraints();
    b.setX(new SizeValue("100px"));
    b.setY(new SizeValue("200px"));
    b.setWidth(new SizeValue("300px"));
    b.setHeight(new SizeValue("400px"));
    b.setHorizontalAlign(HorizontalAlign.right);
    b.setVerticalAlign(VerticalAlign.bottom);
    assertEquals("100px", b.getX().toString());
    assertEquals("200px", b.getY().toString());
    assertEquals("300px", b.getWidth().toString());
    assertEquals("400px", b.getHeight().toString());
    assertEquals(HorizontalAlign.right, b.getHorizontalAlign());
    assertEquals(VerticalAlign.bottom, b.getVerticalAlign());
  }
}
