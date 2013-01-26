package de.lessvoid.niftyimpl.layout.manager;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.HorizontalAlign;
import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.nifty.layout.VerticalAlign;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class CenterLayoutTest {
  private ArrayList<Layoutable> elements = new ArrayList<Layoutable>();
  private CenterLayout layout = new CenterLayout();
  private LayoutPartTestHelper root;
  private LayoutPartTestHelper child;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    root = new LayoutPartTestHelper(box, boxConstraint);
    child = new LayoutPartTestHelper();
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
    child.getBoxConstraints().setWidth(new SizeValue("100px"));
    child.getBoxConstraints().setHeight(new SizeValue("100px"));
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlign.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 190, 100, 100);
  }

  @Test
  public void testCenterSingleHeightSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new SizeValue("75%h"));
    child.getBoxConstraints().setHeight(new SizeValue("100px"));
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlign.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 282, 190, 75, 100);
  }

  @Test
  public void testCenterSingleWidthSuffix() throws Exception {
    child.getBoxConstraints().setWidth(new SizeValue("100px"));
    child.getBoxConstraints().setHeight(new SizeValue("75%w"));
    child.getBoxConstraints().setHorizontalAlign(HorizontalAlign.center);
    child.getBoxConstraints().setVerticalAlign(VerticalAlign.center);

    layout.layoutElements(root, elements);
    Assert.assertBox(child.getLayoutPos(), 270, 202, 100, 75);
  }
}
