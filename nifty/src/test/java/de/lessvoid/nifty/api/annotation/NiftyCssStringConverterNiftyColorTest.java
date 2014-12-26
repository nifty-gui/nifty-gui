package de.lessvoid.nifty.api.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.api.NiftyColor;

public class NiftyCssStringConverterNiftyColorTest {
  private NiftyCssStringConverterNiftyColor converter = new NiftyCssStringConverterNiftyColor();

  @Test
  public void testToString() {
    assertEquals("#ff0000ff", converter.toString(NiftyColor.RED()));
  }

  @Test
  public void testFromString() {
    assertEquals(NiftyColor.RED(), converter.fromString("#ff0000ff"));
  }

  @Test
  public void testFromStringNoAlpha() {
    assertEquals(NiftyColor.RED(), converter.fromString("#ff0000"));
  }
}
