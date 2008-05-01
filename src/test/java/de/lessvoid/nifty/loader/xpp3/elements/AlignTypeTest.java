package de.lessvoid.nifty.loader.xpp3.elements;

import junit.framework.TestCase;

public class AlignTypeTest extends TestCase {
  
  public void testFromStringMatch() {
    assertEquals(AlignType.center, AlignType.valueOf("center"));
  }

  public void testFromStringMatchesNot() {
    try {
      AlignType.valueOf("center2");
      fail("should have thrown exception");
    } catch (Exception e) {
      assertEquals("No enum const class de.lessvoid.nifty.loader.xpp3.elements.AlignType.center2", e.getMessage());
    }
  }

  public void testFromStringMatchesNotUpperCase() {
    try {
      AlignType.valueOf("CENTER");
      fail("should have thrown exception");
    } catch (Exception e) {
      assertEquals("No enum const class de.lessvoid.nifty.loader.xpp3.elements.AlignType.CENTER", e.getMessage());
    }
  }
}
