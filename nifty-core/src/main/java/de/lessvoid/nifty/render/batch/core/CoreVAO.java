package de.lessvoid.nifty.render.batch.core;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * Represents an abstraction of a VAO (Vertex Array Object).
 *
 * Note: Requires OpenGL 3.2 or greater.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreVAO {
  @Nonnull
  private final CoreGL gl;
  @Nonnull
  private final IntBuffer vertexArrayBuffer;
  private int vao;

  /**
   * Creates a new VAO. This calls glGenVertexArrays.
   */
  public CoreVAO(@Nonnull final CoreGL gl, @Nonnull final BufferFactory bufferFactory) {
    this.gl = gl;
    this.vertexArrayBuffer = bufferFactory.createNativeOrderedIntBuffer(1);
    init();
  }

  /**
   * Binds this VAO to make it the current VAO.
   */
  public void bind() {
    gl.glBindVertexArray(vao);
    CheckGL.checkGLError(gl, "glBindVertexArray");
  }

  /**
   * Unbinds this VAO which makes the VAO with id 0 the active one.
   */
  public void unbind() {
    gl.glBindVertexArray(0);
    CheckGL.checkGLError(gl, "glBindVertexArray(0)");
  }

  /**
   * Deletes all vertex data associated with this VBO.
   */
  public void delete() {
    vertexArrayBuffer.clear();
    vertexArrayBuffer.put(0, vao);
    gl.glDeleteVertexArrays(1, vertexArrayBuffer);
  }

  /**
   * Configures the vertex attribute with the specified data. The type of the data will be GL_FLOAT.
   *
   * @param index  The index of the vertex attribute to modify.
   * @param size   The size of the data for this vertex attribute (the number of GL_FLOAT's to use).
   * @param stride The stride between the data.
   * @param offset The offset of the data.
   */
  public void enableVertexAttributef(final int index, final int size, final int stride, final int offset) {
    gl.glVertexAttribPointer(index, size, gl.GL_FLOAT(), false, stride * 4, offset * 4);
    gl.glEnableVertexAttribArray(index);
    CheckGL.checkGLError(gl, "glVertexAttribPointer (" + index + ")");
  }

  private void init() {
    vertexArrayBuffer.clear();
    gl.glGenVertexArrays(1, vertexArrayBuffer);
    vao = vertexArrayBuffer.get(0);
    CheckGL.checkGLError(gl, "glGenVertexArrays");
  }
}
