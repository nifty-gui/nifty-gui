package de.lessvoid.xml.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class {@link SpecialValuesReplace}.
 *
 * @author Marc Pompl
 * @author void (added some stuff)
 */
public class SpecialValuesReplaceTest {

  @Test
  public void testParseSimpleInput() {
    Assert.assertEquals("", SpecialValuesReplace.replace(null, null, null, null, null));
    Assert.assertEquals("", SpecialValuesReplace.replace("", null, null, null, null));
    Assert.assertEquals("abc", SpecialValuesReplace.replace("abc", null, null, null, null));
    Assert.assertEquals("${abc}", SpecialValuesReplace.replace("${abc}", null, null, null, null));
  }

  @Test
  public void testParsePropInput() {
    Assert.assertEquals("${PROP.doesNotExist}", SpecialValuesReplace.replace("${PROP.doesNotExist}", null, null, null, null));
    System.getProperties().setProperty("nifty.test", "existsInSystem");
    Properties myProperties = new Properties();
    myProperties.setProperty("nifty.test", "existsInMyProperties");
    Assert.assertEquals("existsInSystem", SpecialValuesReplace.replace("${PROP.nifty.test}", null, null, null, null));
    Assert.assertEquals("existsInMyProperties", SpecialValuesReplace.replace("${PROP.nifty.test}", null, null,
        myProperties, null));
  }

  @Test
  public void testParseEnvInput() {
    Assert.assertEquals("${ENV.doesNotExist}", SpecialValuesReplace.replace("${ENV.doesNotExist}", null, null, null, null));
    // Existence of PATH may be system dependant...
    String path = SpecialValuesReplace.replace("${ENV.PATH}", null, null, null, null);
    Assert.assertNotNull(path);
    Assert.assertTrue(path.length() > 0);
    Assert.assertFalse("ENV.PATH".equals(path));
  }

  @Test
  public void testParseObjectInput() {
    Assert.assertEquals("${CALL.getValue()}", SpecialValuesReplace.replace("${CALL.getValue()}", null, null, null, null));
    Assert.assertEquals("called", SpecialValuesReplace.replace("${CALL.getValue()}", null, new MyObjectCallback(),
        null, null));
    Assert.assertEquals("${CALL.getNonExisting()}", SpecialValuesReplace.replace("${CALL.getNonExisting()}", null,
        null, null, null));
  }

  @Test
  public void testQuoting() {
    Assert.assertEquals("${CALL.getValue()}", SpecialValuesReplace.replace("\\${CALL.getValue()}", null,
        new MyObjectCallback(), null, null));
  }

  @Test
  public void testLocalize() {
    Map<String, String> resources = new HashMap<String, String>();
    resources.put("id", "stuff");
//    assertEquals("${notfound}", SpecialValuesReplace.replace("${notfound}", resources, null, null, null));
//    assertEquals("${unknown.notfound}", SpecialValuesReplace.replace("${unknown.notfound}", resources, null, null, null));
    assertEquals("value", SpecialValuesReplace.replace("${id.test}", resources, null, null, null));
  }

  private class MyObjectCallback {
    public String getValue() {
      return "called";
    }
  }
}
