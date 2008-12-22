package de.lessvoid.nifty.loader.xpp3.elements.helper;

import junit.framework.TestCase;

public class PaddingAttributeParserTest extends TestCase {
  private PaddingAttributeParser parser;

  public void setUp() {
  }

  public void testParseErrorNull() {
    try {
      parser = new PaddingAttributeParser(null);
      fail("should throw a Exception");
    } catch (Exception e) {
      assertEquals("parsing error, paddingString is null", e.getMessage());
    }
  }

  public void testParseErrorEmpty() {
    try {
      parser = new PaddingAttributeParser("");
      fail("should throw a Exception");
    } catch (Exception e) {
      assertEquals("parsing error, paddingString is empty", e.getMessage());
    }
  }

  public void testParseErrorInvalidArgCount() {
    try {
      parser = new PaddingAttributeParser("a,v,c,d,e");
      fail("should throw a Exception");
    } catch (Exception e) {
      assertEquals("parsing error, paddingString count error (5) expecting value from 1 to 4", e.getMessage());
    }
  }

  public void testParseSingleValue() throws Exception {
    parser = new PaddingAttributeParser("20px");
    assertPaddingValues("20px", "20px", "20px", "20px");
  }

  public void testParseTwoValues() throws Exception {
    parser = new PaddingAttributeParser("20px,10px");
    assertPaddingValues("10px", "20px", "10px", "20px");
  }

  public void testParseThreeValues() throws Exception {
    parser = new PaddingAttributeParser("20px,10px,30px");
    assertPaddingValues("10px", "20px", "10px", "30px");
  }

  public void testParseFourValues() throws Exception {
    parser = new PaddingAttributeParser("20px,10px,30px,40px");
    assertPaddingValues("40px", "20px", "10px", "30px");
  }

  private void assertPaddingValues(final String left, final String top, final String right, final String bottom) {
    assertEquals(left, parser.getPaddingLeft());
    assertEquals(top, parser.getPaddingTop());
    assertEquals(right, parser.getPaddingRight());
    assertEquals(bottom, parser.getPaddingBottom());
  }
}
