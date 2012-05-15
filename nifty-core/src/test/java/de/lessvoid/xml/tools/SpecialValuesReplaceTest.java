package de.lessvoid.xml.tools;

import static org.junit.Assert.assertEquals;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the class {@link SpecialValuesReplace}.
 * 
 * @author Marc Pompl
 * @author void (added some stuff)
 * @created 13.06.2010
 */
public class SpecialValuesReplaceTest {

  @Test
  public void testParseSimpleInput() {
    Assert.assertEquals("", SpecialValuesReplace.replace(null, null, null, null));
    Assert.assertEquals("", SpecialValuesReplace.replace("", null, null, null));
    Assert.assertEquals("abc", SpecialValuesReplace.replace("abc", null, null, null));
    Assert.assertEquals("${abc}", SpecialValuesReplace.replace("${abc}", null, null, null));
  }

  @Test
  public void testParsePropInput() {
    Assert.assertEquals("${PROP.doesNotExist}", SpecialValuesReplace.replace("${PROP.doesNotExist}", null, null, null));
    System.getProperties().setProperty("nifty.test", "existsInSystem");
    Properties myProperties = new Properties();
    myProperties.setProperty("nifty.test", "existsInMyProperties");
    Assert.assertEquals("existsInSystem", SpecialValuesReplace.replace("${PROP.nifty.test}", null, null, null));
    Assert.assertEquals("existsInMyProperties", SpecialValuesReplace.replace("${PROP.nifty.test}", null, null, myProperties));
  }

  @Test
  public void testParseEnvInput() {
    Assert.assertEquals("${ENV.doesNotExist}", SpecialValuesReplace.replace("${ENV.doesNotExist}", null, null, null));
    // Existence of PATH may be system dependant...
    String path = SpecialValuesReplace.replace("${ENV.PATH}", null, null, null);
    Assert.assertNotNull(path);
    Assert.assertTrue(path.length() > 0);
    Assert.assertFalse("ENV.PATH".equals(path));
  }

  @Test
  public void testParseObjectInput() {
    Assert.assertEquals("${CALL.getValue()}", SpecialValuesReplace.replace("${CALL.getValue()}", null, null, null));
    Assert.assertEquals("called", SpecialValuesReplace.replace("${CALL.getValue()}", null, new MyObjectCallback(), null));
    Assert.assertEquals("${CALL.getNonExisting()}", SpecialValuesReplace.replace("${CALL.getNonExisting()}", null, null, null));
  }

  @Test
  public void testQuoting() {
    Assert.assertEquals("${CALL.getValue()}", SpecialValuesReplace.replace("\\${CALL.getValue()}", null, new MyObjectCallback(), null));
  }

  @Test
  public void testLocalize() {
    Map<String, ResourceBundle> resources = new Hashtable<String, ResourceBundle>();
    resources.put("id", new ResourceBundleMock());
    assertEquals("${notfound}", SpecialValuesReplace.replace("${notfound}", resources, null, null));
    assertEquals("${unknown.notfound}", SpecialValuesReplace.replace("${unknown.notfound}", resources, null, null));
    assertEquals("value", SpecialValuesReplace.replace("${id.test}", resources, null, null));
  }

  private class ResourceBundleMock extends ResourceBundle {
    private Vector<String> data = new Vector<String>();
    public ResourceBundleMock() {
      data.add("value");
    }
    @Override
    public Enumeration<String> getKeys() {
      return data.elements();
    }
    @Override
    protected Object handleGetObject(String key) {
      return data.get(0);
    }
  }

  private class MyObjectCallback {
    @SuppressWarnings ("unused")
    public String getValue() {
      return "called";
    }
  }
}
