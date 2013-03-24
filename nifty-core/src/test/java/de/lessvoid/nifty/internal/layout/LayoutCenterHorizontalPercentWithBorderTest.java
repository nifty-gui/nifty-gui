package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;
import de.lessvoid.nifty.internal.layout.LayoutCenter;

public class LayoutCenterHorizontalPercentWithBorderTest {
  private LayoutCenter layout;
  private Box rootBox;
  private Box box;
  private BoxConstraints constraint;
  private BoxConstraints rootBoxConstraints;

  @Before
  public void setUp() throws Exception {
    layout = new LayoutCenter();

    rootBox = new Box(0, 0, 640, 480);

    rootBoxConstraints = new BoxConstraints();
    rootBoxConstraints.setPadding(new UnitValue("50px"));

    box = new Box();

    constraint = new BoxConstraints();
    constraint.setWidth(new UnitValue("50%"));
  }

  @Test
  public void testHorizontalAlignLeftPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlignment.left);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    Assert.assertBoxLeftWidth(box, 50, width);
  }

  @Test
  public void testHorizontalAlignRightPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlignment.right);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    Assert.assertBoxLeftWidth(box, 640 - width, width);
  }

  @Test
  public void testHorizontalAlignCenterPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlignment.center);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50 + ((640 - 100) / 2) / 2, (640 - 100) / 2);
  }
}
