package de.lessvoid.niftyimpl.layout;

import de.lessvoid.niftyimpl.layout.Box;
import junit.framework.TestCase;

public class BoxTest extends TestCase {

  public void testDefaultConstructor() {
    Box box = new Box();
    assertEquals(0, box.getX());
    assertEquals(0, box.getY());
    assertEquals(0, box.getWidth());
    assertEquals(0, box.getHeight());
  }

  public void testHashcode() {
    Box box = new Box();
    assertEquals(923521L, box.hashCode());
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

  public void testFrom() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box();
    copy.from(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  public void testSetter() {
    Box copy = new Box();
    copy.setX(100);
    copy.setY(200);
    copy.setWidth(300);
    copy.setHeight(400);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  public void testEquals() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertTrue(box.equals(copy));
  }

  public void testEqualsSame() {
    Box box = new Box(100, 200, 300, 400);
    assertTrue(box.equals(box));
  }

  public void testNotEqualsNull() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(null));
  }

  public void testNotEqualsWrongClass() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(new Object()));
  }

  public void testNotEqualsWrongX() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(101, 200, 300, 400);
    assertFalse(a.equals(b));
  }

  public void testNotEqualsWrongY() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 201, 300, 400);
    assertFalse(a.equals(b));
  }

  public void testNotEqualsWrongWidth() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 301, 400);
    assertFalse(a.equals(b));
  }

  public void testNotEqualsWrongHeight() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 300, 401);
    assertFalse(a.equals(b));
  }
}
