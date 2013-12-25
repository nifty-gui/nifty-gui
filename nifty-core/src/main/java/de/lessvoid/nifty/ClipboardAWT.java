package de.lessvoid.nifty;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AWT based clipboard implementation.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ClipboardAWT implements Clipboard {
  /**
   * The logger of this class.
   */
  private static final Logger log = Logger.getLogger(ClipboardAWT.class.getName());

  /**
   * Put data into the system clipboard.
   *
   * @param data the data
   */
  @Override
  public void put(@Nullable final String data) {
    java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(new StringSelection(data), null);
  }

  /**
   * Get string data back from the clipboard.
   *
   * @return string data from clipboard
   */
  @Override
  @Nullable
  public String get() {
    java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
      try {
        return contents.getTransferData(DataFlavor.stringFlavor).toString();
      } catch (UnsupportedFlavorException ex) {
        log.log(Level.WARNING, "DataFlavor not supported!", ex);
      } catch (IOException ex) {
        log.log(Level.INFO, "Conversation", ex);
      }
    }
    return null;
  }
}
