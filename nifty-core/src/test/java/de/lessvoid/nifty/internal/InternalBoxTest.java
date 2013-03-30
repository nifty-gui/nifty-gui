package de.lessvoid.nifty.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.internal.InternalBox;

public class InternalBoxTest {

  @Test
  public void testDefaultConstructor() {
    InternalBox box = new InternalBox();
    assertEquals(0, box.getX());
    assertEquals(0, box.getY());
    assertEquals(0, box.getWidth());
    assertEquals(0, box.getHeight());
  }

  @Test
  public void testHashcode() {
    InternalBox box = new InternalBox();
    assertEquals(923521L, box.hashCode());
  }

  @Test
  public void testNormalConstructor() {
    InternalBox box = new InternalBox(100, 100, 200, 200);
    assertEquals(100, box.getX());
    assertEquals(100, box.getY());
    assertEquals(200, box.getWidth());
    assertEquals(200, box.getHeight());
  }

  @Test
  public void testToString() {
    InternalBox box = new InternalBox(100, 100, 200, 200);
    assertTrue(box.toString().startsWith("InternalBox [x=100, y=100, width=200, height=200] "));
  }

  @Test
  public void testCopyConstructor() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    InternalBox copy = new InternalBox(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testFrom() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    InternalBox copy = new InternalBox();
    copy.from(box);
    assertEquals(100, copy.getX());
    assertEquals(200, copy.getY());
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testSetter() {
    InternalBox copy = new InternalBox();
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
  public void testSetDimenstion() {
    InternalBox copy = new InternalBox();
    copy.setDimension(300, 400);
    assertEquals(300, copy.getWidth());
    assertEquals(400, copy.getHeight());
  }

  @Test
  public void testEquals() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    InternalBox copy = new InternalBox(box);
    assertTrue(box.equals(copy));
  }

  @Test
  public void testEqualsSame() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    assertTrue(box.equals(box));
  }

  @Test
  public void testNotEqualsNull() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    assertFalse(box.equals(null));
  }

  @Test
  public void testNotEqualsWrongClass() {
    InternalBox box = new InternalBox(100, 200, 300, 400);
    assertFalse(box.equals(new Object()));
  }

  @Test
  public void testNotEqualsWrongX() {
    InternalBox a = new InternalBox(100, 200, 300, 400);
    InternalBox b = new InternalBox(101, 200, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongY() {
    InternalBox a = new InternalBox(100, 200, 300, 400);
    InternalBox b = new InternalBox(100, 201, 300, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongWidth() {
    InternalBox a = new InternalBox(100, 200, 300, 400);
    InternalBox b = new InternalBox(100, 200, 301, 400);
    assertFalse(a.equals(b));
  }

  @Test
  public void testNotEqualsWrongHeight() {
    InternalBox a = new InternalBox(100, 200, 300, 400);
    InternalBox b = new InternalBox(100, 200, 300, 401);
    assertFalse(a.equals(b));
  }
}
