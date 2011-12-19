package de.lessvoid.nifty.layout.manager;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class CenterLayoutVerticalFixedWithBorderTest extends TestCase {
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
    constraint.setHeight(new SizeValue("100px"));
  }

  public void testVerticalAlignDefaultFixedWidth() {
    // defaults to top right now
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxTopHeight(box, 190, 100);
  }

  public void testVerticalAlignTopFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.top);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxTopHeight(box, 50, 100);
  }

  public void testVerticalAlignBottomFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.bottom);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxTopHeight(box, 480 - 100 - 50 + 50, 100);
  }

  public void testVerticalAlignCenterFixedWidth() {
    constraint.setVerticalAlign(VerticalAlign.center);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxTopHeight(box, 190, 100);
  }
}
