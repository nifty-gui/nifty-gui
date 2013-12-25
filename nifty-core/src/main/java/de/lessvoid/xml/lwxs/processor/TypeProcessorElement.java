package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.xml.lwxs.elements.Element;
import de.lessvoid.xml.lwxs.elements.OccursEnum;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

import javax.annotation.Nonnull;

public class TypeProcessorElement implements XmlProcessor {
  private final Type parent;

  public TypeProcessorElement(final Type parentParam) {
    parent = parentParam;
  }

  @Override
  public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    if (name == null) {
      throw new Exception("[name] attribute is a required attribute");
    }
    String type = attributes.get("type");
    if (type == null) {
      throw new Exception("[type] attribute is a required attribute");
    }
    OccursEnum occures = OccursEnum.optional;
    if (attributes.get("occurs") != null) {
      occures = OccursEnum.valueOf(attributes.get("occurs"));
    }
    Element element = new Element(name, type, occures);
    parent.addElement(element);

    xmlParser.nextTag();
  }
}
