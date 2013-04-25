package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.InternalBox;
import de.lessvoid.nifty.internal.InternalBoxConstraints;
import de.lessvoid.nifty.internal.InternalLayoutVertical;
import de.lessvoid.nifty.internal.InternalLayoutable;

public class InternalLayoutVerticalMarginTest {
  private InternalLayoutVertical layout= new InternalLayoutVertical();
  private InternalLayoutableTestImpl root;
  private List<InternalLayoutable> elements;
  private InternalLayoutableTestImpl top;
  private InternalLayoutableTestImpl bottom;

  @Before
  public void before() throws Exception {
    root = new InternalLayoutableTestImpl(new InternalBox(0, 0, 100, 200), new InternalBoxConstraints());
    elements = new ArrayList<InternalLayoutable>();
    top = new InternalLayoutableTestImpl(new InternalBox(), new InternalBoxConstraints());
    elements.add(top);
    bottom = new InternalLayoutableTestImpl(new InternalBox(), new InternalBoxConstraints());
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
