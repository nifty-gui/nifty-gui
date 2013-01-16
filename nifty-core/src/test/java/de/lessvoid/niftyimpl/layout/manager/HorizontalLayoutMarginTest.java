package de.lessvoid.niftyimpl.layout.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;

public class HorizontalLayoutMarginTest {
  private HorizontalLayout layout= new HorizontalLayout();
  private LayoutPart root;
  private List<Layoutable> elements;
  private LayoutPart left;
  private LayoutPart right;

  @Before
  public void before() throws Exception {
    root = new LayoutPart(new Box(0, 0, 200, 100), new BoxConstraints());
    elements = new ArrayList<Layoutable>();
    left = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(left);
    right = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(right);
  }

  @Test
  public void testLeftMargin() throws Exception {
    left.getBoxConstraints().setMarginLeft(SizeValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 50, 0, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 150, 0, 100, 100);
  }

  @Test
  public void testRightMargin() throws Exception {
    left.getBoxConstraints().setMarginRight(SizeValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 0, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 150, 0, 100, 100);
  }

  @Test
  public void testTopMargin() throws Exception {
    left.getBoxConstraints().setMarginTop(SizeValue.px(50));
    layout.layoutElements(root, elements);

    Assert.assertBox(left.getLayoutPos(), 0, 50, 100, 100);
    Assert.assertBox(right.getLayoutPos(), 100, 0, 100, 100);
  }
}
