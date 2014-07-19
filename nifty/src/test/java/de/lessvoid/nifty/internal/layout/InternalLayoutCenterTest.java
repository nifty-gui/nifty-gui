package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.HAlign;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VAlign;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutCenter;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutCenterTest {
  private ArrayList<InternalLayoutable> elements = new ArrayList<InternalLayoutable>();
  private InternalLayoutCenter layout = new InternalLayoutCenter();
  private InternalLayoutableTestImpl root;
  private InternalLayoutableTestImpl child;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    InternalBoxConstraints boxConstraint = new InternalBoxConstraints();
    root = new InternalLayoutableTestImpl(box, boxConstraint);
    child = new InternalLayoutableTestImpl();
    elements.add(child);
  }

  @Test
  public void testEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  @Test
  public void testNullList() throws Exception {
    layout.layoutElements(root, null);
  }

  @Test
  public void testEmptyList() throws Exception {
    layout.layoutElements(root, new ArrayList<InternalLayoutable>());
  }

  @Test
  public void testCenterSingle() throws Exception {
    child.getBoxConstraints().setWidth(new UnitValue("100px"));
    child.getBoxConstraints().setHeight(new UnitValue("100px"));
    child.getBoxConstraints().setHorizontalAlign(HAlign.center);
    child.getBoxConstraints().setVerticalAlign(VAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testCenterSingleHeightSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new UnitValue("75%h"));
    child.getBoxConstraints().setHeight(new UnitValue("100px"));
    child.getBoxConstraints().setHorizontalAlign(HAlign.center);
    child.getBoxConstraints().setVerticalAlign(VAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 282, 190, 75, 100);
  }

  @Test
  public void testCenterSingleWidthSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new UnitValue("100px"));
    child.getBoxConstraints().setHeight(new UnitValue("75%w"));
    child.getBoxConstraints().setHorizontalAlign(HAlign.center);
    child.getBoxConstraints().setVerticalAlign(VAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 202, 100, 75);
  }
}
