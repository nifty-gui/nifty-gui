package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

import javax.annotation.Nonnull;

public class Element {
  @Nonnull
  private final String tagName;
  @Nonnull
  private final String tagType;
  @Nonnull
  private final OccursEnum occurs;

  public Element(
      @Nonnull final String elementNameParam,
      @Nonnull final String elementTypeParam,
      @Nonnull final OccursEnum elementOccursParam) throws Exception {
    tagName = elementNameParam;
    tagType = elementTypeParam;
    occurs = elementOccursParam;
  }

  public void addToProcessor(@Nonnull final Schema schema, @Nonnull final XmlProcessorType processor) throws Exception {
    Type type = schema.getType(tagType);
    type.addChildren(schema, processor, tagName, tagType, occurs);

    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      typeParent.addChildren(schema, processor, tagName, tagType, occurs);
    }
  }

  public void addToSubstGroup(
      @Nonnull final Schema schema,
      @Nonnull final SubstitutionGroup substitutionGroup,
      @Nonnull final XmlType xmlType) throws Exception {
    Type type = schema.getType(tagType);
    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          typeParent.createXmlProcessorFromType(schema, type), tagName, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    } else {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          type.createXmlProcessor(schema), tagName, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    }
  }

  @Nonnull
  public String getTagName() {
    return tagName;
  }

  private static class Helper implements XmlProcessor {
    private final XmlType xmlTypeParent;
    private final XmlProcessorElement xmlProcessorElement;

    public Helper(final XmlType xmlTypeParam, final XmlProcessorElement xmlProcessorElementParam) {
      xmlTypeParent = xmlTypeParam;
      xmlProcessorElement = xmlProcessorElementParam;
    }

    @Override
    public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
      xmlProcessorElement.processSubstGroup(xmlParser, xmlTypeParent, attributes);
    }
  }

}
