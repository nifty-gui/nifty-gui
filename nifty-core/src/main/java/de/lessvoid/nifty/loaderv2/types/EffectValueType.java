package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;

public class EffectValueType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<value> " + super.output(offset);
  }
}
