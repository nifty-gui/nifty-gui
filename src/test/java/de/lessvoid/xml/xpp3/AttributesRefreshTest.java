package de.lessvoid.xml.xpp3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AttributesRefreshTest {
  private Attributes attributes = new Attributes();

  @Before
  public void setUp() {
    attributes.set("testKey", "testValue");
  }

  @Test
  public void testAddNewKey() {
    Attributes src = new Attributes();
    src.set("newKey", "newValue");
    attributes.refreshFromAttributes(src);
    assertEquals("testValue", attributes.get("testKey"));
    assertEquals("newValue", attributes.get("newKey"));
  }

  @Test
  public void testOverwriteExistingKey() {
    Attributes src = new Attributes();
    src.set("testKey", "newValue");
    attributes.refreshFromAttributes(src);
    assertEquals("newValue", attributes.get("testKey"));
  }

  @Test
  public void testRemoveExistingKey() {
    Attributes src = new Attributes();
    src.set("testKey", "");
    attributes.refreshFromAttributes(src);
    assertFalse(attributes.isSet("testKey"));
  }
}
