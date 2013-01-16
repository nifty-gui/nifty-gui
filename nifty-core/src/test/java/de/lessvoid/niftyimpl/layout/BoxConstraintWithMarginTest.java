package de.lessvoid.niftyimpl.layout;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.layout.SizeValue;

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
    boxConstraints.setMarginLeft(new SizeValue("10px"));
    verifyMargin("10px", "0px", "0px", "0px");
  }

  @Test
  public void testMarginRight() {
    boxConstraints.setMarginRight(new SizeValue("10px"));
    verifyMargin("0px", "0px", "10px", "0px");
  }

  @Test
  public void testMarginTop() {
    boxConstraints.setMarginTop(new SizeValue("10px"));
    verifyMargin("0px", "10px", "0px", "0px");
  }

  @Test
  public void testMarginBottom() {
    boxConstraints.setMarginBottom(new SizeValue("10px"));
    verifyMargin("0px", "0px", "0px", "10px");
  }

  @Test
  public void testMarginTopBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("20px"));
    verifyMargin("20px", "10px", "20px", "10px");
  }

  @Test
  public void testMarginTopLeftRightBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("15px"), new SizeValue("20px"));
    verifyMargin("15px", "10px", "15px", "20px");
  }

  @Test
  public void testMarginTopRightBottomLeft() {
    boxConstraints.setMargin(
        new SizeValue("10px"), new SizeValue("15px"), new SizeValue("17px"), new SizeValue("20px"));
    verifyMargin("20px", "10px", "15px", "17px");
  }

  @Test
  public void testMargin() {
    boxConstraints.setMargin(new SizeValue("10px"));
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
