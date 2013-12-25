package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class EffectValueType extends XmlBaseType implements Cloneable {
  public EffectValueType() {
  }

  public EffectValueType(@Nonnull final EffectValueType e) {
    super(e);
  }

  public EffectValueType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  @Override
  @Nonnull
  public EffectValueType clone() throws CloneNotSupportedException {
    try {
      return (EffectValueType) super.clone();
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning returned illegal class type.");
    }
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<value> " + super.output(offset);
  }
}
