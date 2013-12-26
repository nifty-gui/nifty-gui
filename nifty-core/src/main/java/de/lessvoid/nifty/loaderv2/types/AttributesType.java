package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class AttributesType extends XmlBaseType {
  public AttributesType() {
  }

  public AttributesType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  public AttributesType(@Nonnull final AttributesType src) {
    super(src);
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<attributes> (" + getAttributes().toString() + ")";
  }

  public void apply(@Nonnull final Attributes result, @Nonnull final String styleId) {
    result.mergeAndTag(getAttributes(), styleId);
  }
}
