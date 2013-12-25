package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class UseControlsType extends XmlBaseType {
  private static final Logger log = Logger.getLogger(UseControlsType.class.getName());

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useControls> " + super.output(offset);
  }

  public void loadControl(@Nonnull final NiftyLoader niftyLoader, @Nonnull final NiftyType niftyType) throws Exception {
    final String filename = getAttributes().get("filename");
    if (filename == null) {
      log.severe("Missing filename attribute for control");
    } else {
      niftyLoader.loadControlFile("nifty-controls.nxs", filename, niftyType);
    }
  }
}
