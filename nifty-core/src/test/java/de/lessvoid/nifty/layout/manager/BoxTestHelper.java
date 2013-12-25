package de.lessvoid.nifty.layout.manager;

import de.lessvoid.nifty.layout.Box;

import static org.junit.Assert.assertEquals;

public class BoxTestHelper {
  public static void initBox(final Box box, final int x, final int y, final int width, final int height) {
    box.setX(x);
    box.setY(y);
    box.setWidth(width);
    box.setHeight(height);
  }

  public static void assertBox(final Box box, final int x, final int y, final int width, final int height) {
    assertEquals(x, box.getX());
    assertEquals(y, box.getY());
    assertEquals(width, box.getWidth());
    assertEquals(height, box.getHeight());
  }
}
