package de.lessvoid.nifty.slick2d.render.cursor;

import de.lessvoid.nifty.slick2d.loaders.SlickLoadException;

/**
 * This exception is expected to be thrown be the constructor of this class in
 * case loading the specified mouse cursor failed.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickLoadCursorException extends SlickLoadException {
  /**
   * The serialization UID.
   */
  private static final long serialVersionUID = 4068721034678365719L;

  /**
   * Create the exception without an attached message or parent Throwable.
   */
  public SlickLoadCursorException() {
    super();
  };

  /**
   * Create the exception with an attached message and without parent Throwable.
   */
  public SlickLoadCursorException(final String msg) {
    super(msg);
  };

  /**
   * Create the exception with an attached message and parent Throwable.
   */
  public SlickLoadCursorException(final String msg, final Throwable e) {
    super(msg, e);
  };

  /**
   * Create the exception without an attached message and with parent Throwable.
   */
  public SlickLoadCursorException(final Throwable e) {
    super(e);
  };
}