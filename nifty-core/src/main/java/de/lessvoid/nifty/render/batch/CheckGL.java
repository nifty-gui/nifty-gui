package de.lessvoid.nifty.render.batch;

import de.lessvoid.nifty.render.batch.spi.GL;

import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * Helper class to check for OpenGL errors.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CheckGL {
  @Nonnull
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

  /**
   * Checks if the texture size is within the capabilities of OpenGL.
   *
   * @throws GLException In case the texture dimensions are too large or negative.
   */
  public static void checkGLTextureSize(@Nonnull final GL gl, final int textureWidth, final int textureHeight) {
    final int maxSize = getMaxTextureSize(gl);

    if ((textureWidth > maxSize) || (textureHeight > maxSize)) {
      throw new GLException("Attempt to allocate a texture to big for the current hardware");
    }
    if (textureWidth < 0) {
      throw new GLException("Attempt to allocate a texture with a width value below 0.");
    }
    if (textureHeight < 0) {
      throw new GLException("Attempt to allocate a texture with a height value below 0.");
    }
  }

  // Internal implementations

  private static int getMaxTextureSize(@Nonnull final GL gl) {
    int[] params = new int[1];
    gl.glGetIntegerv(gl.GL_MAX_TEXTURE_SIZE(), params, 0);
    int maxTextureSize = params[0];
    checkGLError(gl,"glGetInteger", true);
    return maxTextureSize;
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
