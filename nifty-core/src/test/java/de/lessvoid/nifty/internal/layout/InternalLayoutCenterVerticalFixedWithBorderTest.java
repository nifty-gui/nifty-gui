package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;

public class InternalLayoutCenterVerticalFixedWithBorderTest {
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
