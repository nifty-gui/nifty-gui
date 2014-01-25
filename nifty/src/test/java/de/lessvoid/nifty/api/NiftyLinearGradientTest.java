package de.lessvoid.nifty.api;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NiftyLinearGradientTest {

  @Test
  public void testPoints() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    assertEquals(0.0, gradient.getX0());
    assertEquals(1.0, gradient.getY0());
    assertEquals(170.0, gradient.getX1());
    assertEquals(2.0, gradient.getY1());
  }

  @Test
  public void testSingleColor() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.BLACK()));
  }

  @Test
  public void testTwoColors() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(1.0, NiftyColor.GREEN());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.BLACK()),
        new NiftyColorStop(1.0, NiftyColor.GREEN()));
  }

  @Test
  public void testThreeColors() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(0.5, NiftyColor.RED());
    gradient.addColorStop(1.0, NiftyColor.GREEN());
    assertColorStops(
        gradient.getColorStops(),
        new NiftyColorStop(0.0, NiftyColor.BLACK()),
        new NiftyColorStop(0.5, NiftyColor.RED()),
        new NiftyColorStop(1.0, NiftyColor.GREEN()));
  }

  @Test
  public void testReplaceColor() {
    NiftyLinearGradient gradient = new NiftyLinearGradient(0.0, 1.0, 170.0, 2.0);
    gradient.addColorStop(0.0, NiftyColor.BLACK());
    gradient.addColorStop(0.0, NiftyColor.BLUE());
    assertColorStops(gradient.getColorStops(), new NiftyColorStop(0.0, NiftyColor.BLUE()));
  }

  private void assertColorStops(final List<NiftyColorStop> colorStops, final NiftyColorStop ... stops) {
    assertEquals(colorStops.size(), stops.length);
    for (int i=0; i<stops.length; i++) {
      assertTrue(colorStops.get(i).equals(stops[i]));
      assertTrue(colorStops.get(i).getColor().equals(stops[i].getColor()));
    }
  }
}
