package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.SubstitutionGroup;

public class XmlProcessorSubstituitionGroup {
  private Collection < Element > elements = new ArrayList < Element >();
  private SubstitutionGroup substitutionGroup = new SubstitutionGroup();
  private Schema schema;

  public XmlProcessorSubstituitionGroup(final Schema schemaParam) {
    schema = schemaParam;
  }

  public void addElement(final Element e) throws Exception {
    elements.add(e);
  }

  public SubstitutionGroup getSubstGroup(final XmlType xmlType) throws Exception {
    for (Element e : elements) {
      e.addToSubstGroup(schema, substitutionGroup, xmlType);
    }
    return substitutionGroup;
  }
}
