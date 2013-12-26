package de.lessvoid.xml.lwxs;

import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserFactory;


public class SchemaNullTest {
  private Schema schema;

  @Before
  public void setUp() throws Exception {
    schema = new Schema(XmlPullParserFactory.newInstance(), null);
  }

  @Test(expected = Exception.class)
  public void testPackageNull() throws Exception {
    Attributes attributes = new Attributes();
    schema.process(null, attributes);
  }

  @Test(expected = Exception.class)
  public void testRootNull() throws Exception {
    Attributes attributes = new Attributes();
    attributes.set("package", "package");
    schema.process(null, attributes);
  }

  @Test(expected = Exception.class)
  public void testTypeNull() throws Exception {
    Attributes attributes = new Attributes();
    attributes.set("package", "package");
    attributes.set("root", "root");
    schema.process(null, attributes);
  }
}
