package de.lessvoid.xml.lwxs.elements;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.xml.lwxs.Schema;

public class ElementTest {
  private Schema schemaMock;
  private XmlProcessorType schemaXmlElementProcessorMock;
  private Collection < Element > collection = new ArrayList < Element >();

  @Before
  public void prepare() {
    schemaMock = createMock(Schema.class);
    schemaXmlElementProcessorMock = createMock(XmlProcessorType.class);
  }

  @After
  public void verifyStuff() {
    verify(schemaMock);
    verify(schemaXmlElementProcessorMock);
  }

  @Test
  public void noChildren() throws Exception {
    Type typeMock = createMock(Type.class);
    typeMock.addChildren(schemaMock, schemaXmlElementProcessorMock, "Name", "Type", OccursEnum.optional);
    expect(typeMock.getTypeParent(schemaMock)).andReturn(null);
    replay(typeMock);
    replay(schemaXmlElementProcessorMock);

    expect(schemaMock.getType("Type")).andReturn(typeMock);
    replay(schemaMock);

    Element element = new Element("Name", "Type", OccursEnum.optional);
    element.addToProcessor(schemaMock, schemaXmlElementProcessorMock);

    verify(typeMock);
  }
}
