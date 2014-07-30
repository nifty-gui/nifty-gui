package de.lessvoid.nifty.layout;

import de.lessvoid.nifty.tools.SizeValue;
import junit.framework.TestCase;

public class BoxConstraintWithPaddingTest extends TestCase {

  private BoxConstraints boxConstraints;

  @Override
  public void setUp() {
    boxConstraints = new BoxConstraints();
  }

  public void testPaddingDefault() {
    verifyPadding("0.0px", "0.0px", "0.0px", "0.0px");
  }

  public void testPaddingLeft() {
    boxConstraints.setPaddingLeft(new SizeValue("10px"));
    verifyPadding("10.0px", "0.0px", "0.0px", "0.0px");
  }

  public void testPaddingRight() {
    boxConstraints.setPaddingRight(new SizeValue("10px"));
    verifyPadding("0.0px", "0.0px", "10.0px", "0.0px");
  }

  public void testPaddingTop() {
    boxConstraints.setPaddingTop(new SizeValue("10px"));
    verifyPadding("0.0px", "10.0px", "0.0px", "0.0px");
  }

  public void testPaddingBottom() {
    boxConstraints.setPaddingBottom(new SizeValue("10px"));
    verifyPadding("0.0px", "0.0px", "0.0px", "10.0px");
  }

  public void testPaddingTopBottom() {
    boxConstraints.setPadding(new SizeValue("10px"), new SizeValue("20px"));
    verifyPadding("20.0px", "10.0px", "20.0px", "10.0px");
  }

  public void testPaddingTopLeftRightBottom() {
    boxConstraints.setPadding(new SizeValue("10px"), new SizeValue("15px"), new SizeValue("20px"));
    verifyPadding("15.0px", "10.0px", "15.0px", "20.0px");
  }

  public void testPaddingTopRightBottomLeft() {
    boxConstraints.setPadding(
        new SizeValue("10px"), new SizeValue("15px"), new SizeValue("17px"), new SizeValue("20px"));
    verifyPadding("20.0px", "10.0px", "15.0px", "17.0px");
  }

  public void testPadding() {
    boxConstraints.setPadding(new SizeValue("10px"));
    verifyPadding("10.0px", "10.0px", "10.0px", "10.0px");
  }

  private void verifyPadding(
      final String left,
      final String top,
      final String right,
      final String bottom) {
    assertEquals(left, boxConstraints.getPaddingLeft().toString());
    assertEquals(right, boxConstraints.getPaddingRight().toString());
    assertEquals(top, boxConstraints.getPaddingTop().toString());
    assertEquals(bottom, boxConstraints.getPaddingBottom().toString());
  }
}
