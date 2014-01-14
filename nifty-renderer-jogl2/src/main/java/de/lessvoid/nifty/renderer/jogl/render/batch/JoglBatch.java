package de.lessvoid.nifty.renderer.jogl.render.batch;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.OpenGlBatch;

import java.nio.FloatBuffer;
import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * Concrete JOGL-specific implementation of {@link de.lessvoid.nifty.batch.Batch} interface.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBatch extends OpenGlBatch {
  private final GL2 gl;

  public JoglBatch() {
    gl = GLContext.getCurrentGL().getGL2();
  }

  @Nonnull
  @Override
  protected FloatBuffer createFloatBuffer(int numFloats) {
    return Buffers.newDirectFloatBuffer(numFloats);
  }

  @Override
  protected int GL_DST_COLOR() {
    return GL.GL_DST_COLOR;
  }

  @Override
  protected int GL_FLOAT() {
    return GL.GL_FLOAT;
  }

  @Override
  protected int GL_ONE_MINUS_SRC_ALPHA() {
    return GL.GL_ONE_MINUS_SRC_ALPHA;
  }

  @Override
  protected int GL_SRC_ALPHA() {
    return GL.GL_SRC_ALPHA;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TRIANGLES() {
    return GL.GL_TRIANGLES;
  }

  @Override
  protected int GL_ZERO() {
    return GL.GL_ZERO;
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    gl.glBindTexture(target, texture);
  }

  @Override
  protected void glBlendFunc(int sfactor, int dfactor) {
    gl.glBlendFunc(sfactor, dfactor);
  }

  @Override
  protected void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl.glColorPointer(size, type, stride, pointer);
  }

  @Override
  protected void glDrawArrays(int mode, int first, int count) {
    gl.glDrawArrays(mode, first, count);
  }

  @Override
  protected void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl.glTexCoordPointer(size, type, stride, pointer);
  }

  @Override
  protected void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl.glVertexPointer(size, type, stride, pointer);
  }
}
