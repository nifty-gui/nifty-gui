package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.lwxs.XmlType;
import de.lessvoid.xml.xpp3.Attributes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class XmlBaseType implements Cloneable, XmlType {
  @Nonnull
  private Attributes attributes;

  public XmlBaseType() {
    attributes = new Attributes();
  }

  public XmlBaseType(@Nonnull final XmlBaseType src) {
    attributes = new Attributes(src.attributes);
  }

  public XmlBaseType(@Nonnull final Attributes attributesParam) {
    attributes = new Attributes(attributesParam);
  }

  @Nonnull
  @Override
  public XmlBaseType clone() throws CloneNotSupportedException {
    try {
      final XmlBaseType newObject = (XmlBaseType) super.clone();
      newObject.attributes = new Attributes(attributes);
      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning created a object with the wrong type.");
    }
  }

  public void translateSpecialValues(@Nonnull final Nifty nifty, @Nullable final Screen screen) {
    attributes.translateSpecialValues(
        nifty.getResourceBundles(),
        screen == null ? null : screen.getScreenController(),
        nifty.getGlobalProperties(),
        nifty.getLocale());
  }

  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  public void mergeFromAttributes(@Nonnull final Attributes attributesParam) {
    attributes.merge(attributesParam);
  }

  public String output(final int offset) {
    return "(" + attributes.toString() + ")";
  }

  @Override
  public void applyAttributes(@Nonnull final Attributes attributes) {
    this.attributes.overwrite(attributes);
  }

  @Override
  public String toString() {
    return output(0);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    String output = this.output(0);
    hash = 97 * hash + (output != null ? output.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StyleType other = (StyleType) obj;

    String output = this.output(0);
    String otherOutput = other.output(0);

    return output.equals(otherOutput);
  }

}
