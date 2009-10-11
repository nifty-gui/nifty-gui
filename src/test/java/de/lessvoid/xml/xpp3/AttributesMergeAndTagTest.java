package de.lessvoid.xml.xpp3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AttributesMergeAndTagTest {
  private static final String EXISTING_KEY = "testKey";
  private static final String EXISTING_VALUE = "testValue";
  private static final String NEW_KEY = "newKey";
  private static final String NEW_VALUE = "newValue";
  private static final String TAG = "tag";

  private Attributes attributes = new Attributes();

  @Before
  public void setUp() {
    attributes.set(EXISTING_KEY, EXISTING_VALUE);
  }

  @Test
  public void testAddNewTaggedKey() {
    addNewKeyAndTag();

    assertEquals(NEW_VALUE, attributes.get(NEW_KEY));
    assertEquals(NEW_VALUE, attributes.getWithTag(NEW_KEY, TAG));

    assertEquals(EXISTING_VALUE, attributes.get(EXISTING_KEY));
    assertNull(attributes.getWithTag(EXISTING_KEY, TAG));
  }

  @Test
  public void testOverwriteExistingKeyAndTag() {
    // overwriting existing keys is not possible and will be ignored
    // in the loader a style is applied to attributes from the elements and
    // we now want to overwrite only attributes that are not already set by the element.
    overwriteExistingKeyWithNewValueAndTag();

    assertEquals(EXISTING_VALUE, attributes.get(EXISTING_KEY));
    assertNull(attributes.getWithTag(EXISTING_KEY, TAG));
  }

  @Test
  public void testOverwriteExistingKey() {
    overwriteExistingKeyWithNewValue();

    assertEquals(EXISTING_VALUE, attributes.get(EXISTING_KEY));
    assertNull(attributes.getWithTag(EXISTING_KEY, TAG));
  }

  private void addNewKeyAndTag() {
    Attributes src = new Attributes();
    src.set(NEW_KEY, NEW_VALUE);
    attributes.mergeAndTag(src, TAG);
  }

  private void overwriteExistingKeyWithNewValueAndTag() {
    Attributes src = new Attributes();
    src.set(EXISTING_KEY, NEW_VALUE);
    attributes.mergeAndTag(src, TAG);
  }

  private void overwriteExistingKeyWithNewValue() {
    Attributes src = new Attributes();
    src.set(EXISTING_KEY, NEW_VALUE);
    attributes.merge(src);
  }
}
