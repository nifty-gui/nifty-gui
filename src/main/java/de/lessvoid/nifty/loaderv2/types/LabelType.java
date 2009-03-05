package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class LabelType extends TextType {
  public LabelType() {
    super();
  }

  public LabelType(final Attributes attributes) {
    initFromAttributes(attributes);
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<label> " + super.output(offset);
  }

  public void initFromAttributes(final Attributes attributesParam) {
    super.initFromAttributes(attributesParam);
    if (getAttributes().get("style") == null) {
      getAttributes().set("style", "nifty-label");
    }
  }
}
