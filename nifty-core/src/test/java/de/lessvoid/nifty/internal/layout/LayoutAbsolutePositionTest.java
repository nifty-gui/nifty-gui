package de.lessvoid.nifty.internal.layout;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.Box;
import de.lessvoid.nifty.internal.BoxConstraints;
import de.lessvoid.nifty.internal.layout.LayoutAbsolute.KeepInsidePostProcess;

public class LayoutAbsolutePositionTest {
  private LayoutAbsolute layout = new LayoutAbsolute();
  private List<Layoutable> elements;
  private LayoutableTestImpl rootPanel;
  private LayoutableTestImpl element;

  @Before
  public void setUp() throws Exception {
    Box box = new Box(0, 0, 640, 480);
    BoxConstraints boxConstraint = new BoxConstraints();
    rootPanel = new LayoutableTestImpl(box, boxConstraint);

    box = new Box();
    boxConstraint = new BoxConstraints();
    element = new LayoutableTestImpl(box, boxConstraint);

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
    element.getBoxConstraints().setHeight(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(20, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedWidth() {
    element.getBoxConstraints().setWidth(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(20, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedX() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedY() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutFixedXandY() {
    element.getBoxConstraints().setX(new UnitValue("20px"));
    element.getBoxConstraints().setY(new UnitValue("40px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(20, element.getLayoutPos().getX());
    assertEquals(40, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithPercentageWidth() throws Exception {
    element.getBoxConstraints().setWidth(new UnitValue("25%"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(160, element.getLayoutPos().getWidth());
  }

  @Test
  public void testLayoutHeightSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("50%h"));
    element.getBoxConstraints().setHeight(new UnitValue("100px"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(50, element.getLayoutPos().getWidth());
    assertEquals(100, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutHeightSuffixWithoutHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("50%h"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithHeight() {
    element.getBoxConstraints().setWidth(new UnitValue("100px"));
    element.getBoxConstraints().setHeight(new UnitValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(100, element.getLayoutPos().getWidth());
    assertEquals(50, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWidthSuffixWithoutWidth() {
    element.getBoxConstraints().setHeight(new UnitValue("50%w"));
    layout.layoutElements(rootPanel, elements);

    assertEquals(0, element.getLayoutPos().getX());
    assertEquals(0, element.getLayoutPos().getY());
    assertEquals(0, element.getLayoutPos().getWidth());
    assertEquals(0, element.getLayoutPos().getHeight());
  }

  @Test
  public void testLayoutWithKeepInsideMode() {
    element.getBoxConstraints().setX(new UnitValue("640px"));
    element.getBoxConstraints().setY(new UnitValue("480px"));
    element.getBoxConstraints().setWidth(new UnitValue("10px"));
    element.getBoxConstraints().setHeight(new UnitValue("10px"));

    layout = new LayoutAbsolute(new KeepInsidePostProcess());
    layout.layoutElements(rootPanel, elements);

    assertEquals(630, element.getLayoutPos().getX());
    assertEquals(470, element.getLayoutPos().getY());
    assertEquals(10, element.getLayoutPos().getWidth());
    assertEquals(10, element.getLayoutPos().getHeight());
  }
}
