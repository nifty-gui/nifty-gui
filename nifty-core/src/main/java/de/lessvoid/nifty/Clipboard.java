package de.lessvoid.nifty;

/**
 * Clipboard interface.
 * @author void
 */
public interface Clipboard {

  /**
   * Put data into clipboard.
   * @param data data
   */
  void put(String data);

  /**
   * Get data back from clipboard.
   * @return data from clipboard
   */
  String get();
}
