package de.lessvoid.xml.xpp3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AttributesMergeAndTagTest {
  private Attributes attributes = new Attributes();

  @Before
  public void setUp() {
    attributes.set("testKey", "testValue");
  }

  @Test
  public void testAddNewTaggedKey() {
    Attributes src = new Attributes();
    src.set("newKey", "newValue");
    attributes.mergeAndTag(src, "tag");
    assertNull(attributes.getWithTag("testKey", "tag"));
    assertEquals("newValue", attributes.getWithTag("newKey", "tag"));
    assertEquals("testValue", attributes.get("testKey"));
  }

  @Test
  public void testOverwriteExistingKey() {
    Attributes src = new Attributes();
    src.set("testKey", "newValue");
    attributes.mergeAndTag(src, "tag");
    assertEquals("newValue", attributes.get("testKey"));
    assertNotNull(attributes.getWithTag("testKey", "tag"));
    assertEquals("newValue", attributes.getWithTag("testKey", "tag"));
  }
}
