package de.lessvoid.nifty.renderer.jogl.render.batch.core;


import com.jogamp.common.nio.Buffers;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;
import java.nio.IntBuffer;

/**
 * The CoreElementVBO class represents a VBO bound to GL_ELEMENT_BUFFER.
 *
 * @author void
 */
public class CoreElementVBO {
  @Nonnull
  private final int[] id = new int[1];
  private final int usage;
  private final IntBuffer indexBuffer;

  /**
   * This works exactly as createStaticVBO() but will use GL_STREAM_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreElementVBO createStream(@Nonnull final int[] data) {
    return new CoreElementVBO(GL3.GL_STREAM_DRAW, data);
  }

  /**
   * Allows access to the internally kept nio FloatBuffer that contains the original
   * buffer data. You can access and change this buffer if you want to update the
   * buffer content. Just make sure that you call rewind() before sending your new
   * data to the GPU with the sendData() method.
   *
   * @return the FloatBuffer with the original buffer data (stored in main memory
   * not GPU memory)
   */
  public IntBuffer getBuffer() {
    return indexBuffer;
  }

  /**
   * bind the buffer object as GL_ARRAY_BUFFER
   */
  public void bind() {
    final GL gl = GLContext.getCurrentGL();
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id[0]);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ELEMENT_ARRAY_BUFFER)");
  }

  public void unbind() {
    final GL gl = GLContext.getCurrentGL();
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ELEMENT_ARRAY_BUFFER -> unbind)");
  }

  /**
   * Send the content of the FloatBuffer to the GPU.
   */
  public void send() {
    final GL gl = GLContext.getCurrentGL();
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.limit() * 4, indexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData(GL_ELEMENT_ARRAY_BUFFER)");
  }

  /**
   * Delete all resources for this VBO.
   */
  public void delete() {
    final GL gl = GLContext.getCurrentGL();
    gl.glDeleteBuffers(1, id, 0);
  }

  private CoreElementVBO(final int usageType, @Nonnull final int[] data) {
    final GL gl = GLContext.getCurrentGL();
    usage = usageType;

    indexBuffer = Buffers.newDirectIntBuffer(data.length);
    indexBuffer.put(data);
    indexBuffer.rewind();

    gl.glGenBuffers(1, id, 0);
    CoreCheckGL.checkGLError("glGenBuffers");
    bind();
    send();
  }
}
