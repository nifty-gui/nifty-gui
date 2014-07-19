package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;

public class InternalLayoutCenterHorizontalPercentWithBorderTest {
  private InternalLayoutCenter layout;
  private Box rootBox;
  private Box box;
  private InternalBoxConstraints constraint;
  private InternalBoxConstraints rootBoxConstraints;

  @Before
  public void setUp() throws Exception {
    layout = new InternalLayoutCenter();

    rootBox = new Box(0, 0, 640, 480);

    rootBoxConstraints = new InternalBoxConstraints();
    rootBoxConstraints.setPadding(new UnitValue("50px"));

    box = new Box();

    constraint = new InternalBoxConstraints();
    constraint.setWidth(new UnitValue("50%"));
  }

  @Test
  public void testHorizontalAlignLeftPercentWidth() {
    constraint.setHorizontalAlign(HAlign.left);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    Assert.assertBoxLeftWidth(box, 50, width);
  }

  @Test
  public void testHorizontalAlignRightPercentWidth() {
    constraint.setHorizontalAlign(HAlign.right);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    Assert.assertBoxLeftWidth(box, 640 - width, width);
  }

  @Test
  public void testHorizontalAlignCenterPercentWidth() {
    constraint.setHorizontalAlign(HAlign.center);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50 + ((640 - 100) / 2) / 2, (640 - 100) / 2);
  }
}
