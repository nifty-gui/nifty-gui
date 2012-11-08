package de.lessvoid.nifty;

import static de.lessvoid.nifty.AssertColor.assertColor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NiftyColorTest {

  @Test
  public void testConstruct() {
    NiftyColor c = new NiftyColor(1.f, .5f, .3f, .75f);
    assertColor(1.f, .5f, .3f, .75f, c);
  }

  @Test
  public void testCopy() {
    NiftyColor c = new NiftyColor(new NiftyColor(1.f, .5f, .3f, .75f));
    assertColor(1.f, .5f, .3f, .75f, c);
  }

  @Test
  public void testBLACK() {
    assertColor(0.f, 0.f, 0.f, 1.f, NiftyColor.BLACK());
  }

  @Test
  public void testWHITE() {
    assertColor(1.f, 1.f, 1.f, 1.f, NiftyColor.WHITE());
  }

  @Test
  public void testRED() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.RED());
  }

  @Test
  public void testGREEN() {
    assertColor(0.f, 1.f, 0.f, 1.f, NiftyColor.GREEN());
  }

  @Test
  public void testBLUE() {
    assertColor(0.f, 0.f, 1.f, 1.f, NiftyColor.BLUE());
  }

  @Test
  public void testYELLOW() {
    assertColor(1.f, 1.f, 0.f, 1.f, NiftyColor.YELLOW());
  }

  @Test
  public void testNONE() {
    assertColor(0.f, 0.f, 0.f, 0.f, NiftyColor.NONE());
  }

  @Test
  public void testLongColor() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.fromString("#ff0000ff"));
  }

  @Test
  public void testShortColor() {
    assertColor(1.f, 0.f, 0.f, 1.f, NiftyColor.fromString("#f00f"));
  }

  @Test
  public void testFromColorWithAlpha() {
    assertColor(1.f, 0.f, 0.f, .45f, NiftyColor.fromColorWithAlpha(NiftyColor.RED(), 0.45f));
  }

  @Test
  public void testFromInt() {
    assertColor(1.f, 0.f, 1.f, 0.f, NiftyColor.fromInt(0xFF00FF00));
  }

  @Test
  public void testFromHSV() {
    assertColor(.5f, 0.25f, 0.25f, 1.f, NiftyColor.fromHSV(0.5f, 0.5f, 0.5f));
  }

  @Test
  public void testFromRandom() {
    NiftyColor.randomColor();
  }

  @Test
  public void testSet() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, NiftyColor.BLACK().setRed(0.1f).setGreen(0.2f).setBlue(0.3f).setAlpha(0.4f));
  }

  @Test
  public void testUpdate() {
    assertColor(0.1f, 0.2f, 0.3f, 0.4f, NiftyColor.BLACK().update(new NiftyColor(0.1f, 0.2f, 0.3f, 0.4f)));
  }

  @Test
  public void testLinear() {
    NiftyColor start = NiftyColor.NONE();
    NiftyColor end = NiftyColor.WHITE();
    assertColor(0.5f, 0.5f, 0.5f, 0.5f, start.linear(start, end, 0.5f));
  }

  @Test
  public void testMultiply() {
    NiftyColor start = NiftyColor.RED();
    assertColor(0.25f, 0.f, 0.f, 0.25f, start.mutiply(0.25f));
  }

  @Test
  public void testToString() {
    assertEquals("(0.0,0.0,1.0,1.0)", NiftyColor.BLUE().toString());
  }

  @Test
  public void testHashCode() {
    assertEquals(-804382847L, NiftyColor.RED().hashCode());
  }

  @Test
  public void testEquals() {
    NiftyColor a = NiftyColor.NONE();
    NiftyColor b = new NiftyColor(0.f, 0.f, 0.f, 0.f);
    assertTrue(a.equals(b));
  }

  @Test
  public void testEqualsSame() {
    NiftyColor a = NiftyColor.NONE();
    NiftyColor b = a;
    assertTrue(a.equals(b));
  }
}
