package de.lessvoid.xml.lwxs.elements;

import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.processor.TypeProcessorElement;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

public class ElementProcessorTest {
  private TypeProcessorElement elementProcessor;
  private Schema niftyXmlSchema;
  private XmlParser xmlParserMock;
  private Type parentMock;

  @Before
  public void setUp() {
    niftyXmlSchema = new Schema(null);
    parentMock = createMock(Type.class);
    elementProcessor = new TypeProcessorElement(parentMock);
    xmlParserMock = createMock(XmlParser.class);
  }

  @After
  public void tearDown() {
    verify(xmlParserMock);
    verify(parentMock);
  }

  @Test(expected = Exception.class)
  public void testMissingName() throws Exception {
    replay(xmlParserMock);
    replay(parentMock);
    Attributes attributes = new Attributes();
    elementProcessor.process(xmlParserMock, attributes);
  }

  @Test(expected = Exception.class)
  public void testMissingType() throws Exception {
    replay(xmlParserMock);
    replay(parentMock);
    Attributes attributes = new Attributes();
    attributes.set("name", "name");
    elementProcessor.process(xmlParserMock, attributes);
  }

  @Test
  public void testDefaultOccures() throws Exception {
    xmlParserMock.nextTag();
    replay(xmlParserMock);
    parentMock.addElement(isA(Element.class));
    replay(parentMock);
    Attributes attributes = new Attributes();
    attributes.set("name", "name");
    attributes.set("type", "type");
    elementProcessor.process(xmlParserMock, attributes);
  }

  @Test
  public void testWithOccures() throws Exception {
    xmlParserMock.nextTag();
    replay(xmlParserMock);
    parentMock.addElement(isA(Element.class));
    replay(parentMock);
    Attributes attributes = new Attributes();
    attributes.set("name", "name");
    attributes.set("type", "type");
    attributes.set("occures", "optional");
    elementProcessor.process(xmlParserMock, attributes);
  }
}
