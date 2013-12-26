package de.lessvoid.xml.lwxs.elements;

import de.lessvoid.xml.lwxs.Schema;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class SubstitutionGroup {
  @Nonnull
  private final Collection<Element> elements = new ArrayList<Element>();

  public void addToProcessor(final Schema schema, @Nonnull final XmlProcessorType processor) throws Exception {
    XmlProcessorSubstituitionGroup subst = new XmlProcessorSubstituitionGroup(schema);
    for (Element e : elements) {
      subst.addElement(e);
    }
    processor.addSubstituitionGroup(subst);
  }

  @Nonnull
  public Type getHelperType() {
    return new Type("none", null) {
      @Override
      public void addElement(final Element element) {
        elements.add(element);
      }
    };
  }
}
