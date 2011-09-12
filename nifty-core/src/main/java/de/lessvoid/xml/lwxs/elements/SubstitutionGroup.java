package de.lessvoid.xml.lwxs.elements;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.xml.lwxs.Schema;

public class SubstitutionGroup {
  private Collection < Element > elements = new ArrayList < Element >();

  public void addToProcessor(final Schema schema, final XmlProcessorType processor) throws Exception {
    XmlProcessorSubstituitionGroup subst = new XmlProcessorSubstituitionGroup(schema);
    for (Element e : elements) {
      subst.addElement(e);
    }
    processor.addSubstituitionGroup(subst);
  }

  public Type getHelperType() {
    Type type = new Type() {
      public void addElement(final Element element) {
        elements.add(element);
      }
    };
    return type;
  }
}
