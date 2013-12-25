package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

import javax.annotation.Nonnull;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UseStylesType extends XmlBaseType {
  @Nonnull
  private static final Logger log = Logger.getLogger(UseStylesType.class.getName());

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useStyle> " + super.output(offset);
  }

  public void loadStyle(
      @Nonnull final NiftyLoader niftyLoader,
      @Nonnull final NiftyType niftyType,
      @Nonnull final Nifty nifty) throws Exception {
    final String filename = getAttributes().get("filename");
    if (filename == null) {
      log.log(Level.SEVERE, "Missing filename attribute for style!");
    } else {
      niftyLoader.loadStyleFile("nifty-styles.nxs", filename, niftyType, nifty);
    }
  }
}
