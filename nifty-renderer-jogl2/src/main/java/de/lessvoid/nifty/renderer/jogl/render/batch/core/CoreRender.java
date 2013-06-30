package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

/**
 * Simple helper methods to render vertex arrays.
 * @author void
 */
public class CoreRender {

  /**
   * Render the currently active VAO using triangles with the given
   * number of vertices.
   *
   * @param count number of vertices to render as triangle strips
   */
  public static void renderTrianglesIndexed(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawElements(GL.GL_TRIANGLES, count, GL.GL_UNSIGNED_INT, 0);
    CoreCheckGL.checkGLError("glDrawElements");
  }
}
