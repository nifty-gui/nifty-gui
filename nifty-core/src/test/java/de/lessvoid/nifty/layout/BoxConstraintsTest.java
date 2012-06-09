package de.lessvoid.nifty.layout;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

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

}
