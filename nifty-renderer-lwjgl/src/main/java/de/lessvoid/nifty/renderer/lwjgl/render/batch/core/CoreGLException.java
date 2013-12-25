package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;

/**
 * This is the exception thrown by the classes of the CoreGL library in case something goes wrong badly.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class CoreGLException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new CoreGL exception with {@code null} as its detail message.  The cause is not initialized, and may
   * subsequently be initialized by a call to {@link #initCause}.
   */
  public CoreGLException() {
    super();
  }

  /**
   * Constructs a new CoreGL exception with the specified detail message. The cause is not initialized, and may
   * subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
   *                method.
   */
  public CoreGLException(String message) {
    super(message);
  }

  /**
   * Constructs a new CoreGL exception with the specified detail message and cause.
   * <p/>
   * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this CoreGL
   * exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
   *                value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public CoreGLException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new CoreGL exception with the specified cause and a detail message of
   * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
   * <tt>cause</tt>). This constructor is useful for CoreGL exceptions that are little more than wrappers for other
   * throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
   *              value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public CoreGLException(Throwable cause) {
    super(cause);
  }
}
