package de.lessvoid.nifty.slick2d.input;

/**
 * This enumerator stores the forwarding modes the input systems are supporting.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public enum ForwardingMode {
  /**
   * Normal mode. Relay all events first to the Nifty-GUI and in case they are not handled by the GUI, forward them to
   * the forwarding listener.
   */
  none,

  /**
   * Forward all mouse events directly to the second listeners and do not send them to the Nifty-GUI.
   */
  mouse,

  /**
   * Forward all keyboard events directly to the second listeners and do not send them to the Nifty-GUI.
   */
  keyboard,

  /**
   * Forward all input events (mouse and keyboard) directly to the second listeners and do not send them to the
   * Nifty-GUI.
   */
  all
}
