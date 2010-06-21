package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;

public class XmlBaseType implements XmlType {
  private Attributes attributes = new Attributes();

  public XmlBaseType() {
  }

  public XmlBaseType(final XmlBaseType src) {
    if (src.attributes != null) {
      attributes = new Attributes(src.attributes);
    }
  }

  public XmlBaseType(final Attributes attributesParam) {
    initFromAttributes(attributesParam);
  }

  public void translateSpecialValues(final Nifty nifty, final Screen screen) {
    attributes.translateSpecialValues(nifty.getResourceBundles(), screen == null ? null : screen.getScreenController(), nifty.getGlobalProperties());
  }

  public Attributes getAttributes() {
    return attributes;
  }

  public void initFromAttributes(final Attributes attributesParam) {
    attributes = new Attributes(attributesParam);
  }

  public void mergeFromAttributes(final Attributes attributesParam) {
    attributes.merge(attributesParam);
  }

  public String output(final int offset) {
    return "(" + attributes.toString() + ")";
  }
}
