package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;
import de.lessvoid.nifty.internal.layout.LayoutCenter;

public class LayoutCenterVerticalFixedWithBorderTest {
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
    constraint.setHeight(new UnitValue("100px"));
  }

  @Test
  public void testVerticalAlignDefaultFixedWidth() {
    // defaults to top right now
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 190, 100);
  }

  @Test
  public void testVerticalAlignTopFixedWidth() {
    constraint.setVerticalAlign(VerticalAlignment.top);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 50, 100);
  }

  @Test
  public void testVerticalAlignBottomFixedWidth() {
    constraint.setVerticalAlign(VerticalAlignment.bottom);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 480 - 100 - 50 + 50, 100);
  }

  @Test
  public void testVerticalAlignCenterFixedWidth() {
    constraint.setVerticalAlign(VerticalAlignment.center);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 190, 100);
  }
}
