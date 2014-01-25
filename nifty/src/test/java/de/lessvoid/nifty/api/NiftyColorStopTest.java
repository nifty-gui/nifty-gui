package de.lessvoid.nifty.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NiftyColorStopTest {

  @Test
  public void testCreate() {
    NiftyColorStop stop = new NiftyColorStop(1.0, NiftyColor.BLUE());
    assertEquals(1.0, stop.getStop());
    assertEquals("#0000ffff {0.0, 0.0, 1.0, 1.0}", stop.getColor().toString());
  }

  @Test
  public void testLower() {
    NiftyColorStop stopA = new NiftyColorStop(0.4, NiftyColor.BLUE());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.BLUE());
    assertTrue(stopA.compareTo(stopB) < 0);
  }

  @Test
  public void testHigher() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.BLUE());
    NiftyColorStop stopB = new NiftyColorStop(0.4, NiftyColor.BLUE());
    assertTrue(stopA.compareTo(stopB) > 0);
  }

  @Test
  public void testEquals() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.BLUE());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.BLUE());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsLower() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.BLACK());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.BLUE());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsHigher() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.BLUE());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.BLACK());
    assertTrue(stopA.compareTo(stopB) == 0);
  }
}
