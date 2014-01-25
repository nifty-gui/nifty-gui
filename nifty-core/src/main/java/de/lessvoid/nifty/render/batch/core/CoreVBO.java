package de.lessvoid.nifty.render.batch.core;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * Represents an abstraction of a VBO (Vertex Buffer Object) bound as a GL_ARRAY_BUFFER.
 *
 * Note: Requires OpenGL 3.2 or greater.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreVBO {
  @Nonnull
  private final CoreGL gl;
  private final int id;
  private final int usage;
  private final long byteLength;
  @Nonnull
  private final FloatBuffer vertexBuffer;
  @Nonnull
  private final IntBuffer idBuffer;
  private ByteBuffer mappedBufferCache;

  /**
   * Creates a new VBO with the specified static vertex data (GL_STATIC_DRAW). This will create the vertex buffer
   * object but does not bind or send the vertex data to the GPU. You'll need to call {@link #bind()} to bind this VBO
   * and you'll need to call {@link #send()} to transmit the vertex data to the GPU.
   */
  @Nonnull
  public static CoreVBO createStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final float[] data) {
    return new CoreVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data);
  }

  /**
   * This provides the same functionality as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, float[])},
   * but automatically sends the specified vertex data to the GPU.
   */
  @Nonnull
  public static CoreVBO createAndSendStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final float[] data) {
    CoreVBO result = new CoreVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data);
    result.send();
    return result;
  }

  /**
   * This provides the same functionality as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, float[])},
   * but automatically sends the specified vertex data to the GPU.
   */
  @Nonnull
  public static CoreVBO createAndSendStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final FloatBuffer data) {
    CoreVBO result = new CoreVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data.array());
    result.send();
    return result;
  }

  /**
   * This works exactly as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, float[])},
   * but will use GL_DYNAMIC_DRAW instead.
   */
  @Nonnull
  public static CoreVBO createDynamicVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final float[] data) {
    return new CoreVBO(gl, bufferFactory, gl.GL_DYNAMIC_DRAW(), data);
  }

  /**
   * This works exactly as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, float[])},
   * but will use GL_STREAM_DRAW instead.
   */
  @Nonnull
  public static CoreVBO createStreamVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final float[] data) {
    return new CoreVBO(gl, bufferFactory, gl.GL_STREAM_DRAW(), data);
  }

  private CoreVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int usageType,
          @Nonnull final float[] data) {
    this.gl = gl;
    usage = usageType;
    byteLength = data.length << 2;

    vertexBuffer = bufferFactory.createNativeOrderedFloatBuffer(data.length);
    vertexBuffer.put(data);
    vertexBuffer.rewind();

    idBuffer = bufferFactory.createNativeOrderedIntBuffer(1);
    gl.glGenBuffers(1, idBuffer);
    id = idBuffer.get(0);
    CheckGL.checkGLError(gl, "glGenBuffers");

    gl.glBindBuffer(gl.GL_ARRAY_BUFFER(), id);
    gl.glBufferData(gl.GL_ARRAY_BUFFER(), vertexBuffer, usage);
    CheckGL.checkGLError(gl, "glBufferData");
  }

  /**
   * Allows access to the internal {@link java.nio.FloatBuffer} (stored in system memory, not GPU memory) that
   * contains the original vertex data. You can access and change this buffer if you want to update the buffer content.
   * Just make sure that you call {@link java.nio.Buffer#rewind()} before sending your new vertex data to the GPU with
   * the {@link #send()} method.
   */
  public FloatBuffer getBuffer() {
    return vertexBuffer;
  }

  /**
   * Allows access to the internal {@link java.nio.FloatBuffer} that contains the original vertex data by mapping this
   * VBO into client space (although the actual memory is on the GPU) and returns the buffer as a
   * {@link java.nio.FloatBuffer}. You can access and change this buffer if you want to update the buffer content. Just
   * make sure that you call {@link java.nio.Buffer#rewind()} before sending your new vertex data to the GPU with the
   * {@link #send()} method.
   */
  @Nonnull
  public FloatBuffer getMappedBuffer() {
    ByteBuffer dataBuffer = gl.glMapBuffer(gl.GL_ARRAY_BUFFER(), gl.GL_WRITE_ONLY(), byteLength, mappedBufferCache);
    CheckGL.checkGLError(gl, "getMappedBuffer(GL_ARRAY_BUFFER)");
    mappedBufferCache = dataBuffer;
    return dataBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

  /**
   * You'll need to call this when you're done writing vertex data into a mapped buffer obtained with
   * {@link #getMappedBuffer()} to return control back to the GPU.
   */
  public void unmapBuffer() {
    gl.glUnmapBuffer(gl.GL_ARRAY_BUFFER());
  }

  /**
   * Binds the VBO as a GL_ARRAY_BUFFER.
   */
  public void bind() {
    gl.glBindBuffer(gl.GL_ARRAY_BUFFER(), id);
    CheckGL.checkGLError(gl, "glBindBuffer(GL_ARRAY_BUFFER)");
  }

  /**
   * Sends the current vertex data to the GPU.
   */
  public void send() {
    gl.glBufferData(gl.GL_ARRAY_BUFFER(), vertexBuffer, usage);
    CheckGL.checkGLError(gl, "glBufferData(GL_ARRAY_BUFFER)");
  }

  /**
   * Deletes all vertex data associated with this VBO.
   */
  public void delete() {
    idBuffer.clear();
    idBuffer.put(0, id);
    gl.glDeleteBuffers(1, idBuffer);
  }
}
