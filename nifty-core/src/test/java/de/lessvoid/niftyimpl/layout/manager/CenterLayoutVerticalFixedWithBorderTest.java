package de.lessvoid.niftyimpl.layout.manager;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.nifty.layout.VerticalAlign;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;

public class CenterLayoutVerticalFixedWithBorderTest {
  private CenterLayout layout;
  private Box rootBox;
  private Box box;
  private BoxConstraints constraint;
  private BoxConstraints rootBoxConstraints;

  @Before
  public void setUp() throws Exception {
    layout = new CenterLayout();
    rootBox = new Box(0, 0, 640, 480);
    rootBoxConstraints = new BoxConstraints();
    rootBoxConstraints.setPadding(new SizeValue("50px"));
    box = new Box();
    constraint = new BoxConstraints();
    constraint.setHeight(new SizeValue("100px"));
  }

  @Test
  public void testVerticalAlignDefaultFixedWidth() {
    // defaults to top right now
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 190, 100);
  }

  @Test
  public void testVerticalAlignTopFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.top);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 50, 100);
  }

  @Test
  public void testVerticalAlignBottomFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.bottom);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 480 - 100 - 50 + 50, 100);
  }

  @Test
  public void testVerticalAlignCenterFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.center);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 190, 100);
  }
}
