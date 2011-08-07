package de.lessvoid.nifty.renderer.lwjgl.render;

import static org.lwjgl.opengl.ARBBufferObject.GL_DYNAMIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VBO {
  private int id;

  public VBO() {
    IntBuffer buffer = BufferUtils.createIntBuffer(1);
    glGenBuffersARB(buffer);
    id = buffer.get(0);
  }

  public void bufferDynamicData(final float[] data) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.rewind();
    glBindBufferARB(GL_ARRAY_BUFFER_ARB, id);
    glBufferDataARB(GL_ARRAY_BUFFER_ARB, buffer, GL_DYNAMIC_DRAW_ARB);
  }

  public void bind() {
    glBindBufferARB(GL_ARRAY_BUFFER_ARB, id);
  }
}
