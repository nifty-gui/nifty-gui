package de.lessvoid.nifty.layout.manager;

import static de.lessvoid.nifty.layout.manager.BoxTestHelper.assertBox;
import static de.lessvoid.nifty.layout.manager.BoxTestHelper.initBox;

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
    initBox(box, 100, 100, 400, 10);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 100, 100, 200, 10);
  }

  @Test
  public void testHeight() {
    initBox(box, 100, 100, 40, 400);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 100, 100, 40, 200);
  }

  @Test
  public void testX() {
    initBox(box, 10, 100, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testY() {
    initBox(box, 100, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testXWidth() {
    initBox(box, 400, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 260, 100, 40, 40);
  }

  @Test
  public void testXHeight() {
    initBox(box, 100, 400, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    assertBox(box, 100, 260, 40, 40);
  }
}
