package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TypeProcessor implements XmlProcessor {
  @Nonnull
  private final Schema niftyXmlSchema;

  public TypeProcessor(@Nonnull final Schema niftyXmlSchemaParam) {
    niftyXmlSchema = niftyXmlSchemaParam;
  }

  @Override
  public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
    String name = getNameAttribute(attributes);

    Type type = new Type(name, getExtendsAttribute(attributes));
    niftyXmlSchema.addType(name, type);

    SubstitutionGroup substGroup = new SubstitutionGroup();
    substGroup.add("element", new TypeProcessorElement(type));
    substGroup.add("group", new TypeProcessorSubstitutionGroup(type));

    xmlParser.nextTag();
    xmlParser.zeroOrMore(substGroup);
  }

  @Nonnull
  private String getNameAttribute(@Nonnull final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    if (name == null) {
      throw new Exception("[name] attribute is a required attribute");
    }
    return name;
  }

  @Nullable
  private String getExtendsAttribute(@Nonnull final Attributes attributes) {
    return attributes.get("extends");
  }
}
