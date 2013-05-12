package de.lessvoid.nifty.internal.layout;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;

public class InternalLayoutCenterHorizontalWithBorderTest {
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
  }

  @Test
  public void testHorizontalAlignDefault() {
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50, 540);
  }
}
