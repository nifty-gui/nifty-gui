package de.lessvoid.niftyimpl.layout.manager;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.nifty.layout.VerticalAlign;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;

public class CenterLayoutVerticallPercentWithBorderTest {
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
    constraint.setHeight(new SizeValue("50%"));
  }

  @Test
  public void testVerticalAlignTopPercentWidth() {
    constraint.setVerticalAlign(VerticalAlign.top);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int height = (480 - 100) / 2;
    Assert.assertBoxTopHeight(box, 50, height);
  }

  @Test
  public void testVerticalAlignBottomPercentWidth() {
    constraint.setVerticalAlign(VerticalAlign.bottom);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    int height = (480 - 100) / 2;
    Assert.assertBoxTopHeight(box, 480 - height - 50 + 50, height);
  }

  @Test
  public void testVerticalAlignCenterPercentWidth() {
    constraint.setVerticalAlign(VerticalAlign.center);
    layout.handleVerticalAlignment(rootBox, rootBoxConstraints, box, constraint);
    Assert.assertBoxTopHeight(box, 50 + ((480 - 100) / 2) / 2, (480 - 100) / 2);
  }
}
