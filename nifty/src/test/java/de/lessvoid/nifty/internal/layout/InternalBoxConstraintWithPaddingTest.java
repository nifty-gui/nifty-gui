package de.lessvoid.nifty.internal.layout;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.UnitValue;
import de.lessvoid.nifty.internal.layout.InternalBoxConstraints;

public class InternalBoxConstraintWithPaddingTest {
  private InternalBoxConstraints boxConstraints;

  @Before
  public void setUp() {
    boxConstraints = new InternalBoxConstraints();
  }

  @Test
  public void testPaddingDefault() {
    verifyPadding("0px", "0px", "0px", "0px");
  }

  @Test
  public void testPaddingLeft() {
    boxConstraints.setPaddingLeft(new UnitValue("10px"));
    verifyPadding("10px", "0px", "0px", "0px");
  }

  @Test
  public void testPaddingRight() {
    boxConstraints.setPaddingRight(new UnitValue("10px"));
    verifyPadding("0px", "0px", "10px", "0px");
  }

  @Test
  public void testPaddingTop() {
    boxConstraints.setPaddingTop(new UnitValue("10px"));
    verifyPadding("0px", "10px", "0px", "0px");
  }

  @Test
  public void testPaddingBottom() {
    boxConstraints.setPaddingBottom(new UnitValue("10px"));
    verifyPadding("0px", "0px", "0px", "10px");
  }

  @Test
  public void testPaddingTopBottom() {
    boxConstraints.setPadding(new UnitValue("10px"), new UnitValue("20px"));
    verifyPadding("20px", "10px", "20px", "10px");
  }

  @Test
  public void testPaddingTopLeftRightBottom() {
    boxConstraints.setPadding(new UnitValue("10px"), new UnitValue("15px"), new UnitValue("20px"));
    verifyPadding("15px", "10px", "15px", "20px");
  }

  @Test
  public void testPaddingTopRightBottomLeft() {
    boxConstraints.setPadding(
        new UnitValue("10px"), new UnitValue("15px"), new UnitValue("17px"), new UnitValue("20px"));
    verifyPadding("20px", "10px", "15px", "17px");
  }

  @Test
  public void testPadding() {
    boxConstraints.setPadding(new UnitValue("10px"));
    verifyPadding("10px", "10px", "10px", "10px");
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
