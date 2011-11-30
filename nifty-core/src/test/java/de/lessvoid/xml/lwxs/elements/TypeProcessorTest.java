package de.lessvoid.xml.lwxs.elements;

import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.processor.TypeProcessor;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

public class TypeProcessorTest {
  private TypeProcessor typeProcessor;
  private Schema niftyXmlSchema;
  private XmlParser xmlParserMock;

  @Before
  public void setUp() {
    niftyXmlSchema = new Schema(null);
    typeProcessor = new TypeProcessor(niftyXmlSchema);
    xmlParserMock = createMock(XmlParser.class);
  }

  @After
  public void tearDown() {
    verify(xmlParserMock);
  }

  @Test
  public void testSimple() throws Exception {
    Attributes attributes = new Attributes();
    attributes.set("name", "testType");

    xmlParserMock.nextTag();
    xmlParserMock.zeroOrMore(isA(de.lessvoid.xml.xpp3.SubstitutionGroup.class));
    replay(xmlParserMock);

    typeProcessor.process(xmlParserMock, attributes);

    Type type = niftyXmlSchema.getType("testType");
    assertNotNull(type);
  }

  @Test(expected = Exception.class)
  public void testNameMissing() throws Exception {
    replay(xmlParserMock);

    Attributes attributes = new Attributes();
    typeProcessor.process(null, attributes);
  }
}
