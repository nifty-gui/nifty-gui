package de.lessvoid.xml.xpp3;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlParameterTest {
  private final ControlParameter controlParameter = new ControlParameter();

  @Test
  public void testStandard() {
    assertTrue(controlParameter.isParameter("$huhu"));
  }

  @Test
  public void testNew() {
    assertTrue(controlParameter.isParameter("$(huhu)"));
  }

  @Test
  public void testNewSubStringInFront() {
    assertTrue(controlParameter.isParameter("stuff$(huhu)"));
  }

  @Test
  public void testNewSubStringAfter() {
    assertTrue(controlParameter.isParameter("$(huhu)stuff"));
  }

  @Test
  public void testNewSubStringAfterAndInFront() {
    assertTrue(controlParameter.isParameter("stuff$(huhu)stuff"));
  }

  @Test
  public void testNoParameter() {
    assertFalse(controlParameter.isParameter("${huhu}"));
  }
}
