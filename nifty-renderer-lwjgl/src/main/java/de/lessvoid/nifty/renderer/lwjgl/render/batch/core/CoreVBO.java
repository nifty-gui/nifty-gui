package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;


import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * The CoreArrayVBO class represents a VBO bound to GL_ARRAY_BUFFER.
 * @author void
 */
public class CoreVBO {
  private final int id;
  private final int usage;
  private final long byteLength;
  private FloatBuffer vertexBuffer;
  private ByteBuffer mappedBufferCache;

  /**
   * Create a new VBO with static vertex data (GL_STATIC_DRAW). This will
   * create the buffer object but does not bind or sends the data to the GPU.
   * You'll need to call bind() to bind this VBO and you'll need to call sendData()
   * to transmit the buffer data to the GPU.
   * 
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreVBO createStatic(final float[] data) {
    return new CoreVBO(GL_STATIC_DRAW, data);
  }

  /**
   * This provides the same functionality as createStaticVBO() but automatically
   * sends the data given to the GPU.
   * 
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreVBO createStaticAndSend(final float[] data) {
    CoreVBO result = new CoreVBO(GL_STATIC_DRAW, data);
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
  public static CoreVBO createStaticAndSend(final FloatBuffer data) {
    CoreVBO result = new CoreVBO(GL_STATIC_DRAW, data.array());
    result.send();
    return result;
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_DYNAMIC_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreVBO createDynamic(final float[] data) {
    return new CoreVBO(GL_DYNAMIC_DRAW, data);
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_STREAM_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreVBO createStream(final float[] data) {
    return new CoreVBO(GL_STREAM_DRAW, data);
  }

  private CoreVBO(final int usageType, final float[] data) {
    usage = usageType;
    byteLength = data.length << 2;

    vertexBuffer = BufferUtils.createFloatBuffer(data.length);
    vertexBuffer.put(data);
    vertexBuffer.rewind();

    id = glGenBuffers();
    CoreCheckGL.checkGLError("glGenBuffers");

    glBindBuffer(GL_ARRAY_BUFFER, id);
    glBufferData(GL_ARRAY_BUFFER, vertexBuffer, usage);
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
   * Maps the buffer object that this represents into client space and returns the buffer as a FloatBuffer
   * @return the FloatBuffer to directly write data into (mapped into client space but is actual memory on the GPU)
   */
  public FloatBuffer getMappedBuffer() {
    ByteBuffer dataBuffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, byteLength, mappedBufferCache);
    CoreCheckGL.checkGLError("getMappedBuffer(GL_ARRAY_BUFFER)");

    mappedBufferCache = dataBuffer;
    return dataBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

  /**
   * You'll need to call that when you're done writing data into a mapped buffer to return access back to the GPU.
   */
  public void unmapBuffer() {
    glUnmapBuffer(GL_ARRAY_BUFFER);
  }

  /**
   * bind the buffer object as GL_ARRAY_BUFFER
   */
  public void bind() {
    glBindBuffer(GL_ARRAY_BUFFER, id);
    CoreCheckGL.checkGLError("glBindBuffer(GL_ARRAY_BUFFER)");
  }

  /**
   * Send the content of the FloatBuffer to the GPU.
   */
  public void send() {
    glBufferData(GL_ARRAY_BUFFER, vertexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData(GL_ARRAY_BUFFER)");
  }

  /**
   * Delete all resources for this VBO.
   */
  public void delete() {
    glDeleteBuffers(id);
  }
}
