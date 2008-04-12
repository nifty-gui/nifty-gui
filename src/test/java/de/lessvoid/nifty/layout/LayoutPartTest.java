package de.lessvoid.nifty.layout;

import junit.framework.TestCase;

public class LayoutPartTest extends TestCase {
  
  public void testDefaultConstruction() {
    LayoutPart part = new LayoutPart();
    assertNotNull( part.getBox());
    assertNotNull( part.getBoxConstraints());
  }
  
  public void testConstruction() {
    Box box = new Box();
    BoxConstraints constraints = new BoxConstraints();
    LayoutPart part = new LayoutPart( box, constraints );
    
    assertEquals( box, part.getBox());
    assertEquals( constraints, part.getBoxConstraints());
  }
}
