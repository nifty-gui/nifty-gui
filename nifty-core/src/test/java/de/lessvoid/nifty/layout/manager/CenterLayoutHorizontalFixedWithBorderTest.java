package de.lessvoid.nifty.layout.manager;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class CenterLayoutHorizontalFixedWithBorderTest extends TestCase {
  private CenterLayout layout;
  private Box rootBox;
  private Box box;
  private BoxConstraints constraint;
  private BoxConstraints rootBoxConstraints;

  public void setUp() throws Exception {
    layout = new CenterLayout();

    rootBox = new Box(0, 0, 640, 480);

    rootBoxConstraints = new BoxConstraints();
    rootBoxConstraints.setPadding(new SizeValue("50px"));

    box = new Box();

    constraint = new BoxConstraints();
    constraint.setWidth(new SizeValue("100px"));
  }

  public void testHorizontalAlignDefaultFixedWidth() {
    // defauls to left right now
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 270, 100);
  }

  public void testHorizontalAlignLeftFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.left);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 50, 100);
  }

  public void testHorizontalAlignRightFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.right);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 640 - 100, 100);
  }

  public void testHorizontalAlignCenterFixedWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.center);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 270, 100);
  }
}
