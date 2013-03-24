package de.lessvoid.nifty.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.internal.NiftyIdGenerator;

public class NiftyIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(NiftyIdGenerator.generate());
    int second = Integer.valueOf(NiftyIdGenerator.generate());
    assertTrue(second > first);
  }
}
