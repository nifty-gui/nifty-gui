package de.lessvoid.nifty.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class NiftyIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(NiftyIdGenerator.generate());
    int second = Integer.valueOf(NiftyIdGenerator.generate());
    assertTrue(second > first);
  }
}
