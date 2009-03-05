package de.lessvoid.nifty.loaderv2.types.helper;

import de.lessvoid.xml.tools.MethodInvoker;

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

  public MethodInvoker getMethod(final Object ... controlController) {
    return new MethodInvoker(value, controlController);
  }
}
