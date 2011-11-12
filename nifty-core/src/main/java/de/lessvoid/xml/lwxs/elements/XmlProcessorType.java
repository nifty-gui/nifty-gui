package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.tools.MethodInvoker;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class XmlProcessorType implements XmlProcessor {
  private String fullClassName;
  private Collection < XmlProcessorElement > elements = new ArrayList < XmlProcessorElement >();
  private Collection < XmlProcessorSubstituitionGroup > substGroups = new ArrayList < XmlProcessorSubstituitionGroup >();
  private XmlType xmlTypeParentSingle;
  private XmlType xmlTypeParentMultiple;
  private String xmlTypeParentName;
  private XmlType xmlType;

  public XmlProcessorType(final String fullClassNameParam) {
    fullClassName = fullClassNameParam;
  }

  public void addElementProcessor(final XmlProcessorElement element) {
    elements.add(element);
  }

  public void addSubstituitionGroup(final XmlProcessorSubstituitionGroup element) {
    substGroups.add(element);
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    xmlType = ClassHelper.getInstance(fullClassName, XmlType.class);
    xmlType.initFromAttributes(attributes);

    if (xmlTypeParentSingle != null) {
      invoke(xmlType, xmlTypeParentSingle, "set");
    } else if (xmlTypeParentMultiple != null) {
      invoke(xmlType, xmlTypeParentMultiple, "add");
    }

    xmlParser.nextTag();
    for (XmlProcessorElement child : elements) {
      child.process(xmlParser, xmlType);
    }
    for (XmlProcessorSubstituitionGroup subst : substGroups) {
      xmlParser.zeroOrMore(subst.getSubstGroup(xmlType));
    }
  }

  public XmlType getXmlType() {
    return xmlType;
  }

  private void invoke(final XmlType child, final XmlType parent, final String qualifier) {
    MethodInvoker methodInvoker = new MethodInvoker(qualifier + xmlTypeParentName + "()", parent);
    methodInvoker.invoke(child);
  }

  public void parentLinkSet(final XmlType xmlTypeParent, final String elementName) {
    xmlTypeParentSingle = xmlTypeParent;
    xmlTypeParentMultiple = null;
    xmlTypeParentName = elementName;
  }

  public void parentLinkAdd(final XmlType xmlTypeParent, final String elementName) {
    xmlTypeParentSingle = null;
    xmlTypeParentMultiple = xmlTypeParent;
    xmlTypeParentName = elementName;
  }
}
