package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.batch.spi.GL;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *         N.B: Please never store a GL instance as it might 
 *         become invalidated at runtime and it would allow to 
 *         call OpenGL when there is no current context on this thread
 */
public class JoglGL implements GL {

  @Override
  public int GL_ALPHA() {
    return GL2.GL_ALPHA;
  }

  @Override
  public int GL_ALPHA_TEST() {
    return GL2.GL_ALPHA_TEST;
  }

  @Override
  public int GL_BLEND() {
    return GL2.GL_BLEND;
  }

  @Override
  public int GL_BLEND_DST() {
    return GL2.GL_BLEND_DST;
  }

  @Override
  public int GL_BLEND_SRC() {
    return GL2.GL_BLEND_SRC;
  }

  @Override
  public int GL_BYTE() {
    return GL2.GL_BYTE;
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
  public int GL_FALSE() {
    return GL2.GL_FALSE;
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
  public int GL_LINEAR_MIPMAP_LINEAR() {
    return GL2.GL_LINEAR_MIPMAP_LINEAR;
  }

  @Override
  public int GL_LINEAR_MIPMAP_NEAREST() {
    return GL2.GL_LINEAR_MIPMAP_NEAREST;
  }

  @Override
  public int GL_LUMINANCE() {
    return GL2.GL_LUMINANCE;
  }

  @Override
  public int GL_LUMINANCE_ALPHA() {
    return GL2.GL_LUMINANCE_ALPHA;
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
  public int GL_NEAREST_MIPMAP_LINEAR() {
    return GL2.GL_NEAREST_MIPMAP_LINEAR;
  }

  @Override
  public int GL_NEAREST_MIPMAP_NEAREST() {
    return GL2.GL_NEAREST_MIPMAP_NEAREST;
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
  public int GL_POINTS() {
    return GL2.GL_POINTS;
  }

  @Override
  public int GL_PROJECTION() {
    return GL2.GL_PROJECTION;
  }

  @Override
  public int GL_RGB() {
    return GL2.GL_RGB;
  }

  @Override
  public int GL_RGBA() {
    return GL2.GL_RGBA;
  }

  @Override
  public int GL_SHORT() {
    return GL2.GL_SHORT;
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
  public int GL_TEXTURE_BINDING_2D() {
    return GL2.GL_TEXTURE_BINDING_2D;
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
  public int GL_TRIANGLE_STRIP() {
    return GL2.GL_TRIANGLE_STRIP;
  }

  @Override
  public int GL_TRIANGLE_FAN() {
    return GL2.GL_TRIANGLE_FAN;
  }

  @Override
  public int GL_TRUE() {
    return GL2.GL_TRUE;
  }

  @Override
  public int GL_UNSIGNED_BYTE() {
    return GL2.GL_UNSIGNED_BYTE;
  }

  @Override
  public int GL_UNSIGNED_SHORT() {
    return GL2.GL_UNSIGNED_SHORT;
  }

  @Override
  public int GL_UNSIGNED_SHORT_4_4_4_4() {
    return GL2.GL_UNSIGNED_SHORT_4_4_4_4;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_5_5_1() {
    return GL2.GL_UNSIGNED_SHORT_5_5_5_1;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_6_5() {
    return GL2.GL_UNSIGNED_SHORT_5_6_5;
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
    GLContext.getCurrentGL().getGL2().glAlphaFunc(func, ref);
  }

  @Override
  public void glBindTexture(int target, int texture) {
    GLContext.getCurrentGL().getGL2().glBindTexture(target, texture);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    GLContext.getCurrentGL().getGL2().glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glClear(int mask) {
    GLContext.getCurrentGL().getGL2().glClear(mask);
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    GLContext.getCurrentGL().getGL2().glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    GLContext.getCurrentGL().getGL2().glColorPointer(size, type, stride, pointer);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    GLContext.getCurrentGL().getGL2().glDeleteTextures(n, textures);
  }

  @Override
  public void glDisable(int cap) {
    GLContext.getCurrentGL().getGL2().glDisable(cap);
  }

  @Override
  public void glDisableClientState(int array) {
    GLContext.getCurrentGL().getGL2().glDisableClientState(array);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    GLContext.getCurrentGL().getGL2().glDrawArrays(mode, first, count);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int indices) {
    GLContext.getCurrentGL().getGL2().glDrawElements(mode, count, type, indices);
  }

  @Override
  public void glEnable(int cap) {
    GLContext.getCurrentGL().getGL2().glEnable(cap);
  }

  @Override
  public void glEnableClientState(int array) {
    GLContext.getCurrentGL().getGL2().glEnableClientState(array);
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    GLContext.getCurrentGL().getGL2().glGenTextures(n, textures);
  }

  @Override
  public int glGetError() {
    return GLContext.getCurrentGL().getGL2().glGetError();
  }

  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    GLContext.getCurrentGL().getGL2().glGetIntegerv(pname, params, offset);
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    GLContext.getCurrentGL().getGL2().glGetIntegerv(pname, params);
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return GLContext.getCurrentGL().getGL2().glIsEnabled(cap);
  }

  @Override
  public void glLoadIdentity() {
    GLContext.getCurrentGL().getGL2().glLoadIdentity();
  }

  @Override
  public void glMatrixMode(int mode) {
    GLContext.getCurrentGL().getGL2().glMatrixMode(mode);
  }

  @Override
  public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    GLContext.getCurrentGL().getGL2().glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    GLContext.getCurrentGL().getGL2().glTexCoordPointer(size, type, stride, pointer);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    GLContext.getCurrentGL().getGL2().glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    GLContext.getCurrentGL().getGL2().glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    GLContext.getCurrentGL().getGL2().glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  public void glTranslatef(float x, float y, float z) {
    GLContext.getCurrentGL().getGL2().glTranslatef(x, y, z);
  }

  @Override
  public void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    GLContext.getCurrentGL().getGL2().glVertexPointer(size, type, stride, pointer);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    GLContext.getCurrentGL().getGL2().glViewport(x, y, width, height);
  }
}
