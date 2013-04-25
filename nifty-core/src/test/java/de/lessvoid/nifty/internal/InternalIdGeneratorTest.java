package de.lessvoid.nifty.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.internal.InternalIdGenerator;

public class InternalIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(InternalIdGenerator.generate());
    int second = Integer.valueOf(InternalIdGenerator.generate());
    assertTrue(second > first);
  }
}
