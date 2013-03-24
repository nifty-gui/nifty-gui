package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;
import de.lessvoid.nifty.internal.layout.LayoutCenter;

public class LayoutCenterVerticallPercentWithBorderTest {
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
    constraint.setHeight(new UnitValue("50%"));
  }

  @Test
  public void testVerticalAlignTopPercentWidth() {
    constraint.setVerticalAlign(VerticalAlignment.top);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int height = (480 - 100) / 2;
    Assert.assertBoxTopHeight(box, 50, height);
  }

  @Test
  public void testVerticalAlignBottomPercentWidth() {
    constraint.setVerticalAlign(VerticalAlignment.bottom);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int height = (480 - 100) / 2;
    Assert.assertBoxTopHeight(box, 480 - height - 50 + 50, height);
  }

  @Test
  public void testVerticalAlignCenterPercentWidth() {
    constraint.setVerticalAlign(VerticalAlignment.center);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 50 + ((480 - 100) / 2) / 2, (480 - 100) / 2);
  }
}
