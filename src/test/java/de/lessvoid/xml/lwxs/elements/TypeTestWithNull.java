package de.lessvoid.xml.lwxs.elements;

import org.junit.Test;

import de.lessvoid.xml.lwxs.elements.Type;

public class TypeTestWithNull {
  @Test(expected = Exception.class)
  public void withNull() throws Exception {
    new Type(null, null);
  }
}
