package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.SubstitutionGroup;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class Element {
  private final String tagName;
  private final String tagType;
  private final OccursEnum occurs;

  public Element(
      final String elementNameParam,
      final String elementTypeParam,
      final OccursEnum elementOccursParam) throws Exception {
    if (elementNameParam == null) {
      throw new Exception("elementName can't be null");
    }
    if (elementTypeParam == null) {
      throw new Exception("elementType can't be null");
    }
    if (elementOccursParam == null) {
      throw new Exception("elementOccursParam can't be null");
    }
    tagName = elementNameParam;
    tagType = elementTypeParam;
    occurs = elementOccursParam;
  }

  public void addToProcessor(final Schema schema, final XmlProcessorType processor) throws Exception {
    Type type = schema.getType(tagType);
    if (type == null) {
      throw new Exception("type [" + tagType + "] not found");
    }
    type.addChildren(schema, processor, tagName, tagType, occurs);

    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      typeParent.addChildren(schema, processor, tagName, tagType, occurs);
    }
  }

  public void addToSubstGroup(
      final Schema schema,
      final SubstitutionGroup substitutionGroup,
      final XmlType xmlType) throws Exception {
    Type type = schema.getType(tagType);
    if (type == null) {
      throw new Exception("type [" + tagType + "] not found");
    }
    Type typeParent = type.getTypeParent(schema);
    if (typeParent != null) {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          typeParent.createXmlProcessorFromType(schema, type), tagName, tagType, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    } else {
      XmlProcessorElement xmlProcessorElement = new XmlProcessorElement(
          type.createXmlProcessor(schema), tagName, tagType, occurs);
      substitutionGroup.add(getTagName(), new Helper(xmlType, xmlProcessorElement));
    }
  }

  public String getTagName() {
    return tagName;
  }

  private class Helper implements XmlProcessor {
    private final XmlType xmlTypeParent;
    private final XmlProcessorElement xmlProcessorElement;

    public Helper(final XmlType xmlTypeParam, final XmlProcessorElement xmlProcessorElementParam) {
      xmlTypeParent = xmlTypeParam;
      xmlProcessorElement = xmlProcessorElementParam;
    }

    public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
      xmlProcessorElement.processSubstGroup(xmlParser, xmlTypeParent, attributes);
    }
  }

}
