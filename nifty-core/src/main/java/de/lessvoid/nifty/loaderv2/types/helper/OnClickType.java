package de.lessvoid.nifty.loaderv2.types.helper;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;

public class OnClickType {
  private String value;

  public OnClickType(final String valueParam) {
    this.value = valueParam;
  }

  public boolean isValid() {
    if (value == null) {
      return false;
    }

    return value.matches("\\w+\\((|\\w+(,\\s*\\w+)*)\\)");
  }

  public NiftyMethodInvoker getMethod(final Nifty nifty, final Object ... controlController) {
    return new NiftyMethodInvoker(nifty, value, controlController);
  }
}
