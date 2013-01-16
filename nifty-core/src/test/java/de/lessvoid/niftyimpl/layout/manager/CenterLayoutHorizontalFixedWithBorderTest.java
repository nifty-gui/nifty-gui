package de.lessvoid.niftyimpl.layout.manager;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.HorizontalAlign;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;

public class CenterLayoutHorizontalFixedWithBorderTest {
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
    constraint.setWidth(new SizeValue("100px"));
  }

  @Test
  public void testHorizontalAlignDefaultFixedWidth() {
    // defauls to left right now
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 270, 100);
  }

  @Test
  public void testHorizontalAlignLeftFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.left);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50, 100);
  }

  @Test
  public void testHorizontalAlignRightFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.right);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 640 - 100, 100);
  }

  @Test
  public void testHorizontalAlignCenterFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.center);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 270, 100);
  }
}
