package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class TypeProcessor implements XmlProcessor {
  private final Schema niftyXmlSchema;

  public TypeProcessor(final Schema niftyXmlSchemaParam) {
    niftyXmlSchema = niftyXmlSchemaParam;
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String name = getNameAttribute(attributes);

    Type type = new Type(name, getExtendsAttribute(attributes));
    niftyXmlSchema.addType(name, type);

    SubstitutionGroup substGroup = new SubstitutionGroup();
    substGroup.add("element", new TypeProcessorElement(type));
    substGroup.add("group", new TypeProcessorSubstitutionGroup(type));

    xmlParser.nextTag();
    xmlParser.zeroOrMore(substGroup);
  }

  private String getNameAttribute(final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    if (name == null) {
      throw new Exception("[name] attribute is a required attribute");
    }
    return name;
  }

  private String getExtendsAttribute(final Attributes attributes) throws Exception {
    return attributes.get("extends");
  }
}
