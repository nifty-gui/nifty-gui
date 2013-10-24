package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.BufferUtils;

import de.lessvoid.nifty.batch.OpenGlBatch;

import java.nio.FloatBuffer;
import javax.annotation.Nonnull;

/**
 * Concrete LibGDX-specific implementation of {@link de.lessvoid.nifty.batch.Batch} interface.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBatch extends OpenGlBatch {
  @Nonnull
  @Override
  protected FloatBuffer createFloatBuffer(int numFloats) {
    return BufferUtils.newFloatBuffer(numFloats);
  }

  @Override
  protected int GL_DST_COLOR() {
    return GL10.GL_DST_COLOR;
  }

  @Override
  protected int GL_FLOAT() {
    return GL10.GL_FLOAT;
  }

  @Override
  protected int GL_ONE_MINUS_SRC_ALPHA() {
    return GL10.GL_ONE_MINUS_SRC_ALPHA;
  }

  @Override
  protected int GL_SRC_ALPHA() {
    return GL10.GL_SRC_ALPHA;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL10.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TRIANGLES() {
    return GL10.GL_TRIANGLES;
  }

  @Override
  protected int GL_ZERO() {
    return GL10.GL_ZERO;
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    Gdx.gl10.glBindTexture(target, texture);
  }

  @Override
  protected void glBlendFunc(int sfactor, int dfactor) {
    Gdx.gl10.glBlendFunc(sfactor, dfactor);
  }

  @Override
  protected void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glColorPointer(size, type, stride, pointer);
  }

  @Override
  protected void glDrawArrays(int mode, int first, int count) {
    Gdx.gl10.glDrawArrays(mode, first, count);
  }

  @Override
  protected void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glTexCoordPointer(size, type, stride, pointer);
  }

  @Override
  protected void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glVertexPointer(size, type, stride, pointer);
  }
}
