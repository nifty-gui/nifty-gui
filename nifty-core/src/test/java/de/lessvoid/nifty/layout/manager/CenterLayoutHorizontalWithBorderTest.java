package de.lessvoid.nifty.layout.manager;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.tools.SizeValue;
import junit.framework.TestCase;

public class CenterLayoutHorizontalWithBorderTest extends TestCase {
  private CenterLayout layout;
  private Box rootBox;
  private Box box;
  private BoxConstraints constraint;
  private BoxConstraints rootBoxConstraints;

  @Override
  public void setUp() throws Exception {
    layout = new CenterLayout();

    rootBox = new Box(0, 0, 640, 480);

    rootBoxConstraints = new BoxConstraints();
    rootBoxConstraints.setPadding(new SizeValue("50px"));

    box = new Box();

    constraint = new BoxConstraints();
  }

  public void testHorizontalAlignDefault() {
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    CenterLayoutTest.assertBoxLeftWidth(box, 50, 540);
  }
}
