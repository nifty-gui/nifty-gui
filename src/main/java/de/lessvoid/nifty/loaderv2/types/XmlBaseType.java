package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;

public class XmlBaseType implements XmlType {
  private Attributes attributes;

  public XmlBaseType() {
  }

  public XmlBaseType(final Attributes attributesParam) {
    initFromAttributes(attributesParam);
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public void initFromAttributes(final Attributes attributesParam) {
    attributes = attributesParam;
  }

  public String output(final int offset) {
    return "(" + attributes.toString() + ")";
  }
}
