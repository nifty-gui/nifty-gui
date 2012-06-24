package de.lessvoid.xml.lwxs.processor;

import de.lessvoid.xml.lwxs.elements.SubstitutionGroup;
import de.lessvoid.xml.lwxs.elements.Type;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.XmlParser;
import de.lessvoid.xml.xpp3.XmlProcessor;

public class TypeProcessorSubstitutionGroup implements XmlProcessor {
  private final Type parent;

  public TypeProcessorSubstitutionGroup(final Type parentParam) {
    parent = parentParam;
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    SubstitutionGroup substitutionGroup = new SubstitutionGroup();
    parent.addSubstitutionGroup(substitutionGroup);

    xmlParser.nextTag();
    xmlParser.oneOrMore("element", new TypeProcessorElement(substitutionGroup.getHelperType()));
  }
}
