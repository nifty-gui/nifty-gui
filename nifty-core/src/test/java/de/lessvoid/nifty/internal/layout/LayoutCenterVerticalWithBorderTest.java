package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;
import de.lessvoid.nifty.internal.layout.LayoutCenter;

public class LayoutCenterVerticalWithBorderTest {
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
  }

  @Test
  public void testHorizontalAlignDefault() {
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50, 540);
  }
}
