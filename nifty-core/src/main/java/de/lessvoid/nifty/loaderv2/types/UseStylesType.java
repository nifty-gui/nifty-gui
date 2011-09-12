package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

public class UseStylesType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useStyle> " + super.output(offset);
  }

  public void loadStyle(final NiftyLoader niftyLoader, final NiftyType niftyType, final Nifty nifty) throws Exception {
    niftyLoader.loadStyleFile("nifty-styles.nxs", getAttributes().get("filename"), niftyType, nifty);
  }
}
