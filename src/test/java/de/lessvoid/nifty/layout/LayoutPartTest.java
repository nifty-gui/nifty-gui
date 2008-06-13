package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;
import junit.framework.TestCase;

public class LayoutPartTest extends TestCase {

  public void testDefaultConstruction() {
    LayoutPart part = new LayoutPart();
    assertNotNull(part.getBox());
    assertNotNull(part.getBoxConstraints());
  }

  public void testConstruction() {
    Box box = new Box();
    BoxConstraints constraints = new BoxConstraints();
    LayoutPart part = new LayoutPart(box, constraints);

    assertEquals(box, part.getBox());
    assertEquals(constraints, part.getBoxConstraints());
  }

  public void testCopy() {
    Box box = new Box(100, 200, 300, 400);
    BoxConstraints constraints = new BoxConstraints(
      new SizeValue("100px"),
      new SizeValue("200px"),
      new SizeValue("300px"),
      new SizeValue("400px"),
      HorizontalAlign.right,
      VerticalAlign.bottom);

    LayoutPart part = new LayoutPart(box, constraints);
    LayoutPart copy = new LayoutPart(part);
    assertNotSame(copy.getBox(), part.getBox());
    assertNotSame(copy.getBoxConstraints(), part.getBoxConstraints());
    assertEquals(100, copy.getBox().getX());
    assertEquals(200, copy.getBox().getY());
    assertEquals(300, copy.getBox().getWidth());
    assertEquals(400, copy.getBox().getHeight());
    assertEquals("100px", copy.getBoxConstraints().getX().toString());
    assertEquals("200px", copy.getBoxConstraints().getY().toString());
    assertEquals("300px", copy.getBoxConstraints().getWidth().toString());
    assertEquals("400px", copy.getBoxConstraints().getHeight().toString());
    assertEquals(HorizontalAlign.right, copy.getBoxConstraints().getHorizontalAlign());
    assertEquals(VerticalAlign.bottom, copy.getBoxConstraints().getVerticalAlign());
  }
}
