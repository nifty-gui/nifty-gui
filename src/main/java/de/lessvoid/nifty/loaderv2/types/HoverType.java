package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.tools.StringHelper;

public class HoverType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<hover> " + super.output(offset);
  }

  public Falloff materialize() {
    return new Falloff(getAttributes().createProperties());
  }
}
