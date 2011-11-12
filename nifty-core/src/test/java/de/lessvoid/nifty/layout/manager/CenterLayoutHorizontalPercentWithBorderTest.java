package de.lessvoid.nifty.layout.manager;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class CenterLayoutHorizontalPercentWithBorderTest extends TestCase {
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
    constraint.setWidth(new SizeValue("50%"));
  }

  public void testHorizontalAlignLeftPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.left);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    CenterLayoutTest.assertBoxLeftWidth(box, 50, width);
  }

  public void testHorizontalAlignRightPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.right);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int width = (640 - 100) / 2;
    CenterLayoutTest.assertBoxLeftWidth(box, 640 - width, width);
  }

  public void testHorizontalAlignCenterPercentWidth() {
    constraint.setHorizontalAlign(HorizontalAlign.center);
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 50 + ((640 - 100) / 2) / 2, (640 - 100) / 2);
  }
}
