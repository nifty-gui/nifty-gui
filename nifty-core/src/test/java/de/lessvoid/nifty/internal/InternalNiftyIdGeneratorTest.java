package de.lessvoid.nifty.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.internal.InternalNiftyIdGenerator;

public class InternalNiftyIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(InternalNiftyIdGenerator.generate());
    int second = Integer.valueOf(InternalNiftyIdGenerator.generate());
    assertTrue(second > first);
  }
}
