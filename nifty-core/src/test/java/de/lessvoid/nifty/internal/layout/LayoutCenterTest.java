package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.HorizontalAlignment;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.api.VerticalAlignment;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutCenterTest {
  private ArrayList<Layoutable> elements = new ArrayList<Layoutable>();
  private LayoutCenter layout = new LayoutCenter();
  private LayoutableTestImpl root;
  private LayoutableTestImpl child;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    root = new LayoutableTestImpl(box, boxConstraint);
    child = new LayoutableTestImpl();
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
    layout.layoutElements(root, new ArrayList<Layoutable>());
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
