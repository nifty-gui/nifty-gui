package de.lessvoid.nifty.layout.manager;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout.KeepInsidePostProcess;

public class KeepInsidePostProcessTest {
  private KeepInsidePostProcess keepInside = new KeepInsidePostProcess();
  private int rootBoxX = 100;
  private int rootBoxY = 100;
  private int rootBoxWidth = 200;
  private int rootBoxHeight = 200;
  private Box box = new Box();

  @Test
  public void testWidth() {
    initBox(100, 100, 400, 10);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(100, 100, 200, 10);
  }

  @Test
  public void testHeight() {
    initBox(100, 100, 40, 400);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(100, 100, 40, 200);
  }

  @Test
  public void testX() {
    initBox(10, 100, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(100, 100, 40, 40);
  }

  @Test
  public void testY() {
    initBox(100, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(100, 100, 40, 40);
  }

  @Test
  public void testXWidth() {
    initBox(400, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(260, 100, 40, 40);
  }

  @Test
  public void testXHeight() {
    initBox(100, 400, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(100, 260, 40, 40);
  }

  private void initBox(final int x, final int y, final int width, final int height) {
    box.setX(x);
    box.setY(y);
    box.setWidth(width);
    box.setHeight(height);
  }

  private void assertBox(final int x, final int y, final int width, final int height) {
    assertEquals(box.getX(), x);
    assertEquals(box.getY(), y);
    assertEquals(box.getWidth(), width);
    assertEquals(box.getHeight(), height);
  }
}
