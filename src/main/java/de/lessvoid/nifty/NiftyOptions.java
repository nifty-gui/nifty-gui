package de.lessvoid.nifty;

/**
 * NiftyOptions enum.
 * @author void
 */
public enum NiftyOptions {

  /**
   * Show the console.
   */
  ShowConsole("showConsole", "false");

  /**
   * key.
   */
  private final String key;

  /**
   * defaultValue.
   */
  private final String defaultValue;

  /**
   * create it from key and default.
   * @param propertyKey key
   * @param newDefaultValue default
   */
  NiftyOptions(final String propertyKey, final String newDefaultValue) {
    this.key = propertyKey;
    this.defaultValue = newDefaultValue;
  }

  /**
   * get key.
   * @return key
   */
  public String getKey() {
    return key;
  }

  /**
   * get default.
   * @return default value
   */
  public String getDefaultValue() {
    return defaultValue;
  }

}
