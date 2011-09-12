package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

public class XmlProcessorElement {
  private XmlProcessorType elementProcessor;
  private String elementName;
  private OccursEnum elementOccurs;

  public XmlProcessorElement(
      final XmlProcessorType elementProcessorParam,
      final String elementNameParam,
      final String elementTypeParam,
      final OccursEnum elementOccursParam) throws Exception {
    if (elementProcessorParam == null) {
      throw new Exception("elementProcessorParam must not be null");
    }
    if (elementNameParam == null) {
      throw new Exception("elementNameParam must not be null");
    }
    if (elementOccursParam == null) {
      throw new Exception("elementOccursParam must not be null");
    }
    elementProcessor = elementProcessorParam;
    elementName = elementNameParam;
    elementOccurs = elementOccursParam;
  }

  public void process(
      final XmlParser xmlParser,
      final XmlType xmlTypeParent) throws Exception {
    if (elementOccurs.equals(OccursEnum.required)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      xmlParser.required(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.oneOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      xmlParser.oneOrMore(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.optional)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      xmlParser.optional(elementName, elementProcessor);
    } else if (elementOccurs.equals(OccursEnum.zeroOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      xmlParser.zeroOrMore(elementName, elementProcessor);
    }
  }

  public void processSubstGroup(
      final XmlParser xmlParser,
      final XmlType xmlTypeParent,
      final Attributes attributes) throws Exception {
    if (elementOccurs.equals(OccursEnum.required)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.oneOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.optional)) {
      elementProcessor.parentLinkSet(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    } else if (elementOccurs.equals(OccursEnum.zeroOrMore)) {
      elementProcessor.parentLinkAdd(xmlTypeParent, elementName);
      elementProcessor.process(xmlParser, attributes);
    }
  }
}
