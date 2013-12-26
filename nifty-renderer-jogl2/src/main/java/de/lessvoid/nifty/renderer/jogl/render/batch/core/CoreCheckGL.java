package de.lessvoid.nifty.renderer.jogl.render.batch.core;


import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import java.util.logging.Logger;

/**
 * Helper method to check for GL errors. This will call glGetError() and as long as the call returns not GL_NO_ERROR it
 * will log the error and the stacktrace of the caller using jdk14 logging.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class CoreCheckGL {
  /**
   * The logger of this class.
   */
  private static final Logger log = Logger.getLogger(CoreCheckGL.class.getName());

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   */
  public static void checkGLError() {
    checkGLError("");
  }

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   *
   * @param message a message to log
   *                (can be used to log additional information for instance what call was executed before)
   */
  public static void checkGLError(final String message) {
    checkGLError(message, false);
  }

  /**
   * Check for GL error and log any errors found. You should probably call this once a frame.
   *
   * @param message        a message to log
   *                       (can be used to log additional information for instance what call was executed before)
   * @param throwException in case this value is set {@code true} and OpenGL reports a error a exception will be thrown
   * @throws CoreGLException in case the {@code throwException} is set {@code true} and OpenGL reports an error
   */
  public static void checkGLError(final String message, final boolean throwException) {
    final GL gl = GLContext.getCurrentGL();
    int error = gl.glGetError();
    boolean hasError = false;
    while (error != GL.GL_NO_ERROR) {
      hasError = true;
      StringBuilder stacktrace = new StringBuilder();
      for (StackTraceElement strackTraceElement : Thread.currentThread().getStackTrace()) {
        stacktrace.append(strackTraceElement.toString());
        stacktrace.append("\n");
      }
      log.warning("OpenGL Error: (" + error + ") " + " {" + message + "} " + stacktrace.toString());
      error = gl.glGetError();
    }

    if (hasError && throwException) {
      throw new CoreGLException("OpenGL Error occurred:" + message);
    }
  }
}
