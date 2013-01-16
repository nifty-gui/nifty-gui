package de.lessvoid.niftyimpl.layout.manager;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.HorizontalAlign;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class CenterLayoutWithPaddingTest {
  private LayoutPart rootPanel;
  private CenterLayout layout;
  private BoxConstraints constraint;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();

    rootPanel = new LayoutPart(box, boxConstraint);
    rootPanel.getBoxConstraints().setPadding(new SizeValue("50px"));

    layout = new CenterLayout();
    constraint = new BoxConstraints();
  }

  @Test
  public void testHorizontalAlignCenterWithBorder() throws Exception {
    constraint.setWidth(new SizeValue("100px"));
    constraint.setHeight(new SizeValue("100px"));
    constraint.setHorizontalAlign(HorizontalAlign.center);

    LayoutPart child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testHorizontalAlignCenterWithBorderNoConstraint() throws Exception {
    constraint.setHorizontalAlign(HorizontalAlign.center);

    LayoutPart child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 50, 50, 540, 380);
  }

  private LayoutPart prepare(final BoxConstraints constraint) {
    Box box = new Box();
    LayoutPart child = new LayoutPart(box, constraint);
    ArrayList<Layoutable> elements = new ArrayList<Layoutable>();
    elements.add(child);
    layout.layoutElements(rootPanel, elements);
    return child;
  }
}
