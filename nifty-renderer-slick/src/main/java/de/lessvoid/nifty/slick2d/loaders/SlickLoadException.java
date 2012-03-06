package de.lessvoid.nifty.slick2d.loaders;

/**
 * This exception is expected to be thrown be the constructor of this class in case loading the specified resource
 * failed.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class SlickLoadException extends Exception {
  /**
   * The serialization UID.
   */
  private static final long serialVersionUID = 4068721034678365719L;

  /**
   * Create the exception without an attached message or parent Throwable.
   */
  public SlickLoadException() {
  }

  /**
   * Create the exception with an attached message and without parent Throwable.
   *
   * @param msg The message that is attached to this exception
   */
  public SlickLoadException(final String msg) {
    super(msg);
  }

  /**
   * Create the exception with an attached message and parent Throwable.
   *
   * @param msg The message that is attached to this exception
   * @param e The throwable that caused this exception
   */
  public SlickLoadException(final String msg, final Throwable e) {
    super(msg, e);
  }

  /**
   * Create the exception without an attached message and with parent Throwable.
   *
   * @param e The throwable that caused this exception
   */
  public SlickLoadException(final Throwable e) {
    super(e);
  }
}