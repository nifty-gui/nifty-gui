package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.xml.lwxs.elements.SubstitutionGroup;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

import javax.annotation.Nonnull;

public class TypeProcessorSubstitutionGroup implements XmlProcessor {
  private final Type parent;

  public TypeProcessorSubstitutionGroup(final Type parentParam) {
    parent = parentParam;
  }

  @Override
  public void process(@Nonnull final XmlParser xmlParser, @Nonnull final Attributes attributes) throws Exception {
    SubstitutionGroup substitutionGroup = new SubstitutionGroup();
    parent.addSubstitutionGroup(substitutionGroup);

    xmlParser.nextTag();
    xmlParser.oneOrMore("element", new TypeProcessorElement(substitutionGroup.getHelperType()));
  }
}
