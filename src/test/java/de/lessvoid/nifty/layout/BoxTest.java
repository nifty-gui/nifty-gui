package de.lessvoid.nifty.layout;

import junit.framework.TestCase;

public class BoxTest extends TestCase {

  public void testDefaultConstructor() {
    Box box = new Box();
    assertEquals( 0, box.getX());
    assertEquals( 0, box.getY());
    assertEquals( 0, box.getWidth());
    assertEquals( 0, box.getHeight());
  }

  public void testNormalConstructor() {
    Box box = new Box( 100, 100, 200, 200 );
    assertEquals( 100, box.getX());
    assertEquals( 100, box.getY());
    assertEquals( 200, box.getWidth());
    assertEquals( 200, box.getHeight());
  }
}
