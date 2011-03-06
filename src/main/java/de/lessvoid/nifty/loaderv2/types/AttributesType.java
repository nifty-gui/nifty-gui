package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class AttributesType extends XmlBaseType {
  public AttributesType() {
  }

  public AttributesType(final Attributes attributes) {
    super(attributes);
  }

  public AttributesType(final AttributesType src) {
    super(src);
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<attributes> (" + getAttributes().toString() + ")";
  }

  public void apply(final Attributes result, final String styleId) {
    result.mergeAndTag(getAttributes(), styleId);
  }
}
