package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;



/**
 * A Vertex Array Object (VAO).
 * @author void
 */
public class CoreVAO {
  private int vao;

  /**
   * Create a new VAO. This calls glGenVertexArrays.
   */
  public CoreVAO() {
    init();
  }

  /**
   * Bind this VAO to make it the current VAO.
   */
  public void bind() {
    final GL gl = GLContext.getCurrentGL();
    if (gl.isGL3()) {
      gl.getGL3().glBindVertexArray(vao);
      CoreCheckGL.checkGLError("glBindVertexArray");
    }
  }

  /**
   * Unbinds this VAO which makes the VAO with id 0 the active one.
   */
  public void unbind() {
    final GL gl = GLContext.getCurrentGL();
    if (gl.isGL3()) {
      gl.getGL3().glBindVertexArray(0);
      CoreCheckGL.checkGLError("glBindVertexArray(0)");
    }
  }

  /**
   * Delete all resources for this VAO.
   */
  public void delete() {
    final GL gl = GLContext.getCurrentGL();
    if (gl.isGL3()) {
      int[] buffer = new int[1];
      buffer[0] = vao;
      gl.getGL3().glDeleteVertexArrays(1, buffer, 0);
    }
  }

  /**
   * Configure the vertex attribute with the given data. The type of the data will be
   * GL_FLOAT.
   *
   * @param index the index of the vertex attribute to modify
   * @param size the size of the data for this vertex attribute
   *        (the number of GL_FLOAT to use)
   * @param stride the stride between the data
   * @param offset the offset of the data
   */
  public void enableVertexAttributef(final int index, final int size, final int stride, final int offset) {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL2ES2().glVertexAttribPointer(index, size, GL.GL_FLOAT, false, stride * 4, offset * 4);
    gl.getGL2ES2().glEnableVertexAttribArray(index);
    CoreCheckGL.checkGLError("glVertexAttribPointer (" + index + ")");
  }

  private void init() {
    final GL gl = GLContext.getCurrentGL();
    if (gl.isGL3()) {
      int[] buffer = new int[1];
      gl.getGL3().glGenVertexArrays(1, buffer, 0);
      vao = buffer[0];
      CoreCheckGL.checkGLError("glGenVertexArrays");
    }
  }
}
