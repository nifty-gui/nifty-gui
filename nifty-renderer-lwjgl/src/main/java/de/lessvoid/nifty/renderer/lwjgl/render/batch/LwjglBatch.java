package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.OpenGlBatch;

import java.nio.FloatBuffer;
import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Concrete Lwjgl-specific implementation of {@link de.lessvoid.nifty.batch.Batch} interface.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBatch extends OpenGlBatch {
  @Nonnull
  @Override
  protected FloatBuffer createFloatBuffer(int numFloats) {
    return BufferUtils.createFloatBuffer(numFloats);
  }

  @Override
  protected int GL_DST_COLOR() {
    return GL11.GL_DST_COLOR;
  }

  @Override
  protected int GL_FLOAT() {
    return GL11.GL_FLOAT;
  }

  @Override
  protected int GL_ONE_MINUS_SRC_ALPHA() {
    return GL11.GL_ONE_MINUS_SRC_ALPHA;
  }

  @Override
  protected int GL_SRC_ALPHA() {
    return GL11.GL_SRC_ALPHA;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL11.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TRIANGLES() {
    return GL11.GL_TRIANGLES;
  }

  @Override
  protected int GL_ZERO() {
    return GL11.GL_ZERO;
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    GL11.glBindTexture(target, texture);
  }

  @Override
  protected void glBlendFunc(int sfactor, int dfactor) {
    GL11.glBlendFunc(sfactor, dfactor);
  }

  @Override
  protected void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    GL11.glColorPointer(size, stride, pointer);
  }

  @Override
  protected void glDrawArrays(int mode, int first, int count) {
    GL11.glDrawArrays(mode, first, count);
  }

  @Override
  protected void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    GL11.glTexCoordPointer(size, stride, pointer);
  }

  @Override
  protected void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    GL11.glVertexPointer(size, stride, pointer);
  }
}
