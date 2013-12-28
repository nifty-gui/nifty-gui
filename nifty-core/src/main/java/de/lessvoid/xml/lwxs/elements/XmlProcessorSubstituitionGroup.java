package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.Schema;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.SubstitutionGroup;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class XmlProcessorSubstituitionGroup {
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();
  @Nonnull
  private final SubstitutionGroup substitutionGroup = new SubstitutionGroup();
  private final Schema schema;

  public XmlProcessorSubstituitionGroup(final Schema schemaParam) {
    schema = schemaParam;
  }

  public void addElement(final Element e) throws Exception {
    elements.add(e);
  }

  @Nonnull
  public SubstitutionGroup getSubstGroup(@Nonnull final XmlType xmlType) throws Exception {
    for (Element e : elements) {
      e.addToSubstGroup(schema, substitutionGroup, xmlType);
    }
    return substitutionGroup;
  }
}
