package de.lessvoid.xml.lwxs.elements;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserFactory;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.processor.TypeProcessor;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

public class TypeProcessorTest {
  private TypeProcessor typeProcessor;
  private Schema niftyXmlSchema;
  private XmlParser xmlParserMock;

  @Before
  public void setUp() throws Exception {
    niftyXmlSchema = new Schema(XmlPullParserFactory.newInstance(), null);
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
}
