package de.lessvoid.xml.xpp3;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlParameterExtractTest {
  private final ControlParameter controlParameter = new ControlParameter();

  @Test
  public void testStandard() {
    assertEquals("huhu", controlParameter.extractParameter("$huhu"));
  }

  @Test
  public void testNew() {
    assertEquals("huhu", controlParameter.extractParameter("$(huhu)"));
  }

  @Test
  public void testNewInFront() {
    assertEquals("huhu", controlParameter.extractParameter("$(huhu)stuff"));
  }

  @Test
  public void testNewBehind() {
    assertEquals("huhu", controlParameter.extractParameter("stuff$(huhu)"));
  }

  @Test
  public void testNewBehindAndInFront() {
    assertEquals("huhu", controlParameter.extractParameter("stuff$(huhu)stuff"));
  }
}
