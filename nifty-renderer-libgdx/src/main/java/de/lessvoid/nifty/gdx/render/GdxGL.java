package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;

import de.lessvoid.nifty.render.batch.spi.GL;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class GdxGL implements GL {
  @Override
  public int GL_ALPHA() {
    return GL10.GL_ALPHA;
  }

  @Override
  public int GL_ALPHA_TEST() {
    return GL10.GL_ALPHA_TEST;
  }

  @Override
  public int GL_BLEND() {
    return GL10.GL_BLEND;
  }

  @Override
  public int GL_BLEND_DST() {
    return GL11.GL_BLEND_DST;
  }

  @Override
  public int GL_BLEND_SRC() {
    return GL11.GL_BLEND_SRC;
  }

  @Override
  public int GL_BYTE() {
    return GL10.GL_BYTE;
  }

  @Override
  public int GL_COLOR_ARRAY() {
    return GL10.GL_COLOR_ARRAY;
  }

  @Override
  public int GL_COLOR_BUFFER_BIT() {
    return GL10.GL_COLOR_BUFFER_BIT;
  }

  @Override
  public int GL_CULL_FACE() {
    return GL10.GL_CULL_FACE;
  }

  @Override
  public int GL_DEPTH_TEST() {
    return GL10.GL_DEPTH_TEST;
  }

  @Override
  public int GL_DST_COLOR() {
    return GL10.GL_DST_COLOR;
  }

  @Override
  public int GL_FALSE() {
    return GL10.GL_FALSE;
  }

  @Override
  public int GL_FLOAT() {
    return GL10.GL_FLOAT;
  }

  @Override
  public int GL_INVALID_ENUM() {
    return GL10.GL_INVALID_ENUM;
  }

  @Override
  public int GL_INVALID_OPERATION() {
    return GL10.GL_INVALID_OPERATION;
  }

  @Override
  public int GL_INVALID_VALUE() {
    return GL10.GL_INVALID_VALUE;
  }

  @Override
  public int GL_LIGHTING() {
    return GL10.GL_LIGHTING;
  }

  @Override
  public int GL_LINEAR() {
    return GL10.GL_LINEAR;
  }

  @Override
  public int GL_LINEAR_MIPMAP_LINEAR() {
    return GL10.GL_LINEAR_MIPMAP_LINEAR;
  }

  @Override
  public int GL_LINEAR_MIPMAP_NEAREST() {
    return GL10.GL_LINEAR_MIPMAP_NEAREST;
  }

  @Override
  public int GL_LUMINANCE() {
    return GL10.GL_LUMINANCE;
  }

  @Override
  public int GL_LUMINANCE_ALPHA() {
    return GL10.GL_LUMINANCE_ALPHA;
  }

  @Override
  public int GL_MAX_TEXTURE_SIZE() {
    return GL10.GL_MAX_TEXTURE_SIZE;
  }

  @Override
  public int GL_MODELVIEW() {
    return GL10.GL_MODELVIEW;
  }

  @Override
  public int GL_NEAREST() {
    return GL10.GL_NEAREST;
  }

  @Override
  public int GL_NEAREST_MIPMAP_LINEAR() {
    return GL10.GL_NEAREST_MIPMAP_LINEAR;
  }

  @Override
  public int GL_NEAREST_MIPMAP_NEAREST() {
    return GL10.GL_NEAREST_MIPMAP_NEAREST;
  }

  @Override
  public int GL_NO_ERROR() {
    return GL10.GL_NO_ERROR;
  }

  @Override
  public int GL_NOTEQUAL() {
    return GL10.GL_NOTEQUAL;
  }

  @Override
  public int GL_ONE_MINUS_SRC_ALPHA() {
    return GL10.GL_ONE_MINUS_SRC_ALPHA;
  }

  @Override
  public int GL_OUT_OF_MEMORY() {
    return GL10.GL_OUT_OF_MEMORY;
  }

  @Override
  public int GL_POINTS() {
    return GL10.GL_POINTS;
  }

  @Override
  public int GL_PROJECTION() {
    return GL10.GL_PROJECTION;
  }

  @Override
  public int GL_RGB() {
    return GL10.GL_RGB;
  }

  @Override
  public int GL_RGBA() {
    return GL10.GL_RGBA;
  }

  @Override
  public int GL_SHORT() {
    return GL10.GL_SHORT;
  }

  @Override
  public int GL_SRC_ALPHA() {
    return GL10.GL_SRC_ALPHA;
  }

  @Override
  public int GL_STACK_OVERFLOW() {
    return GL10.GL_STACK_OVERFLOW;
  }

  @Override
  public int GL_STACK_UNDERFLOW() {
    return GL10.GL_STACK_UNDERFLOW;
  }

  @Override
  public int GL_TEXTURE_2D() {
    return GL10.GL_TEXTURE_2D;
  }

  @Override
  public int GL_TEXTURE_BINDING_2D() {
    return GL11.GL_TEXTURE_BINDING_2D;
  }

  @Override
  public int GL_TEXTURE_COORD_ARRAY() {
    return GL10.GL_TEXTURE_COORD_ARRAY;
  }

  @Override
  public int GL_TEXTURE_MAG_FILTER() {
    return GL10.GL_TEXTURE_MAG_FILTER;
  }

  @Override
  public int GL_TEXTURE_MIN_FILTER() {
    return GL10.GL_TEXTURE_MIN_FILTER;
  }

  @Override
  public int GL_TRIANGLES() {
    return GL10.GL_TRIANGLES;
  }

  @Override
  public int GL_TRIANGLE_STRIP() {
    return GL10.GL_TRIANGLE_STRIP;
  }

  @Override
  public int GL_TRIANGLE_FAN() {
    return GL10.GL_TRIANGLE_FAN;
  }

  @Override
  public int GL_TRUE() {
    return GL10.GL_TRUE;
  }

  @Override
  public int GL_UNSIGNED_BYTE() {
    return GL10.GL_UNSIGNED_BYTE;
  }

  @Override
  public int GL_UNSIGNED_SHORT() {
    return GL10.GL_UNSIGNED_SHORT;
  }

  @Override
  public int GL_UNSIGNED_SHORT_4_4_4_4() {
    return GL10.GL_UNSIGNED_SHORT_4_4_4_4;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_5_5_1() {
    return GL10.GL_UNSIGNED_SHORT_5_5_5_1;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_6_5() {
    return GL10.GL_UNSIGNED_SHORT_5_6_5;
  }

  @Override
  public int GL_VERTEX_ARRAY() {
    return GL10.GL_VERTEX_ARRAY;
  }

  @Override
  public int GL_VIEWPORT() {
    return GL11.GL_VIEWPORT;
  }

  @Override
  public int GL_ZERO() {
    return GL10.GL_ZERO;
  }

  @Override
  public void glAlphaFunc(int func, float ref) {
    Gdx.gl10.glAlphaFunc(func, ref);
  }

  @Override
  public void glBindTexture(int target, int texture) {
    Gdx.gl10.glBindTexture(target, texture);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    Gdx.gl10.glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glClear(int mask) {
    Gdx.gl10.glClear(mask);
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    Gdx.gl10.glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glColorPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glColorPointer(size, type, stride, pointer);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    Gdx.gl10.glDeleteTextures(n, textures);
  }

  @Override
  public void glDisable(int cap) {
    Gdx.gl10.glDisable(cap);
  }

  @Override
  public void glDisableClientState(int array) {
    Gdx.gl10.glDisableClientState(array);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    Gdx.gl10.glDrawArrays(mode, first, count);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int indices) {
    Gdx.gl11.glDrawElements(mode, count, type, indices);
  }

  @Override
  public void glEnable(int cap) {
    Gdx.gl10.glEnable(cap);
  }

  @Override
  public void glEnableClientState(int array) {
    Gdx.gl10.glEnableClientState(array);
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    Gdx.gl10.glGenTextures(n, textures);
  }

  @Override
  public int glGetError() {
    return Gdx.gl10.glGetError();
  }

  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    Gdx.gl10.glGetIntegerv(pname, params, offset);
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    Gdx.gl10.glGetIntegerv(pname, params);
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return Gdx.gl11.glIsEnabled(cap);
  }

  @Override
  public void glLoadIdentity() {
    Gdx.gl10.glLoadIdentity();
  }

  @Override
  public void glMatrixMode(int mode) {
    Gdx.gl10.glMatrixMode(mode);
  }

  @Override
  public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    Gdx.gl10.glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glTexCoordPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glTexCoordPointer(size, type, stride, pointer);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
    Gdx.gl10.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    Gdx.gl10.glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    Gdx.gl11.glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
    Gdx.gl10.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @Override
  public void glTranslatef(float x, float y, float z) {
    Gdx.gl10.glTranslatef(x, y, z);
  }

  @Override
  public void glVertexPointer(int size, int type, int stride, FloatBuffer pointer) {
    Gdx.gl10.glVertexPointer(size, type, stride, pointer);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    Gdx.gl10.glViewport(x, y, width, height);
  }
}
