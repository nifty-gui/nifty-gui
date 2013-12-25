package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.tools.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResourceBundleType extends XmlBaseType {
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<resourceBundle> " + super.output(offset);
  }

  public void materialize(@Nonnull final Nifty nifty) {
    String id = getId();
    String fileName = getFilename();
    if (id != null && fileName != null) {
      nifty.addResourceBundle(id, fileName);
    }
  }

  @Nullable
  private String getId() {
    return getAttributes().get("id");
  }

  @Nullable
  private String getFilename() {
    return getAttributes().get("filename");
  }
}
