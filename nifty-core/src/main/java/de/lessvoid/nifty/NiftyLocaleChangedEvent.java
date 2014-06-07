package de.lessvoid.nifty;

import java.util.Locale;

public class NiftyLocaleChangedEvent {
  private final Locale locale;

  public NiftyLocaleChangedEvent(final Locale locale) {
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }
}
