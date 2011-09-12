package de.lessvoid.xml.lwxs.elements;

import org.junit.Test;

import de.lessvoid.xml.lwxs.elements.Element;

public class ElementTestWithNull {
  @Test(expected = Exception.class)
  public void withNullName() throws Exception {
    new Element(null, null, null);
  }

  @Test(expected = Exception.class)
  public void withNullType() throws Exception {
    new Element("name", null, null);
  }

  @Test(expected = Exception.class)
  public void withNullOccures() throws Exception {
    new Element("name", "type", null);
  }
}
