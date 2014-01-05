package de.lessvoid.nifty.api;

import static de.lessvoid.nifty.AssertColor.assertColor;

import org.junit.Test;

public class NiftyMutableColorTest {
  private NiftyMutableColor color = new NiftyMutableColor(NiftyColor.TRANSPARENT());

  @Test
  public void testSet() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, color.setRed(0.1f).setGreen(0.2f).setBlue(0.3f).setAlpha(0.4f));
  }

  @Test
  public void testUpdate() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, color.update(new NiftyColor(0.1f, 0.2f, 0.3f, 0.4f)));
  }

  @Test
  public void testLinear() {
    NiftyColor start = NiftyColor.NONE();
    NiftyColor end = NiftyColor.WHITE();
    assertColor(0.5f, 0.5f, 0.5f, 0.5f, color.linear(start, end, 0.5f));
  }

  @Test
  public void testMultiply() {
    assertColor(0.25f, 0.f, 0.f, 0.25f, color.mutiply(0.25f));
  }

}
