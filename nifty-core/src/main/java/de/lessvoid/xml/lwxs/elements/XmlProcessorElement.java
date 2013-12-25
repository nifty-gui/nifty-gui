package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;

import javax.annotation.Nonnull;

public class XmlProcessorElement {
  @Nonnull
  private XmlProcessorType elementProcessor;
  @Nonnull
  private String elementName;
  @Nonnull
  private OccursEnum elementOccurs;

  public XmlProcessorElement(
      @Nonnull final XmlProcessorType elementProcessorParam,
      @Nonnull final String elementNameParam,
      @Nonnull final OccursEnum elementOccursParam) {
    elementProcessor = elementProcessorParam;
    elementName = elementNameParam;
    elementOccurs = elementOccursParam;
  }

  public void process(
      @Nonnull final XmlParser xmlParser,
      @Nonnull final XmlType xmlTypeParent) throws Exception {
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
      @Nonnull final XmlParser xmlParser,
      @Nonnull final XmlType xmlTypeParent,
      @Nonnull final Attributes attributes) throws Exception {
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
