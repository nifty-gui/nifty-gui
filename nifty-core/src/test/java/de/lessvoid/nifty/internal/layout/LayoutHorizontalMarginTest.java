package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutHorizontalMarginTest {
  private LayoutHorizontal layout= new LayoutHorizontal();
  private LayoutableTestImpl root;
  private List<Layoutable> elements;
  private LayoutableTestImpl left;
  private LayoutableTestImpl right;

  @Before
  public void before() throws Exception {
    root = new LayoutableTestImpl(new Box(0, 0, 200, 100), new BoxConstraints());
    elements = new ArrayList<Layoutable>();
    left = new LayoutableTestImpl(new Box(), new BoxConstraints());
    elements.add(left);
    right = new LayoutableTestImpl(new Box(), new BoxConstraints());
    elements.add(right);
  }

  @Test
  public void testLeftMargin() throws Exception {
    left.getBoxConstraints().setMarginLeft(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 50, 0, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 150, 0, 100, 100);
  }

  @Test
  public void testRightMargin() throws Exception {
    left.getBoxConstraints().setMarginRight(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 150, 0, 100, 100);
  }

  @Test
  public void testTopMargin() throws Exception {
    left.getBoxConstraints().setMarginTop(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 50, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 100, 0, 100, 100);
  }
}
