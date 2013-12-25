package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.effects.Falloff.HoverFalloffConstraint;
import de.lessvoid.nifty.effects.Falloff.HoverFalloffType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class FalloffTest {

  @Test
  public void testHoverFalloffType() {
    assertEquals("none", HoverFalloffType.none.toString());
  }

  @Test
  public void testHoverFalloffConstraint() {
    assertEquals("none", HoverFalloffConstraint.none.toString());
  }
}
