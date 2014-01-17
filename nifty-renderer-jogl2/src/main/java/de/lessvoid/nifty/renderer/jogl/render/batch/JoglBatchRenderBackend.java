package de.lessvoid.nifty.renderer.jogl.render.batch;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.OpenGLBatchRenderBackend;
import de.lessvoid.nifty.renderer.jogl.render.JoglImage;
import de.lessvoid.nifty.renderer.jogl.render.JoglMouseCursor;
import de.lessvoid.nifty.spi.render.MouseCursor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * This {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementation includes full support for multiple
 * texture atlases and non-atlas textures.
 *
 * Jogl implementation of the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} interface. This implementation
 * will be the most backwards-compatible because it doesn't use any functions beyond OpenGL 1.1. It is suitable for
 * desktop devices.
 *
 * {@inheritDoc}
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 * @author void
 */
public class JoglBatchRenderBackend extends OpenGLBatchRenderBackend<JoglBatch> {
  @Nonnull
  private static final GL2 gl = GLContext.getCurrentGL().getGL2();

  @Nonnull
  @Override
  protected Image createImageFromBuffer(@Nullable ByteBuffer buffer, int imageWidth, int imageHeight) {
    return new JoglImage(buffer, imageWidth, imageHeight);
  }

  @Nullable
  @Override
  protected ByteBuffer getImageAsBuffer(Image image) {
    return image instanceof JoglImage ? ((JoglImage)image).getBuffer() : null;
  }

  @Nonnull
  @Override
  protected ByteBuffer createNativeOrderedByteBuffer(int numBytes) {
    return Buffers.newDirectByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  protected IntBuffer createNativeOrderedIntBuffer(int numInts) {
    return Buffers.newDirectIntBuffer(numInts);
  }

  @Nullable
  @Override
  public MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException {
    return new JoglMouseCursor(filename, hotspotX, hotspotY, getResourceLoader());
  }

  @Nonnull
  @Override
  public JoglBatch createBatch() {
    return new JoglBatch();
  }

  @Override
  protected int GL_ALPHA_TEST() {
    return GL2.GL_ALPHA_TEST;
  }

  @Override
  protected int GL_BLEND() {
    return GL.GL_BLEND;
  }

  @Override
  protected int GL_COLOR_ARRAY() {
    return GL2.GL_COLOR_ARRAY;
  }

  @Override
  protected int GL_COLOR_BUFFER_BIT() {
    return GL.GL_COLOR_BUFFER_BIT;
  }

  @Override
  protected int GL_CULL_FACE() {
    return GL.GL_CULL_FACE;
  }

  @Override
  protected int GL_DEPTH_TEST() {
    return GL.GL_DEPTH_TEST;
  }

  @Override
  protected int GL_INVALID_ENUM() {
    return GL.GL_INVALID_ENUM;
  }

  @Override
  protected int GL_INVALID_OPERATION() {
    return GL.GL_INVALID_OPERATION;
  }

  @Override
  protected int GL_INVALID_VALUE() {
    return GL.GL_INVALID_VALUE;
  }

  @Override
  protected int GL_LIGHTING() {
    return GL2.GL_LIGHTING;
  }

  @Override
  protected int GL_LINEAR() {
    return GL.GL_LINEAR;
  }

  @Override
  protected int GL_MAX_TEXTURE_SIZE() {
    return GL.GL_MAX_TEXTURE_SIZE;
  }

  @Override
  protected int GL_MODELVIEW() {
    return GL2.GL_MODELVIEW;
  }

  @Override
  protected int GL_NEAREST() {
    return GL.GL_NEAREST;
  }

  @Override
  protected int GL_NO_ERROR() {
    return GL.GL_NO_ERROR;
  }

  @Override
  protected int GL_NOTEQUAL() {
    return GL.GL_NOTEQUAL;
  }

  @Override
  protected int GL_OUT_OF_MEMORY() {
    return GL.GL_OUT_OF_MEMORY;
  }

  @Override
  protected int GL_PROJECTION() {
    return GL2.GL_PROJECTION;
  }

  @Override
  protected int GL_RGBA() {
    return GL.GL_RGBA;
  }

  @Override
  protected int GL_STACK_OVERFLOW() {
    return GL2.GL_STACK_OVERFLOW;
  }

  @Override
  protected int GL_STACK_UNDERFLOW() {
    return GL2.GL_STACK_UNDERFLOW;
  }

  @Override
  protected int GL_TEXTURE_2D() {
    return GL.GL_TEXTURE_2D;
  }

  @Override
  protected int GL_TEXTURE_COORD_ARRAY() {
    return GL2.GL_TEXTURE_COORD_ARRAY;
  }

  @Override
  protected int GL_TEXTURE_MAG_FILTER() {
    return GL.GL_TEXTURE_MAG_FILTER;
  }

  @Override
  protected int GL_TEXTURE_MIN_FILTER() {
    return GL.GL_TEXTURE_MIN_FILTER;
  }

  @Override
  protected int GL_UNSIGNED_BYTE() {
    return GL.GL_UNSIGNED_BYTE;
  }

  @Override
  protected int GL_VERTEX_ARRAY() {
    return GL2.GL_VERTEX_ARRAY;
  }

  @Override
  protected int GL_VIEWPORT() {
    return GL.GL_VIEWPORT;
  }

  @Override
  protected void glAlphaFunc(int func, float ref) {
    gl.glAlphaFunc(func, ref);
  }

  @Override
  protected void glBindTexture(int target, int texture) {
    gl.glBindTexture(target, texture);
  }

  @Override
  protected void glClear(int mask) {
    gl.glClear(mask);
  }

  @Override
  protected void glClearColor(float red, float green, float blue, float alpha) {
    gl.glClearColor(red, green, blue, alpha);
  }

  @Override
  protected void glDeleteTextures(int n, IntBuffer textures) {
    gl.glDeleteTextures(n, textures);
  }

  @Override
  protected void glDisable(int cap) {
    gl.glDisable(cap);
  }

  @Override
  protected void glDisableClientState(int array) {
    gl.glDisableClientState(array);
  }

  @Override
  protected void glEnable(int cap) {
    gl.glEnable(cap);
  }

  @Override
  protected void glEnableClientState(int array) {
    gl.glEnableClientState(array);
  }

  @Override
  protected void glGenTextures(int n, IntBuffer textures) {
    gl.glGenTextures(n, textures);
  }

  @Override
  protected int glGetError() {
    return gl.glGetError();
  }

  @Override
  protected void glGetIntegerv(int pname, IntBuffer params) {
    gl.glGetIntegerv(pname, params);
  }

  @Override
  protected void glLoadIdentity() {
    gl.glLoadIdentity();
  }

  @Override
  protected void glMatrixMode(int mode) {
    gl.glMatrixMode(mode);
  }

  @Override
  protected void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    gl.glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @Override
  protected void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  protected void glTexParameterf(int target, int pname, float param) {
    gl.glTexParameterf(target, pname, param);
  }

  @Override
  protected void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  protected void glTranslatef(float x, float y, float z) {
    gl.glTranslatef(x, y, z);
  }

  @Override
  protected void glViewport(int x, int y, int width, int height) {
    gl.glViewport(x, y, width, height);
  }
}
