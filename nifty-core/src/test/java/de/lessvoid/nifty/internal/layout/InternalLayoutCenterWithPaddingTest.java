package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutCenterWithPaddingTest {
  private InternalLayoutableTestImpl rootPanel;
  private InternalLayoutCenter layout;
  private InternalBoxConstraints constraint;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    InternalBoxConstraints boxConstraint = new InternalBoxConstraints();

    rootPanel = new InternalLayoutableTestImpl(box, boxConstraint);
    rootPanel.getBoxConstraints().setPadding(new UnitValue("50px"));

    layout = new InternalLayoutCenter();
    constraint = new InternalBoxConstraints();
  }

  @Test
  public void testHorizontalAlignCenterWithBorder() throws Exception {
    constraint.setWidth(new UnitValue("100px"));
    constraint.setHeight(new UnitValue("100px"));
    constraint.setHorizontalAlign(HorizontalAlignment.center);

    InternalLayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testHorizontalAlignCenterWithBorderNoConstraint() throws Exception {
    constraint.setHorizontalAlign(HorizontalAlignment.center);

    InternalLayoutableTestImpl child = prepare(constraint);
    Assert.assertBox(child.getLayoutPos(), 50, 50, 540, 380);
  }

  private InternalLayoutableTestImpl prepare(final InternalBoxConstraints constraint) {
    Box box = new Box();
    InternalLayoutableTestImpl child = new InternalLayoutableTestImpl(box, constraint);
    ArrayList<InternalLayoutable> elements = new ArrayList<InternalLayoutable>();
    elements.add(child);
    layout.layoutElements(rootPanel, elements);
    return child;
  }
}
