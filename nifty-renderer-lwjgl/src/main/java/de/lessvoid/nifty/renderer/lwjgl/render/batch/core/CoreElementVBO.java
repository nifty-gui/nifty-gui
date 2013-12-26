package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;


import org.lwjgl.BufferUtils;

import javax.annotation.Nonnull;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL31.glPrimitiveRestartIndex;

/**
 * The CoreElementVBO class represents a VBO bound to GL_ELEMENT_BUFFER.
 *
 * @author void
 */
public class CoreElementVBO {
  private final int id;
  private final int usage;
  private final IntBuffer indexBuffer;

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
  public static CoreElementVBO createStatic(@Nonnull final int[] data) {
    return new CoreElementVBO(GL_STATIC_DRAW, data);
  }

  /**
   * This provides the same functionality as createStaticVBO() but automatically
   * sends the data given to the GPU.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreElementVBO createStaticAndSend(@Nonnull final int[] data) {
    CoreElementVBO result = new CoreElementVBO(GL_STATIC_DRAW, data);
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
  public static CoreElementVBO createStaticAndSend(@Nonnull final IntBuffer data) {
    CoreElementVBO result = new CoreElementVBO(GL_STATIC_DRAW, data.array());
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
  public static CoreElementVBO createDynamic(@Nonnull final int[] data) {
    return new CoreElementVBO(GL_DYNAMIC_DRAW, data);
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_STREAM_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  @Nonnull
  public static CoreElementVBO createStream(@Nonnull final int[] data) {
    return new CoreElementVBO(GL_STREAM_DRAW, data);
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
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ELEMENT_ARRAY_BUFFER)");
  }

  public void unbind() {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ELEMENT_ARRAY_BUFFER -> unbind)");
  }

  /**
   * Send the content of the FloatBuffer to the GPU.
   */
  public void send() {
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData(GL_ELEMENT_ARRAY_BUFFER)");
  }

  /**
   * Delete all resources for this VBO.
   */
  public void delete() {
    glDeleteBuffers(id);
  }

  /**
   * Enable primitive restart using the given value.
   *
   * @param value the value to use as primitive restart
   */
  public void enablePrimitiveRestart(final int value) {
    glPrimitiveRestartIndex(value);
    glEnable(GL_PRIMITIVE_RESTART);
  }

  /**
   * Disable primitive restart again.
   */
  public void disablePrimitiveRestart() {
    glDisable(GL_PRIMITIVE_RESTART);
  }

  private CoreElementVBO(final int usageType, @Nonnull final int[] data) {
    usage = usageType;

    indexBuffer = BufferUtils.createIntBuffer(data.length);
    indexBuffer.put(data);
    indexBuffer.rewind();

    id = glGenBuffers();
    CoreCheckGL.checkGLError("glGenBuffers");
    bind();
    send();
  }
}
