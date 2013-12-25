package de.lessvoid.nifty.renderer.jogl.render.batch.core;


import com.jogamp.common.nio.Buffers;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;
import java.nio.FloatBuffer;

/**
 * The CoreArrayVBO class represents a VBO bound to GL_ARRAY_BUFFER.
 *
 * @author void
 */
public class CoreVBO {
  private final int id;
  private final int usage;
  private final FloatBuffer vertexBuffer;

  /**
   * Create a new VBO with static vertex data (GL_STATIC_DRAW). This will
   * create the buffer object but does not bind or sends the data to the GPU.
   * You'll need to call bind() to bind this VBO and you'll need to call sendData()
   * to transmit the buffer data to the GPU.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreVBO createStatic(@Nonnull final float[] data) {
    return new CoreVBO(GL.GL_STATIC_DRAW, data);
  }

  /**
   * This provides the same functionality as createStaticVBO() but automatically
   * sends the data given to the GPU.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreVBO createStaticAndSend(@Nonnull final float[] data) {
    CoreVBO result = new CoreVBO(GL.GL_STATIC_DRAW, data);
    result.send();
    return result;
  }

  /**
   * This provides the same functionality as createStatic() but automatically
   * sends the data given to the GPU.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreVBO createStaticAndSend(@Nonnull final FloatBuffer data) {
    CoreVBO result = new CoreVBO(GL.GL_STATIC_DRAW, data.array());
    result.send();
    return result;
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_DYNAMIC_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreVBO createDynamic(@Nonnull final float[] data) {
    return new CoreVBO(GL.GL_DYNAMIC_DRAW, data);
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_STREAM_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreVBO createStream(@Nonnull final float[] data) {
    return new CoreVBO(GL3.GL_STREAM_DRAW, data);
  }

  private CoreVBO(final int usageType, @Nonnull final float[] data) {
    usage = usageType;

    vertexBuffer = Buffers.newDirectFloatBuffer(data.length);
    vertexBuffer.put(data);
    vertexBuffer.rewind();

    final GL gl = GLContext.getCurrentGL();
    int[] buffer = new int[1];
    gl.getGL2ES2().glGenBuffers(1, buffer, 0);
    id = buffer[0];
    CoreCheckGL.checkGLError("glGenBuffers");

    gl.getGL2ES2().glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    gl.getGL2ES2().glBufferData(GL.GL_ARRAY_BUFFER, data.length, vertexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData");
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
  public FloatBuffer getBuffer() {
    return vertexBuffer;
  }

  /**
   * bind the buffer object as GL_ARRAY_BUFFER
   */
  public void bind() {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL2ES2().glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ARRAY_BUFFER)");
  }

  /**
   * Send the content of the FloatBuffer to the GPU.
   */
  public void send() {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL2ES2().glBufferData(GL.GL_ARRAY_BUFFER, vertexBuffer.limit() * 4, vertexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData(GL_ARRAY_BUFFER)");
  }

  /**
   * Delete all resources for this VBO.
   */
  public void delete() {
    int[] buffer = new int[1];
    buffer[0] = id;
    final GL gl = GLContext.getCurrentGL();
    gl.getGL2ES2().glDeleteBuffers(1, buffer, 0);
  }
}
