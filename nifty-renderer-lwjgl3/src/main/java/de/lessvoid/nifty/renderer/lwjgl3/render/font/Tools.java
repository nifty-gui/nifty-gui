/*
 * Created on 20.02.2005
 */
package de.lessvoid.nifty.renderer.lwjgl3.render.font;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

/**
 * @author void
 */
public class Tools {
  private static final Logger log = Logger.getLogger(Tools.class.getName());

  /**
   * @param fkt
   */
  public static void checkGLError(String fkt) {
    // return;

    int error = GL11.glGetError();
    if (error != GL11.GL_NO_ERROR) {
      String glerrmsg = GLUtil.getErrorString(error);
      log.warning("OpenGL Error: (" + error + ") " + glerrmsg + ", " + fkt);
    }

  }

  public static ByteBuffer toByteString(@Nonnull String str, boolean isNullTerminated) {
    int length = str.length();
    if (isNullTerminated) {
      length++;
    }
    ByteBuffer buff = BufferUtils.createByteBuffer(length);
    buff.put(str.getBytes());

    if (isNullTerminated) {
      buff.put((byte) 0);
    }

    buff.flip();
    return buff;
  }
}
