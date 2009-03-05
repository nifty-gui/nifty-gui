package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

public class NiftyStylesType extends XmlBaseType {
  private Collection < StyleType > styles = new ArrayList < StyleType >();
  private Collection < UseStylesType > useStyles = new ArrayList < UseStylesType >();

  public void addStyle(final StyleType newStyle) {
    styles.add(newStyle);
  }

  public void addUseStyles(final UseStylesType newStyle) {
    useStyles.add(newStyle);
  }

  public void loadStyles(final NiftyLoader niftyLoader, final NiftyType niftyType) throws Exception {
    for (UseStylesType useStyle : useStyles) {
      useStyle.loadStyle(niftyLoader, niftyType);
    }
    for (StyleType style : styles) {
      niftyType.addStyle(style);
    }
  }

  public String output() {
    int offset = 1;
    return
      "\nNifty Data:\n" + CollectionLogger.out(offset, styles, "styles")
      + "\n" + CollectionLogger.out(offset, useStyles, "useStyles");
  }
}
