package de.lessvoid.nifty.renderer.lwjgl.render;

import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VBOStandard {
  private int id;
  private static int bla;

  public VBOStandard() {
    IntBuffer buffer = BufferUtils.createIntBuffer(1);
    glGenBuffersARB(buffer);
    id = buffer.get(0);
  }

  public void bufferDynamicData(final float[] data) {
    if ((data.length*4) > bla) {
      bla = data.length*4;
      System.out.println("size: " + bla);
    }
    FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
    buffer.put(data);
    buffer.rewind();
    glBindBufferARB(GL_ARRAY_BUFFER_ARB, id);
    glBufferDataARB(GL_ARRAY_BUFFER_ARB, buffer, GL_STATIC_DRAW_ARB);
  }

  public void bind() {
    glBindBufferARB(GL_ARRAY_BUFFER_ARB, id);
  }
}
