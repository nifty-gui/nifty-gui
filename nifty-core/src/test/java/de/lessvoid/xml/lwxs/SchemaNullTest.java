package de.lessvoid.xml.lwxs;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.xpp3.Attributes;


public class SchemaNullTest {
  private Schema schema;

  @Before
  public void setUp() {
    schema = new Schema(null);
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
