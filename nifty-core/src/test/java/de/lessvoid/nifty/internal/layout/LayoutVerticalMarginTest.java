package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.Assert;
import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;

public class LayoutVerticalMarginTest {
  private LayoutVertical layout= new LayoutVertical();
  private LayoutableTestImpl root;
  private List<Layoutable> elements;
  private LayoutableTestImpl top;
  private LayoutableTestImpl bottom;

  @Before
  public void before() throws Exception {
    root = new LayoutableTestImpl(new Box(0, 0, 100, 200), new BoxConstraints());
    elements = new ArrayList<Layoutable>();
    top = new LayoutableTestImpl(new Box(), new BoxConstraints());
    elements.add(top);
    bottom = new LayoutableTestImpl(new Box(), new BoxConstraints());
    elements.add(bottom);
  }

  @Test
  public void testTopMargin() throws Exception {
    top.getBoxConstraints().setMarginTop(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 0, 50, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 150, 100, 100);
  }

  @Test
  public void testBottomMargin() throws Exception {
    top.getBoxConstraints().setMarginBottom(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 0, 0, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 150, 100, 100);
  }

  @Test
  public void testLeftMargin() throws Exception {
    top.getBoxConstraints().setMarginLeft(UnitValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(top.getLayoutPos(), 50, 0, 100, 100);
    Assert.assertBox(bottom.getLayoutPos(), 0, 100, 100, 100);
  }
}
