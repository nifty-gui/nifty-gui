package de.lessvoid.nifty.internal.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.common.Box;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;
import de.lessvoid.nifty.internal.layout.InternalLayoutHorizontal;
import de.lessvoid.nifty.internal.layout.InternalLayoutable;

public class InternalLayoutHorizontalMarginTest {
  private InternalLayoutHorizontal layout= new InternalLayoutHorizontal();
  private InternalLayoutableTestImpl root;
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl left;
  private InternalLayoutableTestImpl right;

  @Before
  public void before() throws Exception {
    root = new InternalLayoutableTestImpl(new Box(0, 0, 200, 100), new InternalBoxConstraints());
    elements = new ArrayList<InternalLayoutable>();
    left = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
    elements.add(left);
    right = new InternalLayoutableTestImpl(new Box(), new InternalBoxConstraints());
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
