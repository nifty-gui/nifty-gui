package de.lessvoid.nifty.layout;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class BoxConstraintsTest extends TestCase {

  public void testDefaultConstructor() {
    BoxConstraints box = new BoxConstraints();
    assertTrue(box.getX().hasDefault());
    assertTrue(box.getY().hasDefault());
    assertTrue(box.getWidth().hasDefault());
    assertTrue(box.getHeight().hasDefault());
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
    assertTrue(box.getX().hasDefault());
    assertTrue(box.getY().hasDefault());
    assertTrue(box.getWidth().hasDefault());
    assertTrue(box.getHeight().hasDefault());
    assertEquals(HorizontalAlign.horizontalDefault, copy.getHorizontalAlign());
    assertEquals(VerticalAlign.verticalDefault, copy.getVerticalAlign());
  }

}
