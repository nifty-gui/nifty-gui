package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import de.lessvoid.nifty.batch.OpenGLBatchRenderBackend;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglImage;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglMouseCursor;
import de.lessvoid.nifty.spi.render.MouseCursor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * This {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementation includes full support for multiple
 * texture atlases and non-atlas textures.
 *
 * Lwjgl implementation of the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} interface. This implementation
 * will be the most backwards-compatible because it doesn't use any functions beyond OpenGL 1.1. It is suitable for
 * desktop devices.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 * @author void
 */
public class LwjglBatchRenderBackend extends OpenGLBatchRenderBackend<LwjglBatch> {
  @Nonnull
  private static final Logger log = Logger.getLogger(LwjglBatchRenderBackend.class.getName());

  @Nonnull
  @Override
  protected Image createImageFromBuffer(@Nullable ByteBuffer buffer, int imageWidth, int imageHeight) {
    return new LwjglImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  protected ByteBuffer getImageAsBuffer(Image image) {
    return image instanceof LwjglImage ? ((LwjglImage)image).getBuffer() : null;
  }

  @Nonnull
  @Override
  protected ByteBuffer createNativeOrderedByteBuffer(int numBytes) {
    return BufferUtils.createByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  protected IntBuffer createNativeOrderedIntBuffer(int numInts) {
    return BufferUtils.createIntBuffer(numInts);
  }

  @Nonnull
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY)
          throws IOException {
    return new LwjglMouseCursor(filename, hotspotX, hotspotY, getResourceLoader());
  }

  @Nonnull
  @Override
  public LwjglBatch createBatch() {
    return new LwjglBatch();
  }

  @Override
  protected int GL_ALPHA_TEST() {
    return GL11.GL_ALPHA_TEST;
  }

  @Override
  protected int GL_BLEND() {
    return GL11.GL_BLEND;
  }

  @Override
  protected int GL_COLOR_ARRAY() {
    return GL11.GL_COLOR_ARRAY;
  }

  @Override
  protected int GL_COLOR_BUFFER_BIT() {
    return GL11.GL_COLOR_BUFFER_BIT;
  }

  @Override
  protected int GL_CULL_FACE() {
    return GL11.GL_CULL_FACE;
  }

  @Override
  protected int GL_DEPTH_TEST() {
    return GL11.GL_DEPTH_TEST;
  }

  @Override
  protected int GL_INVALID_ENUM() {
    return GL11.GL_INVALID_ENUM;
  }

  @Override
  protected int GL_INVALID_OPERATION() {
    return GL11.GL_INVALID_OPERATION;
  }

  @Override
  protected int GL_INVALID_VALUE() {
    return GL11.GL_INVALID_VALUE;
  }

  @Override
  protected int GL_LIGHTING() {
    return GL11.GL_LIGHTING;
  }

  @Override
  protected int GL_LINEAR() {
    return GL11.GL_LINEAR;
  }

  @Override
  protected int GL_MAX_TEXTURE_SIZE() {
    return GL11.GL_MAX_TEXTURE_SIZE;
  }

  @Override
  protected int GL_MODELVIEW() {
    return GL11.GL_MODELVIEW;
  }

  @Override
  protected int GL_NEAREST() {
    return GL11.GL_NEAREST;
  }

  @Override
  protected int GL_NO_ERROR() {
    return GL11.GL_NO_ERROR;
  }

  @Override
  protected int GL_NOTEQUAL() {
    return GL11.GL_NOTEQUAL;
  }

  @Override
  protected int GL_OUT_OF_MEMORY() {
    return GL11.GL_OUT_OF_MEMORY;
  }

  @Override
  protected int GL_PROJECTION() {
    return GL11.GL_PROJECTION;
  }

  @Override
  protected int GL_RGBA() {
    return GL11.GL_RGBA;
  }

  @Override
  protected int GL_STACK_OVERFLOW() {
    return GL11.GL_STACK_OVERFLOW;
  }

  @Override
  protected int GL_STACK_UNDERFLOW() {
    return GL11.GL_STACK_UNDERFLOW;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL11.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TEXTURE_COORD_ARRAY() {
    return GL11.GL_TEXTURE_COORD_ARRAY;
  }

  @Override
  protected int GL_TEXTURE_MAG_FILTER() {
    return GL11.GL_TEXTURE_MAG_FILTER;
  }

  @Override
  protected int GL_TEXTURE_MIN_FILTER() {
    return GL11.GL_TEXTURE_MIN_FILTER;
  }

  @Override
  protected int GL_UNSIGNED_BYTE() {
    return GL11.GL_UNSIGNED_BYTE;
  }

  @Override
  protected int GL_VERTEX_ARRAY() {
    return GL11.GL_VERTEX_ARRAY;
  }

  @Override
  protected int GL_VIEWPORT() {
    return GL11.GL_VIEWPORT;
  }

  @Override
  protected void glAlphaFunc(int func, float ref) {
    GL11.glAlphaFunc(func, ref);
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    GL11.glBindTexture(target, texture);
  }

  @Override
  protected void glClear(int mask) {
    GL11.glClear(mask);
  }

  @Override
  protected void glClearColor(float red, float green, float blue, float alpha) {
    GL11.glClearColor(red, green, blue, alpha);
  }

  @Override
  protected void glDeleteTextures(int n, IntBuffer textures) {
    GL11.glDeleteTextures(textures);
  }

  @Override
  protected void glDisable(int cap) {
    GL11.glDisable(cap);
  }

  @Override
  protected void glDisableClientState(int array) {
    GL11.glDisableClientState(array);
  }

  @Override
  protected void glEnable(int cap) {
    GL11.glEnable(cap);
  }

  @Override
  protected void glEnableClientState(int array) {
    GL11.glEnableClientState(array);
  }

  @Override
  protected void glGenTextures(int n, IntBuffer textures) {
    GL11.glGenTextures(textures);
  }

  @Override
  protected int glGetError() {
    return GL11.glGetError();
  }

  @Override
  protected void glGetIntegerv(int pname, IntBuffer params) {
    GL11.glGetInteger(pname, params);
  }

  @Override
  protected void glLoadIdentity() {
    GL11.glLoadIdentity();
  }

  @Override
  protected void glMatrixMode(int mode) {
    GL11.glMatrixMode(mode);
  }

  @Override
  protected void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    GL11.glOrtho(left, right, bottom, top, zNear, zFar);
  }

  @Override
  protected void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  protected void glTexParameterf(int target, int pname, float param) {
    GL11.glTexParameterf(target, pname, param);
  }

  @Override
  protected void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  protected void glTranslatef(float x, float y, float z) {
    GL11.glTranslatef(x, y, z);
  }

  @Override
  protected void glViewport(int x, int y, int width, int height) {
    GL11.glViewport(x, y, width, height);
  }
}
