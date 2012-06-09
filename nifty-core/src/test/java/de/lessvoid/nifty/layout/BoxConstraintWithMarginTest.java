package de.lessvoid.nifty.layout;

import junit.framework.TestCase;
import de.lessvoid.nifty.tools.SizeValue;

public class BoxConstraintWithMarginTest extends TestCase {

  private BoxConstraints boxConstraints;

  public void setUp() {
    boxConstraints = new BoxConstraints();
  }

  public void testMarginDefault() {
    verifyMargin("0px", "0px", "0px", "0px");
  }

  public void testMarginLeft() {
    boxConstraints.setMarginLeft(new SizeValue("10px"));
    verifyMargin("10px", "0px", "0px", "0px");
  }

  public void testMarginRight() {
    boxConstraints.setMarginRight(new SizeValue("10px"));
    verifyMargin("0px", "0px", "10px", "0px");
  }

  public void testMarginTop() {
    boxConstraints.setMarginTop(new SizeValue("10px"));
    verifyMargin("0px", "10px", "0px", "0px");
  }

  public void testMarginBottom() {
    boxConstraints.setMarginBottom(new SizeValue("10px"));
    verifyMargin("0px", "0px", "0px", "10px");
  }

  public void testMarginTopBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("20px"));
    verifyMargin("20px", "10px", "20px", "10px");
  }

  public void testMarginTopLeftRightBottom() {
    boxConstraints.setMargin(new SizeValue("10px"), new SizeValue("15px"), new SizeValue("20px"));
    verifyMargin("15px", "10px", "15px", "20px");
  }

  public void testMarginTopRightBottomLeft() {
    boxConstraints.setMargin(
        new SizeValue("10px"), new SizeValue("15px"), new SizeValue("17px"), new SizeValue("20px"));
    verifyMargin("20px", "10px", "15px", "17px");
  }

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
