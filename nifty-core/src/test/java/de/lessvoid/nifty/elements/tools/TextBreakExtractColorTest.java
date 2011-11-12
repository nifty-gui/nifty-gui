package de.lessvoid.nifty.elements.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TextBreakExtractColorTest {
  private TextBreak textBreak = new TextBreak("line", 20, null);

  @Test
  public void testWithNull() {
    assertNull(textBreak.extractColorValue(null));
  }

  @Test
  public void testWithEmptyString() {
    assertNull(textBreak.extractColorValue(""));
  }

  @Test
  public void testNoColorValue() {
    assertNull(textBreak.extractColorValue("hallo"));
  }

  @Test
  public void testColorValueAtBeginning() {
    assertEquals("\\#ff00ff#", textBreak.extractColorValue("\\#ff00ff#hallo"));
  }

  @Test
  public void testColorValueAtEnd() {
    assertEquals("\\#ff00ff#", textBreak.extractColorValue("hallo\\#ff00ff#"));
  }
}
