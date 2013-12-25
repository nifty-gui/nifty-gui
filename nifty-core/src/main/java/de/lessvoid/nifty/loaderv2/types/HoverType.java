package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class HoverType extends XmlBaseType {
  public HoverType(@Nonnull final HoverType hoverType) {
    super(hoverType);
  }

  public HoverType() {
  }

  public HoverType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<hover> " + super.output(offset);
  }

  @Nonnull
  public Falloff materialize() {
    return new Falloff(getAttributes().createProperties());
  }
}
