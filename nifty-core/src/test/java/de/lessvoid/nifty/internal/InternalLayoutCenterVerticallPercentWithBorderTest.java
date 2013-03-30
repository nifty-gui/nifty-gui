package de.lessvoid.nifty.internal;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.InternalBox;
import de.lessvoid.nifty.internal.InternalBoxConstraints;
import de.lessvoid.nifty.internal.InternalLayoutCenter;

public class InternalLayoutCenterVerticallPercentWithBorderTest {
  private InternalLayoutCenter layout;
  private InternalBox rootBox;
  private InternalBox box;
  private InternalBoxConstraints constraint;
  private InternalBoxConstraints rootBoxConstraints;

  @Before
  public void setUp() throws Exception {
    layout = new InternalLayoutCenter();
    rootBox = new InternalBox(0, 0, 640, 480);
    rootBoxConstraints = new InternalBoxConstraints();
    rootBoxConstraints.setPadding(new UnitValue("50px"));
    box = new InternalBox();
    constraint = new InternalBoxConstraints();
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
