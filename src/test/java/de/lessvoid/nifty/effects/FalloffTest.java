package de.lessvoid.nifty.effects;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.effects.Falloff.HoverFalloffConstraint;
import de.lessvoid.nifty.effects.Falloff.HoverFalloffType;


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
