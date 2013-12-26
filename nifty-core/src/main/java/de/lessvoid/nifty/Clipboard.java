package de.lessvoid.nifty;

import javax.annotation.Nullable;

/**
 * Clipboard interface.
 * @author void
 */
public interface Clipboard {

  /**
   * Put data into clipboard.
   * @param data the data for the clipboard or {@link null} to clear the clipboard
   */
  void put(@Nullable String data);

  /**
   * Get data back from clipboard.
   * @return data from clipboard
   */
  @Nullable
  String get();
}
