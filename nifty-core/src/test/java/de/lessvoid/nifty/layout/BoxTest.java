package de.lessvoid.nifty.layout;

import junit.framework.TestCase;

public class BoxTest extends TestCase {

  public void testDefaultConstructor() {
    Box box = new Box();
    assertEquals(0, box.getX());
    assertEquals(0, box.getY());
    assertEquals(0, box.getWidth());
    assertEquals(0, box.getHeight());
  }

  public void testNormalConstructor() {
    Box box = new Box(100, 100, 200, 200);
    assertEquals(100, box.getX());
    assertEquals(100, box.getY());
    assertEquals(200, box.getWidth());
    assertEquals(200, box.getHeight());
  }

  public void testCopyConstructor() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }
}
