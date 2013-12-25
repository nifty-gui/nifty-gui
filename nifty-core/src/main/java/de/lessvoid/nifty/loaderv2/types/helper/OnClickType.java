package de.lessvoid.nifty.loaderv2.types.helper;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class OnClickType {
  /**
   * The pattern used to check
   */
  private static final Pattern VALID_PATTERN = Pattern.compile("\\w+\\((?:|\\w+(?:,\\s*\\w+)*)\\)");
  private final String value;

  public OnClickType(final String valueParam) {
    this.value = valueParam;
  }

  public boolean isValid() {
    if (value == null) {
      return false;
    }

    return VALID_PATTERN.matcher(value).matches();
  }

  @Nonnull
  public NiftyMethodInvoker getMethod(final Nifty nifty, final Object... controlController) {
    return new NiftyMethodInvoker(nifty, value, controlController);
  }
}
