package de.lessvoid.nifty.layout;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;

public class BoxConstraintsTest extends TestCase {

  public void testNormalConstructor() {
    BoxConstraints box = new BoxConstraints();
    assertNull( box.getX());
    assertNull( box.getY());
    assertNull( box.getWidth());
    assertNull( box.getHeight());
    assertEquals( HorizontalAlign.left, box.getHorizontalAlign());
    assertEquals( VerticalAlign.top, box.getVerticalAlign());
  }

}
