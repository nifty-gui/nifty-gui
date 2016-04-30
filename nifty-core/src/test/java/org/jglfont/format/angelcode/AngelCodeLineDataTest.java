package org.jglfont.format.angelcode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.junit.Test;



public class AngelCodeLineDataTest {
  private AngelCodeLineData lineData = new AngelCodeLineData();

  @Test
  public void testPut() {
    lineData.put("key", "value");
    assertEquals("value", lineData.getString("key"));
  }

  @Test
  public void testGetDefaultString() {
    assertEquals(null, lineData.getString("key"));
  }

  @Test
  public void testGetInteger() {
    lineData.put("key", "12");
    assertEquals(12, lineData.getInt("key"));
  }

  @Test
  public void testGetDefaultInteger() {
    assertEquals(0, lineData.getInt("key"));
  }

  @Test
  public void testGetErrorInteger() {
    lineData.put("key", "value");
    assertEquals(0, lineData.getInt("key"));
  }

  @Test
  public void testClear() {
    lineData.put("key", "12");
    assertEquals("12", lineData.getString("key"));

    lineData.clear();
    assertEquals(null, lineData.getString("key"));
  }

  @Test
  public void testDoesNotExists() {
    assertFalse(lineData.hasValue("key"));
  }

  @Test
  public void testExists() {
    lineData.put("key", "test");
    assertTrue(lineData.hasValue("key"));
  }
}
