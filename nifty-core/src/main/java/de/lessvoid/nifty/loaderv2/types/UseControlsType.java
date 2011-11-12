package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.tools.StringHelper;

public class UseControlsType extends XmlBaseType {
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<useControls> " + super.output(offset);
  }

  public void loadControl(final NiftyLoader niftyLoader, final NiftyType niftyType) throws Exception {
    niftyLoader.loadControlFile("nifty-controls.nxs", getAttributes().get("filename"), niftyType);
  }
}
