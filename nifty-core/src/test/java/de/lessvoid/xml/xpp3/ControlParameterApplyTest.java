package de.lessvoid.xml.xpp3;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlParameterApplyTest {
  private final ControlParameter controlParameter = new ControlParameter();

  @Test
  public void testStandard() {
    assertEquals("4711", controlParameter.applyParameter("$key", "key", "4711"));
  }

  @Test
  public void testNew() {
    assertEquals("4711", controlParameter.applyParameter("$(key)", "key", "4711"));
  }

  @Test
  public void testNewStuffBefore() {
    assertEquals("stuff4711", controlParameter.applyParameter("stuff$(key)", "key", "4711"));
  }

  @Test
  public void testNewStuffAfter() {
    assertEquals("4711stuff", controlParameter.applyParameter("$(key)stuff", "key", "4711"));
  }

  @Test
  public void testNewStuffBeforeAndAfter() {
    assertEquals("stuff4711stuff", controlParameter.applyParameter("stuff$(key)stuff", "key", "4711"));
  }
}
