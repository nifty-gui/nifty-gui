package de.lessvoid.niftyimpl.layout.manager;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;
import de.lessvoid.niftyimpl.layout.Box;
import de.lessvoid.niftyimpl.layout.BoxConstraints;
import de.lessvoid.niftyimpl.layout.Layoutable;
import de.lessvoid.niftyimpl.layout.manager.AbsolutePositionLayout.KeepInsidePostProcess;

public class AbsolutePositionLayoutTest {
  private AbsolutePositionLayout layout = new AbsolutePositionLayout();
  private List<Layoutable> elements;
  private LayoutPart rootPanel;
  private LayoutPart element;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    rootPanel = new LayoutPart(box, boxConstraint);

    box = new Box();
    boxConstraint = new BoxConstraints();
    element = new LayoutPart(box, boxConstraint);

    elements = new ArrayList<Layoutable>();
    elements.add(element);
  }

  @Test
  public void testUpdateEmpty() throws Exception {
    layout.layoutElements(null, null);
  }

  @Test
  public void testUpdateWithNullEntriesMakeNoTrouble() {
    layout.layoutElements(rootPanel, null);
  }

  @Test
  public void testUpdateWithEmptyElements() {
    layout.layoutElements(rootPanel, new ArrayList<Layoutable>());
  }

  @Test
  public void testLayoutFixedHeight() {
    element.getBoxConstraints().setHeight(new SizeValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(20, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedWidth() {
    element.getBoxConstraints().setWidth(new SizeValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(20, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedX() {
    element.getBoxConstraints().setX(new SizeValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedY() {
    element.getBoxConstraints().setX(new SizeValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedXandY() {
    element.getBoxConstraints().setX(new SizeValue("20px"));
    element.getBoxConstraints().setY(new SizeValue("40px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(40, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithPercentageWidth() throws Exception {
    element.getBoxConstraints().setWidth(new SizeValue("25%"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(160, element.getLayoutPos().getWidth());
  }

  @Test
  public void testLayoutHeightSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new SizeValue("50%h"));
    element.getBoxConstraints().setHeight(new SizeValue("100px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(50, element.getLayoutPos().getWidth());
    assertEquals(100, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutHeightSuffixWithoutHeight() {
    element.getBoxConstraints().setWidth(new SizeValue("50%h"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new SizeValue("100px"));
    element.getBoxConstraints().setHeight(new SizeValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(100, element.getLayoutPos().getWidth());
    assertEquals(50, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithoutWidth() {
    element.getBoxConstraints().setHeight(new SizeValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithKeepInsideMode() {
    element.getBoxConstraints().setX(new SizeValue("640px"));
    element.getBoxConstraints().setY(new SizeValue("480px"));
    element.getBoxConstraints().setWidth(new SizeValue("10px"));
    element.getBoxConstraints().setHeight(new SizeValue("10px"));

    layout = new AbsolutePositionLayout(new KeepInsidePostProcess());
    layout.layoutElements(rootPanel, elements);

    assertEquals(630, element.getLayoutPos().getX());
    assertEquals(470, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }
}
