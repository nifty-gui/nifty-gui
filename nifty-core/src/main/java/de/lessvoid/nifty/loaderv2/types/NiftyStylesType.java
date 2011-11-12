package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;

public class NiftyStylesType extends XmlBaseType {
  private Collection < RegisterMouseCursorType > registeredMouseCursor = new ArrayList < RegisterMouseCursorType >();
  private Collection < StyleType > styles = new ArrayList < StyleType >();
  private Collection < UseStylesType > useStyles = new ArrayList < UseStylesType >();

  public void addRegisterMouseCursor(final RegisterMouseCursorType registerMouseCursor) {
    registeredMouseCursor.add(registerMouseCursor);
  }

  public void addStyle(final StyleType newStyle) {
    styles.add(newStyle);
  }

  public void addUseStyles(final UseStylesType newStyle) {
    useStyles.add(newStyle);
  }

  public void loadStyles(final NiftyLoader niftyLoader, final NiftyType niftyType, final Nifty nifty, final Logger log) throws Exception {
    for (RegisterMouseCursorType registerMouseCursorType : registeredMouseCursor) {
      registerMouseCursorType.translateSpecialValues(nifty, null);
      registerMouseCursorType.materialize(nifty, log);
    }
    for (UseStylesType useStyle : useStyles) {
      useStyle.loadStyle(niftyLoader, niftyType, nifty);
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
