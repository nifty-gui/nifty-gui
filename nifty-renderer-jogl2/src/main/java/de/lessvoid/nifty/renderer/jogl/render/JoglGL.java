package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.batch.spi.GL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglGL implements GL {
  @Nonnull
  private static final GL2 gl2 = GLContext.getCurrentGL().getGL2();

  @Override
  public int GL_ALPHA_TEST() {
    return GL2.GL_ALPHA_TEST;
  }

  @Override
  public int GL_BLEND() {
    return GL2.GL_BLEND;
  }

  @Override
  public int GL_COLOR_ARRAY() {
    return GL2.GL_COLOR_ARRAY;
  }

  @Override
  public int GL_COLOR_BUFFER_BIT() {
    return GL2.GL_COLOR_BUFFER_BIT;
  }

  @Override
  public int GL_CULL_FACE() {
    return GL2.GL_CULL_FACE;
  }

  @Override
  public int GL_DEPTH_TEST() {
    return GL2.GL_DEPTH_TEST;
  }

  @Override
  public int GL_DST_COLOR() {
    return GL2.GL_DST_COLOR;
  }

  @Override
  public int GL_FLOAT() {
    return GL2.GL_FLOAT;
  }

  @Override
  public int GL_INVALID_ENUM() {
    return GL2.GL_INVALID_ENUM;
  }

  @Override
  public int GL_INVALID_OPERATION() {
    return GL2.GL_INVALID_OPERATION;
  }

  @Override
  public int GL_INVALID_VALUE() {
    return GL2.GL_INVALID_VALUE;
  }

  @Override
  public int GL_LIGHTING() {
    return GL2.GL_LIGHTING;
  }

  @Override
  public int GL_LINEAR() {
    return GL2.GL_LINEAR;
  }

  @Override
  public int GL_MAX_TEXTURE_SIZE() {
    return GL2.GL_MAX_TEXTURE_SIZE;
  }

  @Override
  public int GL_MODELVIEW() {
    return GL2.GL_MODELVIEW;
  }

  @Override
  public int GL_NEAREST() {
    return GL2.GL_NEAREST;
  }

  @Override
  public int GL_NO_ERROR() {
    return GL2.GL_NO_ERROR;
  }

  @Override
  public int GL_NOTEQUAL() {
    return GL2.GL_NOTEQUAL;
  }

  @Override
  public int GL_ONE_MINUS_SRC_ALPHA() {
    return GL2.GL_ONE_MINUS_SRC_ALPHA;
  }

  @Override
  public int GL_OUT_OF_MEMORY() {
    return GL2.GL_OUT_OF_MEMORY;
  }

  @Override
  public int GL_PROJECTION() {
    return GL2.GL_PROJECTION;
  }

  @Override
  public int GL_RGBA() {
    return GL2.GL_RGBA;
  }

  @Override
  public int GL_SRC_ALPHA() {
    return GL2.GL_SRC_ALPHA;
  }

  @Override
  public int GL_STACK_OVERFLOW() {
    return GL2.GL_STACK_OVERFLOW;
  }

  @Override
  public int GL_STACK_UNDERFLOW() {
    return GL2.GL_STACK_UNDERFLOW;
  }

  @Override
  public int GL_TEXTURE_2D() {
    return GL2.GL_TEXTURE_2D;
  }

  @Override
  public int GL_TEXTURE_COORD_ARRAY() {
    return GL2.GL_TEXTURE_COORD_ARRAY;
  }

  @Override
  public int GL_TEXTURE_MAG_FILTER() {
    return GL2.GL_TEXTURE_MAG_FILTER;
  }

  @Override
  public int GL_TEXTURE_MIN_FILTER() {
    return GL2.GL_TEXTURE_MIN_FILTER;
  }

  @Override
  public int GL_TRIANGLES() {
    return GL2.GL_TRIANGLES;
  }

  @Override
  public int GL_UNSIGNED_BYTE() {
    return GL2.GL_UNSIGNED_BYTE;
  }

  @Override
  public int GL_VERTEX_ARRAY() {
    return GL2.GL_VERTEX_ARRAY;
  }

  @Override
  public int GL_VIEWPORT() {
    return GL2.GL_VIEWPORT;
  }

  @Override
  public int GL_ZERO() {
    return GL2.GL_ZERO;
  }

  @Override
  public void glAlphaFunc(int func, float ref) {
    gl2.glAlphaFunc(func, ref);
  }

  @Override
  public void glBindTexture(int target, int texture) {
    gl2.glBindTexture(target, texture);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    gl2.glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glClear(int mask) {
    gl2.glClear(mask);
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    gl2.glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl2.glColorPointer(size, type, stride, pointer);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    gl2.glDeleteTextures(n, textures);
  }

  @Override
  public void glDisable(int cap) {
    gl2.glDisable(cap);
  }

  @Override
  public void glDisableClientState(int array) {
    gl2.glDisableClientState(array);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    gl2.glDrawArrays(mode, first, count);
  }

  @Override
  public void glEnable(int cap) {
    gl2.glEnable(cap);
  }

  @Override
  public void glEnableClientState(int array) {
    gl2.glEnableClientState(array);
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    gl2.glGenTextures(n, textures);
  }

  @Override
  public int glGetError() {
    return gl2.glGetError();
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    gl2.glGetIntegerv(pname, params);
  }

  @Override
  public void glLoadIdentity() {
    gl2.glLoadIdentity();
  }

  @Override
  public void glMatrixMode(int mode) {
    gl2.glMatrixMode(mode);
  }

  @Override
  public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    gl2.glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl2.glTexCoordPointer(size, type, stride, pointer);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    gl2.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    gl2.glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    gl2.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  public void glTranslatef(float x, float y, float z) {
    gl2.glTranslatef(x, y, z);
  }

  @Override
  public void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    gl2.glVertexPointer(size, type, stride, pointer);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    gl2.glViewport(x, y, width, height);
  }
}
