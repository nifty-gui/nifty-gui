package de.lessvoid.nifty.tools;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ColorValidatorTest {
  private ColorValidator validator = new ColorValidator();

  @Test
  public void testInvalid() {
    assertFalse(validator.isValid(null));
    assertFalse(validator.isValid("#"));
  }

  @Test
  public void testShortModeInvalid() {
    assertFalse(validator.isValid("#f"));
    assertFalse(validator.isValid("#00"));
    assertFalse(validator.isValid("#000"));    
  }

  @Test
  public void testLongModeInvalid() {
    assertFalse(validator.isValid("#ff0000"));
  }
  
  @Test
  public void testShortModeValid() {
    assertTrue(validator.isValid("#f00F"));
    assertTrue(validator.isValid("#1230"));
  }

  @Test
  public void testLongModeValid() {
    assertTrue(validator.isValid("#ff0000FF"));
    assertTrue(validator.isValid("#11223300"));
  }

  @Test
  public void testShortModeIncomplete() {
    assertTrue(validator.isShortModeWithoutAlpha("#123"));
  }

  @Test
  public void testShortModeIncompleteInvalid() {
    assertFalse(validator.isShortModeWithoutAlpha("#123F"));
  }

  @Test
  public void testLongModeIncomplete() {
    assertTrue(validator.isLongModeWithoutAlpha("#112233"));
  }

  @Test
  public void testLongModeIncompleteInvalid() {
    assertFalse(validator.isLongModeWithoutAlpha("#112233FF"));
  }
}
