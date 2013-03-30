package de.lessvoid.nifty.internal;

import static de.lessvoid.nifty.AssertColor.assertColor;

import org.junit.Test;

import de.lessvoid.nifty.internal.InternalNiftyColorStringParser;

public class InternalNiftyColorStringParserTest {
  private InternalNiftyColorStringParser parser = new InternalNiftyColorStringParser();

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
