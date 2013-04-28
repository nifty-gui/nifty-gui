package de.lessvoid.nifty.internal.layout;

import static org.junit.Assert.assertEquals;
import de.lessvoid.nifty.internal.layout.InternalBox;

public class Assert {
  public static void initBox(final InternalBox box, final int x, final int y, final int width, final int height) {
    box.setX(x);
    box.setY(y);
    box.setWidth(width);
    box.setHeight(height);
  }

  public static void assertBox(final InternalBox box, final int x, final int y, final int width, final int height) {
    assertEquals(x, box.getX());
    assertEquals(y, box.getY());
    assertEquals(width, box.getWidth());
    assertEquals(height, box.getHeight());
  }

  public static void assertBoxLeftWidth(final InternalBox box, final int left, final int width) {
    assertEquals(left, box.getX());
    assertEquals(width, box.getWidth());
  }

  public static void assertBoxTopHeight(final InternalBox box, final int top, final int height) {
    assertEquals(top, box.getY());
    assertEquals(height, box.getHeight());
  }
}
