package de.lessvoid.niftyimpl.layout.manager;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;

public class CenterLayoutHorizontalWithBorderTest {
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
  }

  @Test
  public void testHorizontalAlignDefault() {
    layout.handleHorizontalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxLeftWidth(box, 50, 540);
  }
}
