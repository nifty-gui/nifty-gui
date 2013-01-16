package de.lessvoid.niftyimpl.layout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BoxTest {

  @Test
  public void testDefaultConstructor() {
    Box box = new Box();
    assertEquals(0, box.getX());
    assertEquals(0, box.getY());
    assertEquals(0, box.getWidth());
    assertEquals(0, box.getHeight());
  }

  @Test
  public void testHashcode() {
    Box box = new Box();
    assertEquals(923521L, box.hashCode());
  }

  @Test
  public void testNormalConstructor() {
    Box box = new Box(100, 100, 200, 200);
    assertEquals(100, box.getX());
    assertEquals(100, box.getY());
    assertEquals(200, box.getWidth());
    assertEquals(200, box.getHeight());
  }

  @Test
  public void testCopyConstructor() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testFrom() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box();
    copy.from(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
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

  @Test
  public void testEquals() {
    Box box = new Box(100, 200, 300, 400);
    Box copy = new Box(box);
    assertTrue(box.equals(copy));
  }

  @Test
  public void testEqualsSame() {
    Box box = new Box(100, 200, 300, 400);
    assertTrue(box.equals(box));
  }

  @Test
  public void testNotEqualsNull() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(null));
  }

  @Test
  public void testNotEqualsWrongClass() {
    Box box = new Box(100, 200, 300, 400);
    assertFalse(box.equals(new Object()));
  }

  @Test
  public void testNotEqualsWrongX() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(101, 200, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongY() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 201, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongWidth() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 301, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongHeight() {
    Box a = new Box(100, 200, 300, 400);
    Box b = new Box(100, 200, 300, 401);
    assertFalse(a.equals(b));
  }
}
