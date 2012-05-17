package de.lessvoid.nifty.layout.manager;

import static de.lessvoid.nifty.layout.manager.BoxTestHelper.assertBox;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;
public class VerticalLayoutMarginTest {
  private VerticalLayout layout= new VerticalLayout();
  private LayoutPart root;
  private List<LayoutPart> elements;
  private LayoutPart top;
  private LayoutPart bottom;

  @Before
  public void before() throws Exception {
    root = new LayoutPart(new Box(0, 0, 100, 200), new BoxConstraints());

    elements = new ArrayList<LayoutPart>();

    top = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(top);

    bottom = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(bottom);
  }

  @Test
  public void testTopMargin() throws Exception {
    top.getBoxConstraints().setMarginTop(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(top.getBox(), 0, 50, 100, 100);
    assertBox(bottom.getBox(), 0, 150, 100, 100);
  }

  @Test
  public void testBottomMargin() throws Exception {
    top.getBoxConstraints().setMarginBottom(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(top.getBox(), 0, 0, 100, 100);
    assertBox(bottom.getBox(), 0, 150, 100, 100);
  }

  @Test
  public void testLeftMargin() throws Exception {
    top.getBoxConstraints().setMarginLeft(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(top.getBox(), 50, 0, 100, 100);
    assertBox(bottom.getBox(), 0, 100, 100, 100);
  }
}
