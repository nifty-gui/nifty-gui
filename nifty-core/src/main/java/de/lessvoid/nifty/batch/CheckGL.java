package de.lessvoid.nifty.batch;

import de.lessvoid.nifty.batch.spi.GL;

import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * Helper method to check for GL errors. This will call glGetError() and as long as the call returns not GL_NO_ERROR it
 * will log the error and the stack trace of the caller using jdk14 logging.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CheckGL {
  private static final Logger log = Logger.getLogger(CheckGL.class.getName());

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   */
  public static void checkGLError(@Nonnull final GL gl) {
    checkGLError(gl, "");
  }

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   *
   * @param message a message to log
   *                (can be used to log additional information for instance what call was executed before)
   */
  public static void checkGLError(@Nonnull final GL gl, @Nonnull final String message) {
    checkGLError(gl, message, false);
  }

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   *
   * @param message        a message to log
   *                       (can be used to log additional information for instance what call was executed before)
   * @param throwException in case this value is set {@code true} and OpenGL reports a error a exception will be thrown
   * @throws GLException in case the {@code throwException} is set {@code true} and OpenGL reports an error
   */
  public static void checkGLError(
          @Nonnull final GL gl,
          @Nonnull final String message,
          final boolean throwException) {
    int error = gl.glGetError();
    boolean hasError = false;
    while (error != gl.GL_NO_ERROR()) {
      hasError = true;
      String glerrmsg = getGlErrorMessage(gl, error);
      StringBuilder stacktrace = new StringBuilder();
      for (StackTraceElement strackTraceElement : Thread.currentThread().getStackTrace()) {
        stacktrace.append(strackTraceElement.toString());
        stacktrace.append("\n");
      }
      log.warning("OpenGL Error: (" + error + ") " + glerrmsg + " {" + message + "} " + stacktrace.toString());
      error = gl.glGetError();
    }
    if (hasError && throwException) {
      throw new GLException("OpenGL Error occurred:" + message);
    }
  }

  @Nonnull
  private static String getGlErrorMessage(final GL gl, final int error) {
    if (error == gl.GL_INVALID_ENUM()) {
      return "Invalid enum";
    } else if (error == gl.GL_INVALID_VALUE()) {
      return "Invalid value";
    } else if (error == gl.GL_INVALID_OPERATION()) {
      return "Invalid operation";
    } else if (error == gl.GL_STACK_OVERFLOW()) {
      return "Stack overflow";
    } else if (error == gl.GL_STACK_UNDERFLOW()) {
      return "Stack underflow";
    } else if (error == gl.GL_OUT_OF_MEMORY()) {
      return "Out of memory";
    } else {
      return "";
    }
  }
}
