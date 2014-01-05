package de.lessvoid.nifty.internal.layout;

import org.junit.Test;

import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalLayoutAbsolute.KeepInsidePostProcess;

public class InternalKeepInsidePostProcessTest {
  private KeepInsidePostProcess keepInside = new KeepInsidePostProcess();
  private int rootBoxX = 100;
  private int rootBoxY = 100;
  private int rootBoxWidth = 200;
  private int rootBoxHeight = 200;
  private Box box = new Box();

  @Test
  public void testWidth() {
    Assert.initBox(box, 100, 100, 400, 10);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 200, 10);
  }

  @Test
  public void testHeight() {
    Assert.initBox(box, 100, 100, 40, 400);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 200);
  }

  @Test
  public void testX() {
    Assert.initBox(box, 10, 100, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testY() {
    Assert.initBox(box, 100, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 100, 40, 40);
  }

  @Test
  public void testXWidth() {
    Assert.initBox(box, 400, 10, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 260, 100, 40, 40);
  }

  @Test
  public void testXHeight() {
    Assert.initBox(box, 100, 400, 40, 40);
    keepInside.process(rootBoxX, rootBoxY, rootBoxWidth, rootBoxHeight, box);
    Assert.assertBox(box, 100, 260, 40, 40);
  }
}
