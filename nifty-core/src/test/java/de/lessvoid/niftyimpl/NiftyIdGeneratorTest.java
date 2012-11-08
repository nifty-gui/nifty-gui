package de.lessvoid.niftyimpl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.niftyimpl.NiftyIdGenerator;

public class NiftyIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(NiftyIdGenerator.generate());
    int second = Integer.valueOf(NiftyIdGenerator.generate());
    assertTrue(second > first);
  }
}
