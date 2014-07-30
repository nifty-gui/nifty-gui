package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.tools.SizeValue;
import junit.framework.TestCase;

public class BoxConstraintWithMarginTest extends TestCase {

  private BoxConstraints boxConstraints;

  @Override
  public void setUp() {
    boxConstraints = new BoxConstraints();
  }

  public void testMarginDefault() {
    verifyMargin("0.0px", "0.0px", "0.0px", "0.0px");
  }

  public void testMarginLeft() {
    boxConstraints.setMarginLeft(new SizeValue("10px"));
    verifyMargin("10.0px", "0.0px", "0.0px", "0.0px");
  }

  public void testMarginRight() {
    boxConstraints.setMarginRight(new SizeValue("10px"));
    verifyMargin("0.0px", "0.0px", "10.0px", "0.0px");
  }

  public void testMarginTop() {
    boxConstraints.setMarginTop(new SizeValue("10px"));
    verifyMargin("0.0px", "10.0px", "0.0px", "0.0px");
  }

  public void testMarginBottom() {
    boxConstraints.setMarginBottom(new SizeValue("10px"));
    verifyMargin("0.0px", "0.0px", "0.0px", "10.0px");
  }

  public void testMarginTopBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("20px"));
    verifyMargin("20.0px", "10.0px", "20.0px", "10.0px");
  }

  public void testMarginTopLeftRightBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("15px"), new SizeValue("20px"));
    verifyMargin("15.0px", "10.0px", "15.0px", "20.0px");
  }

  public void testMarginTopRightBottomLeft() {
    boxConstraints.setMargin(
        new SizeValue("10px"), new SizeValue("15px"), new SizeValue("17px"), new SizeValue("20px"));
    verifyMargin("20.0px", "10.0px", "15.0px", "17.0px");
  }

  public void testMargin() {
    boxConstraints.setMargin(new SizeValue("10px"));
    verifyMargin("10.0px", "10.0px", "10.0px", "10.0px");
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
