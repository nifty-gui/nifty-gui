package de.lessvoid.nifty.layout.manager;

import static de.lessvoid.nifty.layout.manager.BoxTestHelper.assertBox;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.tools.SizeValue;

public class VerticalLayoutTest extends TestCase {
  private VerticalLayout layout= new VerticalLayout();
  private LayoutPart root;
  private List<LayoutPart> elements;
  private LayoutPart top;
  private LayoutPart bottom;
  
  protected void setUp() throws Exception {
    root = new LayoutPart(new Box(0, 0, 640, 480), new BoxConstraints());

    elements = new ArrayList<LayoutPart>();

    top = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(top);

    bottom = new LayoutPart(new Box(), new BoxConstraints());
    elements.add(bottom);
  }

  public void testUpdateEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements(root, null);
  }

  public void testLayoutDefault() {
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 240);
    assertBox(bottom.getBox(), 0, 240, 640, 240);
  }

  public void testLayoutFixedHeight() {
    top.getBoxConstraints().setHeight(new SizeValue("20px"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 20);
    assertBox(bottom.getBox(), 0, 20, 640, 460);
  }

  public void testLayoutFixedWidth() {
    top.getBoxConstraints().setWidth(new SizeValue("20px"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 20, 240);
  }

  public void testLayoutMaxWidth() {
    top.getBoxConstraints().setWidth(new SizeValue("100%"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 240);
  }

  public void testLayoutMaxWidthWildcard() {
    top.getBoxConstraints().setWidth(new SizeValue("*"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 240);
  }

  public void testLayoutFixedWidthRightAlign() {
    top.getBoxConstraints().setWidth(new SizeValue("20px"));
    top.getBoxConstraints().setHorizontalAlign(HorizontalAlign.right);
    performLayout();
    assertBox(top.getBox(), 620, 0, 20, 240);
  }

  public void testLayoutFixedWidthCenterAlign() {
    top.getBoxConstraints().setWidth(new SizeValue("20px"));
    top.getBoxConstraints().setHorizontalAlign(HorizontalAlign.center);
    performLayout();
    assertBox(top.getBox(), 310, 0, 20, 240);
  }

  public void testLayoutWithPercentage() throws Exception {
    top.getBoxConstraints().setHeight(new SizeValue("25%"));
    bottom.getBoxConstraints().setHeight(new SizeValue("75%"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 120);
    assertBox(bottom.getBox(), 0, 120, 640, 360);
  }

  public void testLayoutWithPaddingAllEqual() {
    root.getBoxConstraints().setPadding(new SizeValue("10px"));
    performLayout();
    assertBox(top.getBox(), 10, 10, 620, 230);
    assertBox(bottom.getBox(), 10, 240, 620, 230);
  }

  public void testLayoutWithPaddingLeft() {
    root.getBoxConstraints().setPaddingLeft(new SizeValue("10px"));
    performLayout();
    assertBox(top.getBox(), 10, 0, 630, 240);
    assertBox(bottom.getBox(), 10, 240, 630, 240);
  }

  public void testLayoutWithPaddingRight() {
    root.getBoxConstraints().setPaddingRight(new SizeValue("10px"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 630, 240);
    assertBox(bottom.getBox(), 0, 240, 630, 240);
  }

  public void testLayoutWithPaddingTop() {
    root.getBoxConstraints().setPaddingTop(new SizeValue("10px"));
    performLayout();
    assertBox(top.getBox(), 0, 10, 640, 235);
    assertBox(bottom.getBox(), 0, 245, 640, 235);
  }

  public void testLayoutWithPaddingBottom() {
    root.getBoxConstraints().setPaddingBottom(new SizeValue("10px"));
    performLayout();
    assertBox(top.getBox(), 0, 0, 640, 235);
    assertBox(bottom.getBox(), 0, 235, 640, 235);
  }

  private void performLayout() {
    layout.layoutElements(root, elements);
  }
}
