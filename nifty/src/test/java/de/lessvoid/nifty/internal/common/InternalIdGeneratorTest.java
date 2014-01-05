package de.lessvoid.nifty.internal.common;

import static org.junit.Assert.*;

import org.junit.Test;

import de.lessvoid.nifty.internal.common.IdGenerator;

public class InternalIdGeneratorTest {

  @Test
  public void test() {
    int first = Integer.valueOf(IdGenerator.generate());
    int second = Integer.valueOf(IdGenerator.generate());
    assertTrue(second > first);
  }
}
