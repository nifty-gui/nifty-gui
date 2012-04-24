package de.lessvoid.nifty.layout.manager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.tools.SizeValue;

public class HorizontalLayoutMarginTest {
  private HorizontalLayout layout= new HorizontalLayout();
  private LayoutPart root;
  private List<LayoutPart> elements;
  private LayoutPart left;
  private LayoutPart right;

  @Before
  public void before() throws Exception {
    root = new LayoutPart(new Box(0, 0, 200, 100), new BoxConstraints());

    elements = new ArrayList<LayoutPart>();

    left = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(left);

    right = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(right);
  }

  @Test
  public void testLeftMargin() throws Exception {
    left.getBoxConstraints().setMarginLeft(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(left, 50, 0, 100, 100);
    assertBox(right, 150, 0, 100, 100);
  }

  @Test
  public void testRightMargin() throws Exception {
    left.getBoxConstraints().setMarginRight(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(left, 0, 0, 100, 100);
    assertBox(right, 50, 0, 100, 100);
  }

  @Test
  public void testTopMargin() throws Exception {
    left.getBoxConstraints().setMarginTop(SizeValue.px(50));
    layout.layoutElements(root, elements);

    assertBox(left, 0, 50, 100, 100);
    assertBox(right, 100, 0, 100, 100);
  }

  private void assertBox(final LayoutPart layoutPart, final int x, final int y, final int width, final int height) {
    assertEquals(x, layoutPart.getBox().getX());
    assertEquals(y, layoutPart.getBox().getY());
    assertEquals(width, layoutPart.getBox().getWidth());
    assertEquals(height, layoutPart.getBox().getHeight());
  }
}