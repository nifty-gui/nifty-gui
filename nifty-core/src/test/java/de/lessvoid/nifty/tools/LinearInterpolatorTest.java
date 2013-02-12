package de.lessvoid.nifty.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinearInterpolatorTest {
  private static final float EPSILON = 0.01f;

  @Test
  public void testWithTwoPoints() {
    LinearInterpolator interpolator = new LinearInterpolator();
    interpolator.addPoint(0.0f, 0.0f);
    interpolator.addPoint(100.0f, 1.0f);
    interpolator.prepare();
    assertEquals(0.0f, interpolator.getValue(-1.0f), EPSILON);
    assertEquals(0.0f, interpolator.getValue(0.0f), EPSILON);
    assertEquals(0.5f, interpolator.getValue(0.5f), EPSILON);
    assertEquals(1.0f, interpolator.getValue(1.0f), EPSILON);
    assertEquals(1.0f, interpolator.getValue(2.0f), EPSILON);
  }
  
  @Test
  public void testWithThreePoints() {
    LinearInterpolator interpolator = new LinearInterpolator();
    interpolator.addPoint(0.0f, 0.0f);
    interpolator.addPoint(100.0f, 0.0f);
    interpolator.addPoint(200.0f, 1.0f);
    interpolator.prepare();
    assertEquals(0.0f, interpolator.getValue(0.0f), EPSILON);
    assertEquals(0.0f, interpolator.getValue(0.5f), EPSILON);
    assertEquals(1.0f, interpolator.getValue(1.0f), EPSILON);
  }
}
