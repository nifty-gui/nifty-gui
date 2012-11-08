package de.lessvoid.niftyimpl.layout;

import junit.framework.TestCase;
import de.lessvoid.niftyimpl.NiftyElementImpl;

public class LayoutPartTest extends TestCase {

  public void testDefaultConstruction() {
    LayoutPart part = new NiftyElementImpl();
    assertNotNull(part.getLayoutPos());
    assertNotNull(part.getBoxConstraints());
  }

  public void testConstruction() {
    Box box = new Box();
    BoxConstraints constraints = new BoxConstraints();
    LayoutPart part = new NiftyElementImpl("dummy", box, constraints);

    assertEquals(box, part.getLayoutPos());
    assertEquals(constraints, part.getBoxConstraints());
  }
}
