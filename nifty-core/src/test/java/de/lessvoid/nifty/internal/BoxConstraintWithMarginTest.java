package de.lessvoid.nifty.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.BoxConstraints;

public class BoxConstraintWithMarginTest {
  private BoxConstraints boxConstraints;

  @Before
  public void setUp() {
    boxConstraints = new BoxConstraints();
  }

  @Test
  public void testMarginDefault() {
    verifyMargin("0px", "0px", "0px", "0px");
  }

  @Test
  public void testMarginLeft() {
    boxConstraints.setMarginLeft(new UnitValue("10px"));
    verifyMargin("10px", "0px", "0px", "0px");
  }

  @Test
  public void testMarginRight() {
    boxConstraints.setMarginRight(new UnitValue("10px"));
    verifyMargin("0px", "0px", "10px", "0px");
  }

  @Test
  public void testMarginTop() {
    boxConstraints.setMarginTop(new UnitValue("10px"));
    verifyMargin("0px", "10px", "0px", "0px");
  }

  @Test
  public void testMarginBottom() {
    boxConstraints.setMarginBottom(new UnitValue("10px"));
    verifyMargin("0px", "0px", "0px", "10px");
  }

  @Test
  public void testMarginTopBottom() {
    boxConstraints.setMargin(new UnitValue("10px"), new UnitValue("20px"));
    verifyMargin("20px", "10px", "20px", "10px");
  }

  @Test
  public void testMarginTopLeftRightBottom() {
    boxConstraints.setMargin(new UnitValue("10px"), new UnitValue("15px"), new UnitValue("20px"));
    verifyMargin("15px", "10px", "15px", "20px");
  }

  @Test
  public void testMarginTopRightBottomLeft() {
    boxConstraints.setMargin(
        new UnitValue("10px"), new UnitValue("15px"), new UnitValue("17px"), new UnitValue("20px"));
    verifyMargin("20px", "10px", "15px", "17px");
  }

  @Test
  public void testMargin() {
    boxConstraints.setMargin(new UnitValue("10px"));
    verifyMargin("10px", "10px", "10px", "10px");
  }

  private void verifyMargin(
      final String left,
      final String top,
      final String right,
      final String bottom) {
    assertEquals(left, boxConstraints.getMarginLeft().toString());
    assertEquals(right, boxConstraints.getMarginRight().toString());
    assertEquals(top, boxConstraints.getMarginTop().toString());
    assertEquals(bottom, boxConstraints.getMarginBottom().toString());
  }
}
