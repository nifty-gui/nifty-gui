package de.lessvoid.nifty.gdx.render.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ObjectMap;

import de.lessvoid.nifty.batch.OpenGLBatchRenderBackend;
import de.lessvoid.nifty.batch.spi.BatchRenderBackend;
import de.lessvoid.nifty.gdx.render.GdxImage;
import de.lessvoid.nifty.gdx.render.GdxMouseCursor;
import de.lessvoid.nifty.spi.render.MouseCursor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This {@link BatchRenderBackend} implementation includes full support for multiple texture atlases and non-atlas
 * textures.
 *
 * LibGDX & OpenGL / OpenGL ES implementation of the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} interface.
 * This implementation will be the most backwards-compatible because it doesn't use any functions beyond OpenGL /
 * OpenGL ES 1.1. It is suitable for both desktop and mobile devices.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBatchRenderBackend extends OpenGLBatchRenderBackend <GdxBatch> {
  @Nonnull
  private static final Logger log = Logger.getLogger(GdxBatchRenderBackend.class.getName());
  @Nonnull
  private final ObjectMap<String, GdxMouseCursor> mouseCursors = new ObjectMap<String, GdxMouseCursor>();
  @Nullable
  private MouseCursor mouseCursor;

  @Override
  public int getWidth() {
    log.fine("getWidth()");
    return Gdx.graphics.getWidth();
  }

  @Override
  public int getHeight() {
    log.fine("getHeight()");
    return Gdx.graphics.getHeight();
  }

  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY)
          throws IOException {
    log.fine("createMouseCursor()");
    return existsCursor(filename) ? getCursor(filename) : createCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    log.fine("enableMouseCursor()");
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    log.fine("disableMouseCursor()");
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }

  @Nonnull
  @Override
  public GdxBatch createBatch() {
    return new GdxBatch();
  }

  @Nonnull
  @Override
  protected Image createImageFromFile(@Nullable String filename) {
    return new GdxImage(filename);
  }

  @Nonnull
  @Override
  protected Image createImageFromBuffer(@Nullable ByteBuffer buffer, int imageWidth, int imageHeight) {
    return new GdxImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  protected ByteBuffer getImageAsBuffer(Image image) {
    return image instanceof GdxImage ? ((GdxImage)image).getBuffer() : null;
  }

  @Nonnull
  @Override
  protected ByteBuffer createNativeOrderedByteBuffer(int numBytes) {
    return BufferUtils.newByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  protected IntBuffer createNativeOrderedIntBuffer(int numInts) {
    return BufferUtils.newIntBuffer(numInts);
  }

  @Override
  protected int GL_ALPHA_TEST() {
    return GL10.GL_ALPHA_TEST;
  }

  @Override
  protected int GL_BLEND() {
    return GL10.GL_BLEND;
  }

  @Override
  protected int GL_COLOR_ARRAY() {
    return GL10.GL_COLOR_ARRAY;
  }

  @Override
  protected int GL_COLOR_BUFFER_BIT() {
    return GL10.GL_COLOR_BUFFER_BIT;
  }

  @Override
  protected int GL_CULL_FACE() {
    return GL10.GL_CULL_FACE;
  }

  @Override
  protected int GL_DEPTH_TEST() {
    return GL10.GL_DEPTH_TEST;
  }

  @Override
  protected int GL_INVALID_ENUM() {
    return GL10.GL_INVALID_ENUM;
  }

  @Override
  protected int GL_INVALID_OPERATION() {
    return GL10.GL_INVALID_OPERATION;
  }

  @Override
  protected int GL_INVALID_VALUE() {
    return GL10.GL_INVALID_VALUE;
  }

  @Override
  protected int GL_LIGHTING() {
    return GL10.GL_LIGHTING;
  }

  @Override
  protected int GL_LINEAR() {
    return GL10.GL_LINEAR;
  }

  @Override
  protected int GL_MAX_TEXTURE_SIZE() {
    return GL10.GL_MAX_TEXTURE_SIZE;
  }

  @Override
  protected int GL_MODELVIEW() {
    return GL10.GL_MODELVIEW;
  }

  @Override
  protected int GL_NEAREST() {
    return GL10.GL_NEAREST;
  }

  @Override
  protected int GL_NO_ERROR() {
    return GL10.GL_NO_ERROR;
  }

  @Override
  protected int GL_NOTEQUAL() {
    return GL10.GL_NOTEQUAL;
  }

  @Override
  protected int GL_OUT_OF_MEMORY() {
    return GL10.GL_OUT_OF_MEMORY;
  }

  @Override
  protected int GL_PROJECTION() {
    return GL10.GL_PROJECTION;
  }

  @Override
  protected int GL_RGBA() {
    return GL10.GL_RGBA;
  }

  @Override
  protected int GL_STACK_OVERFLOW() {
    return GL10.GL_STACK_OVERFLOW;
  }

  @Override
  protected int GL_STACK_UNDERFLOW() {
    return GL10.GL_STACK_UNDERFLOW;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL10.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TEXTURE_COORD_ARRAY() {
    return GL10.GL_TEXTURE_COORD_ARRAY;
  }

  @Override
  protected int GL_TEXTURE_MAG_FILTER() {
    return GL10.GL_TEXTURE_MAG_FILTER;
  }

  @Override
  protected int GL_TEXTURE_MIN_FILTER() {
    return GL10.GL_TEXTURE_MIN_FILTER;
  }

  @Override
  protected int GL_UNSIGNED_BYTE() {
    return GL10.GL_UNSIGNED_BYTE;
  }

  @Override
  protected int GL_VERTEX_ARRAY() {
    return GL10.GL_VERTEX_ARRAY;
  }

  @Override
  protected int GL_VIEWPORT() {
    return GL11.GL_VIEWPORT;
  }

  @Override
  protected void glAlphaFunc(int func, float ref) {
    Gdx.gl10.glAlphaFunc(func, ref);
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    Gdx.gl10.glBindTexture(target, texture);
  }

  @Override
  protected void glClear(int mask) {
    Gdx.gl10.glClear(mask);
  }

  @Override
  protected void glClearColor(float red, float green, float blue, float alpha) {
    Gdx.gl10.glClearColor(red, green, blue, alpha);
  }

  @Override
  protected void glDeleteTextures(int n, IntBuffer textures) {
    Gdx.gl10.glDeleteTextures(n, textures);
  }

  @Override
  protected void glDisable(int cap) {
    Gdx.gl10.glDisable(cap);
  }

  @Override
  protected void glDisableClientState(int array) {
    Gdx.gl10.glDisableClientState(array);
  }

  @Override
  protected void glEnable(int cap) {
    Gdx.gl10.glEnable(cap);
  }

  @Override
  protected void glEnableClientState(int array) {
    Gdx.gl10.glEnableClientState(array);
  }

  @Override
  protected void glGenTextures(int n, IntBuffer textures) {
    Gdx.gl10.glGenTextures(n, textures);
  }

  @Override
  protected int glGetError() {
    return Gdx.gl10.glGetError();
  }

  @Override
  protected void glGetIntegerv(int pname, IntBuffer params) {
    Gdx.gl10.glGetIntegerv(pname, params);
  }

  @Override
  protected void glLoadIdentity() {
    Gdx.gl10.glLoadIdentity();
  }

  @Override
  protected void glMatrixMode(int mode) {
    Gdx.gl10.glMatrixMode(mode);
  }

  @Override
  protected void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    Gdx.gl10.glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @Override
  protected void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  protected void glTexParameterf(int target, int pname, float param) {
    Gdx.gl10.glTexParameterf(target, pname, param);
  }

  @Override
  protected void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    Gdx.gl10.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  protected void glTranslatef(float x, float y, float z) {
    Gdx.gl10.glTranslatef(x, y, z);
  }

  @Override
  protected void glViewport(int x, int y, int width, int height) {
    Gdx.gl10.glViewport(x, y, width, height);
  }

  // Internal implementations

  private boolean existsCursor(final String filename) {
    return mouseCursors.containsKey(filename);
  }

  private GdxMouseCursor getCursor(final String filename) {
    return mouseCursors.get(filename);
  }

  private GdxMouseCursor createCursor (final String filename, final int hotspotX, final int hotspotY) {
    try {
      GdxMouseCursor mouseCursor = new GdxMouseCursor(new GdxImage(filename), hotspotX, hotspotY);
      mouseCursors.put(filename, mouseCursor);
      return mouseCursor;
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not create mouse cursor [" + filename + "]", e);
      return null;
    }
  }
}
