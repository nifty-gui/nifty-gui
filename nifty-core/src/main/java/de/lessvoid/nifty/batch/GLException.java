package de.lessvoid.nifty.batch;

/**
 * This is the exception thrown by classes that make OpenGL calls, in case something goes wrong badly.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class GLException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new exception with {@code null} as its detail message.  The cause is not initialized, and may
   * subsequently be initialized by a call to {@link #initCause}.
   */
  public GLException() {
    super();
  }

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
   * be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
   *                method.
   */
  public GLException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   * <p/>
   * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
   * exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
   *                value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public GLException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new exception with the specified cause and a detail message of
   * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
   * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers for other
   * throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
   *              value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public GLException(Throwable cause) {
    super(cause);
  }
}
