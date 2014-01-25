package de.lessvoid.nifty.render.batch.core;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * Represents an abstraction of a VBO (Vertex Buffer Object) bound as a GL_ELEMENT_BUFFER.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *
 * Note: Requires OpenGL 3.2 or greater.
 */
public class CoreElementVBO {
  @Nonnull
  private final CoreGL gl;
  @Nonnull
  private final IntBuffer indexBuffer;
  @Nonnull
  private final IntBuffer idBuffer;
  private final int id;
  private final int usage;

  /**
   * Creates a new VBO with the specified static vertex data (GL_STATIC_DRAW). This will create the vertex buffer
   * object but does not bind or send the vertex data to the GPU. You'll need to call {@link #bind()} to bind this VBO
   * and you'll need to call {@link #send()} to transmit the vertex data to the GPU.
   */
  @Nonnull
  public static CoreElementVBO createStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final int[] data) {
    return new CoreElementVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data);
  }

  /**
   * This provides the same functionality as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, int[])},
   * but automatically sends the specified vertex data to the GPU.
   */
  @Nonnull
  public static CoreElementVBO createAndSendStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final int[] data) {
    CoreElementVBO result = new CoreElementVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data);
    result.send();
    return result;
  }

  /**
   * This provides the same functionality as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, int[])},
   * but automatically sends the specified vertex data to the GPU.
   */
  @Nonnull
  public static CoreElementVBO createAndSendStaticVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final IntBuffer data) {
    CoreElementVBO result = new CoreElementVBO(gl, bufferFactory, gl.GL_STATIC_DRAW(), data.array());
    result.send();
    return result;
  }

  /**
   * This works exactly as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, int[])},
   * but will use GL_DYNAMIC_DRAW instead.
   */
  @Nonnull
  public static CoreElementVBO createDynamicVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final int[] data) {
    return new CoreElementVBO(gl, bufferFactory, gl.GL_DYNAMIC_DRAW(), data);
  }

  /**
   * This works exactly as
   * {@link #createStaticVBO(de.lessvoid.nifty.render.batch.spi.core.CoreGL, de.lessvoid.nifty.render.batch.spi.BufferFactory, int[])},
   * but will use GL_STREAM_DRAW instead.
   */
  @Nonnull
  public static CoreElementVBO createStreamVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final int[] data) {
    return new CoreElementVBO(gl, bufferFactory, gl.GL_STREAM_DRAW(), data);
  }

  /**
   * Allows access to the internal {@link java.nio.FloatBuffer} (stored in system memory, not GPU memory) that
   * contains the original vertex data. You can access and change this buffer if you want to update the buffer content.
   * Just make sure that you call {@link java.nio.Buffer#rewind()} before sending your new vertex data to the GPU with
   * the {@link #send()} method.
   */
  public IntBuffer getBuffer() {
    return indexBuffer;
  }

  /**
   * Binds the VBO as a GL_ELEMENT_BUFFER.
   */
  public void bind() {
    gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER(), id);
    CheckGL.checkGLError(gl, "glBindBuffer(GL_ELEMENT_ARRAY_BUFFER)");
  }

  public void unbind() {
    gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER(), 0);
    CheckGL.checkGLError(gl, "glBindBuffer(GL_ELEMENT_ARRAY_BUFFER -> unbind)");
  }

  /**
   * Sends the current vertex data to the GPU.
   */
  public void send() {
    gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER(), indexBuffer, usage);
    CheckGL.checkGLError(gl, "glBufferData(GL_ELEMENT_ARRAY_BUFFER)");
  }

  /**
   * Deletes all vertex data associated with this VBO.
   */
  public void delete() {
    idBuffer.clear();
    idBuffer.put(id);
    gl.glDeleteBuffers(1, idBuffer);
  }

  /**
   * Enables primitive restart using the specified value.
   *
   * @param value The value to use as primitive restart.
   */
  public void enablePrimitiveRestart(final int value) {
    gl.glPrimitiveRestartIndex(value);
    gl.glEnable(gl.GL_PRIMITIVE_RESTART());
  }

  /**
   * Disables primitive restart.
   */
  public void disablePrimitiveRestart() {
    gl.glDisable(gl.GL_PRIMITIVE_RESTART());
  }

  private CoreElementVBO(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int usageType,
          @Nonnull final int[] data) {
    this.gl = gl;
    usage = usageType;

    indexBuffer = bufferFactory.createNativeOrderedIntBuffer(data.length);
    indexBuffer.put(data);
    indexBuffer.rewind();

    idBuffer = bufferFactory.createNativeOrderedIntBuffer(data.length);
    gl.glGenBuffers(1, idBuffer);
    id = idBuffer.get(0);

    CheckGL.checkGLError(gl, "glGenBuffers");
    bind();
    send();
  }
}
