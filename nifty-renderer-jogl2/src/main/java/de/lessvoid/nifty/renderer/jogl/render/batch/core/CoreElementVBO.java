package de.lessvoid.nifty.renderer.jogl.render.batch.core;


import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;

import com.jogamp.common.nio.Buffers;

/**
 * The CoreElementVBO class represents a VBO bound to GL_ELEMENT_BUFFER.
 * @author void
 */
public class CoreElementVBO {
  private int[] id = new int[1];
  private int usage;
  private IntBuffer indexBuffer;

  /**
   * Create a new VBO with static vertex data (GL_STATIC_DRAW). This will
   * create the buffer object but does not bind or sends the data to the GPU.
   * You'll need to call bind() to bind this VBO and you'll need to call sendData()
   * to transmit the buffer data to the GPU.
   * 
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreElementVBO createStatic(final int[] data) {
    return new CoreElementVBO(GL.GL_STATIC_DRAW, data);
  }

  /**
   * This provides the same functionality as createStaticVBO() but automatically
   * sends the data given to the GPU.
   * 
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreElementVBO createStaticAndSend(final int[] data) {
    CoreElementVBO result = new CoreElementVBO(GL.GL_STATIC_DRAW, data);
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
  public static CoreElementVBO createStaticAndSend(final IntBuffer data) {
    CoreElementVBO result = new CoreElementVBO(GL.GL_STATIC_DRAW, data.array());
    result.send();
    return result;
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_DYNAMIC_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreElementVBO createDynamic(final int[] data) {
    return new CoreElementVBO(GL.GL_DYNAMIC_DRAW, data);
  }

  /**
   * This works exactly as createStaticVBO() but will use GL_STREAM_DRAW instead.
   *
   * @param data float array of buffer data
   * @return the CoreVBO instance created
   */
  public static CoreElementVBO createStream(final int[] data) {
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
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.limit()*4, indexBuffer, usage);
    CoreCheckGL.checkGLError("glBufferData(GL_ELEMENT_ARRAY_BUFFER)");
  }

  /**
   * Delete all resources for this VBO.
   */
  public void delete() {
    final GL gl = GLContext.getCurrentGL();
    gl.glDeleteBuffers(1, id, 0);
  }

  /**
   * Enable primitive restart using the given value.
   * @param value the value to use as primitive restart
   */
  public void enablePrimitiveRestart(final int value) {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL3().glPrimitiveRestartIndex(value);
    gl.glEnable(GL3.GL_PRIMITIVE_RESTART);
  }

  /**
   * Disable primitive restart again.
   */
  public void disablePrimitiveRestart() {
    final GL gl = GLContext.getCurrentGL();
    gl.glDisable(GL3.GL_PRIMITIVE_RESTART);
  }

  private CoreElementVBO(final int usageType, final int[] data) {
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
