package de.lessvoid.nifty;

import javax.annotation.Nullable;

/**
 * This implementation of the clipboard is unable to interact with the rest of the system. How ever its able to store
 * a single data object for internal use. So coping and pasting inside the Nifty-GUI is possible.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ClipboardInternal implements Clipboard {
  /**
   * The internal stored data.
   */
  @Nullable
  private String data;

  @Override
  public void put(@Nullable final String data) {
    this.data = data;
  }

  @Nullable
  @Override
  public String get() {
    return data;
  }

}
