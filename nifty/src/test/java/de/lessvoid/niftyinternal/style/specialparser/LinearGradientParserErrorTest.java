package de.lessvoid.niftyinternal.style.specialparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class LinearGradientParserErrorTest {
  private final LinearGradientParser parser = new LinearGradientParser();

  @Test
  public void testUnexpectedStart() throws Exception {
    execWithError("#", "unexpected token (#) in token stream");
  }

  @Test
  public void testMissingBracket() throws Exception {
    execWithError("linear-gradient#", "unexpected token (#) in token stream");
  }

  @Test
  public void testOnlyOneColorstop() throws Exception {
    execWithError("linear-gradient(red", "no more tokens");
  }

  @Test
  public void testOneColorstop() throws Exception {
    execWithError("linear-gradient(red#", "unexpected token (#) in token stream");
  }

  @Test
  public void testTwoColorstopsNoRightParenthesis() throws Exception {
    execWithError("linear-gradient(red, green", "no more tokens");
  }

  @Test
  public void testTwoColorstopsMissingSemicolon() throws Exception {
    execWithError("linear-gradient(red, green)", "no more tokens");
  }

  @Test
  public void testSingleColorstopWithPercentMissing() throws Exception {
    execWithError("linear-gradient(red 10);", "unexpected token ()) in token stream");
  }

  @Test
  public void testStartsWithAngleButWrongToken() throws Exception {
    execWithError("linear-gradient(10#", "expected angle unit identifier here but was (#)");
  }

  @Test
  public void testStartsWithAngleButUnsupportedUnit() throws Exception {
    execWithError("linear-gradient(10unit", "unsupported angle unit (unit)");
  }

  @Test
  public void testStartsWithToInvalid() throws Exception {
    execWithError("linear-gradient(to, red);", "expected identifier token after 'to' but was (,)");
  }

  @Test
  public void testToInvalidKeyWord() throws Exception {
    execWithError("linear-gradient(to stuff, red);", "expecting ([left, right]) or ([bottom, top])");
  }

  @Test
  public void testToInvalidSecondWord() throws Exception {
    execWithError("linear-gradient(to left stuff, red);", "expecting ([bottom, top]) after (left)");
  }

  @Test
  public void testToInvalidSecondWordRepeated() throws Exception {
    execWithError("linear-gradient(to left left, red);", "expecting ([bottom, top]) after (left)");
  }

  @Test
  public void testToInvalidSecondWord2() throws Exception {
    execWithError("linear-gradient(to bottom stuff, red);", "expecting ([left, right]) after (bottom)");
  }

  private void execWithError(final String parse, final String expected) {
    try {
      parser.parse(parse);
      fail("expected exception");
    } catch (Exception e) {
      assertEquals(expected, e.getMessage());
    }
  }
}
