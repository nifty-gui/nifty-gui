package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutCenterWithPaddingTest {
  private LayoutableTestImpl rootPanel;
  private LayoutCenter layout;
  private BoxConstraints constraint;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();

    rootPanel = new LayoutableTestImpl(box, boxConstraint);
    rootPanel.getBoxConstraints().setPadding(new UnitValue("50px"));

    layout = new LayoutCenter();
    constraint = new BoxConstraints();
  }

  @Test
  public void testHorizontalAlignCenterWithBorder() throws Exception {
    constraint.setWidth(new UnitValue("100px"));
    constraint.setHeight(new UnitValue("100px"));
    constraint.setHorizontalAlign(HorizontalAlignment.center);

    LayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testHorizontalAlignCenterWithBorderNoConstraint() throws Exception {
    constraint.setHorizontalAlign(HorizontalAlignment.center);

    LayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 50, 50, 540, 380);
  }

  private LayoutableTestImpl prepare(final BoxConstraints constraint) {
    Box box = new Box();
    LayoutableTestImpl child = new LayoutableTestImpl(box, constraint);
    ArrayList<Layoutable> elements = new ArrayList<Layoutable>();
    elements.add(child);
    layout.layoutElements(rootPanel, elements);
    return child;
  }
}
