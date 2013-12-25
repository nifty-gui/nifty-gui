package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.tools.MethodInvoker;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlProcessorType implements XmlProcessor {
  @Nonnull
  private static final Logger log = Logger.getLogger(XmlProcessorType.class.getName());
  @Nonnull
  private final String fullClassName;
  @Nonnull
  private final Collection < XmlProcessorElement > elements = new ArrayList < XmlProcessorElement >();
  @Nonnull
  private final Collection < XmlProcessorSubstituitionGroup > substGroups = new ArrayList < XmlProcessorSubstituitionGroup >();
  @Nullable
  private XmlType xmlTypeParentSingle;
  @Nullable
  private XmlType xmlTypeParentMultiple;
  @Nullable
  private String xmlTypeParentName;
  @Nullable
  private XmlType xmlType;

  public XmlProcessorType(@Nonnull final String fullClassNameParam) {
    fullClassName = fullClassNameParam;
  }

  public void addElementProcessor(@Nonnull final XmlProcessorElement element) {
    elements.add(element);
  }

  public void addSubstituitionGroup(@Nonnull final XmlProcessorSubstituitionGroup element) {
    substGroups.add(element);
  }

  @Override
  public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
    xmlType = ClassHelper.getInstance(fullClassName, XmlType.class);
    if (xmlType == null) {
      log.log(Level.SEVERE, "Failed to process XML. Requested class " + fullClassName + " failed to locate.");
    } else {
      xmlType.applyAttributes(attributes);
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
  }

  @Nullable
  public XmlType getXmlType() {
    return xmlType;
  }

  private void invoke(@Nonnull final XmlType child, @Nonnull final XmlType parent, @Nonnull final String qualifier) {
    MethodInvoker methodInvoker = new MethodInvoker(qualifier + xmlTypeParentName + "()", parent);
    methodInvoker.invoke(child);
  }

  public void parentLinkSet(@Nonnull final XmlType xmlTypeParent, @Nonnull final String elementName) {
    xmlTypeParentSingle = xmlTypeParent;
    xmlTypeParentMultiple = null;
    xmlTypeParentName = elementName;
  }

  public void parentLinkAdd(@Nonnull final XmlType xmlTypeParent, @Nonnull final String elementName) {
    xmlTypeParentSingle = null;
    xmlTypeParentMultiple = xmlTypeParent;
    xmlTypeParentName = elementName;
  }
}
