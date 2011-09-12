package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.tools.StringHelper;

public class ResourceBundleType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<resourceBundle> " + super.output(offset);
  }

  public void materialize(final Nifty nifty) {
    nifty.addResourceBundle(getId(), getFilename());
  }

  private String getId() {
    return getAttributes().get("id");
  }

  private String getFilename() {
    return getAttributes().get("filename");
  }
}
