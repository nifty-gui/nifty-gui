package de.lessvoid.nifty.internal;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.InternalBox;
import de.lessvoid.nifty.internal.InternalBoxConstraints;
import de.lessvoid.nifty.internal.InternalLayoutCenter;
import de.lessvoid.nifty.internal.InternalLayoutable;

public class InternalLayoutCenterTest {
  private ArrayList<InternalLayoutable> elements = new ArrayList<InternalLayoutable>();
  private InternalLayoutCenter layout = new InternalLayoutCenter();
  private InternalLayoutableTestImpl root;
  private InternalLayoutableTestImpl child;

  @Before
  public void setUp() throws Exception {
    InternalBox box = new InternalBox(0, 0, 640, 480);
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
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlignment.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlignment.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testCenterSingleHeightSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new UnitValue("75%h"));
    child.getBoxConstraints().setHeight(new UnitValue("100px"));
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlignment.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlignment.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 282, 190, 75, 100);
  }

  @Test
  public void testCenterSingleWidthSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new UnitValue("100px"));
    child.getBoxConstraints().setHeight(new UnitValue("75%w"));
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlignment.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlignment.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 202, 100, 75);
  }
}
