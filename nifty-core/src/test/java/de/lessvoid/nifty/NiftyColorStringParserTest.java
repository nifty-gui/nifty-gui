package de.lessvoid.nifty;

import static de.lessvoid.nifty.AssertColor.assertColor;

import org.junit.Test;

public class NiftyColorStringParserTest {
  private NiftyColorStringParser parser = new NiftyColorStringParser();

  @Test
  public void testShortWithoutAlpha() {
    assertColor(1.f, 0.f, 0.55f, 1.f, parser.fromString("#f08"));
  }

  @Test
  public void testLongWithoutAlpha() {
    assertColor(1.f, 0.f, 0.55f, 1.f, parser.fromString("#ff0088"));
  }

  @Test
  public void testShort() {
    assertColor(1.f, 0.f, 0.55f, 0.f, parser.fromString("#f080"));
  }

  @Test
  public void testLong() {
    assertColor(1.f, 0.f, 0.55f, 0.f, parser.fromString("#ff008800"));
  }

  @Test
  public void testInvalid() {
    assertColor(1.f, 1.f, 1.f, 1.f, parser.fromString("bla"));
  }
}
