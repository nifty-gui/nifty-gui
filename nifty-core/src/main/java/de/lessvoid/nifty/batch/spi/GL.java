package de.lessvoid.nifty.batch.spi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * OpenGL compatibility (non-core-profile) abstraction to make it easy for internal OpenGL-based rendering classes to
 * make direct OpenGL calls, rather than forcing external rendering implementations to reinvent the wheel. For an
 * example of how this abstraction may be useful, see {@link de.lessvoid.nifty.batch.OpenGLBatchRenderBackend}. This
 * interface is not intended (at this time) to provide a complete OpenGL abstraction, but only access to the methods
 * (and constants) currently in use by internal OpenGL-based rendering classes.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface GL {
  // OpenGL constants
  public int GL_ALPHA_TEST();
  public int GL_BLEND();
  public int GL_COLOR_ARRAY();
  public int GL_COLOR_BUFFER_BIT();
  public int GL_CULL_FACE();
  public int GL_DEPTH_TEST();
  public int GL_DST_COLOR();
  public int GL_FLOAT();
  public int GL_INVALID_ENUM();
  public int GL_INVALID_OPERATION();
  public int GL_INVALID_VALUE();
  public int GL_LIGHTING();
  public int GL_LINEAR();
  public int GL_MAX_TEXTURE_SIZE();
  public int GL_MODELVIEW();
  public int GL_NEAREST();
  public int GL_NO_ERROR();
  public int GL_NOTEQUAL();
  public int GL_ONE_MINUS_SRC_ALPHA();
  public int GL_OUT_OF_MEMORY();
  public int GL_PROJECTION();
  public int GL_RGBA();
  public int GL_SRC_ALPHA();
  public int GL_STACK_OVERFLOW();
  public int GL_STACK_UNDERFLOW();
  public int GL_TEXTURE_2D();
  public int GL_TEXTURE_COORD_ARRAY();
  public int GL_TEXTURE_MAG_FILTER();
  public int GL_TEXTURE_MIN_FILTER();
  public int GL_TRIANGLES();
  public int GL_UNSIGNED_BYTE();
  public int GL_VERTEX_ARRAY();
  public int GL_VIEWPORT();
  public int GL_ZERO();

  // OpenGL methods
  public void glAlphaFunc (int func, float ref);
  public void glBindTexture (int target, int texture);
  public void glBlendFunc (int sfactor, int dfactor);
  public void glClear (int mask);
  public void glClearColor (float red, float green, float blue, float alpha);
  public void glColorPointer (int size, int type, int stride, FloatBuffer pointer);
  public void glDeleteTextures (int n, IntBuffer textures);
  public void glDisable (int cap);
  public void glDisableClientState (int array);
  public void glDrawArrays (int mode, int first, int count);
  public void glEnable (int cap);
  public void glEnableClientState (int array);
  public void glGenTextures (int n, IntBuffer textures);
  public int glGetError();
  public void glGetIntegerv (int pname, IntBuffer params);
  public void glLoadIdentity();
  public void glMatrixMode (int mode);
  public void glOrthof (float left, float right, float bottom, float top, float zNear, float zFar);
  public void glTexCoordPointer (int size, int type, int stride, FloatBuffer pointer);
  public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels);
  public void glTexParameterf (int target, int pname, float param);
  public void glTexSubImage2D (int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels);
  public void glTranslatef (float x, float y, float z);
  public void glVertexPointer (int size, int type, int stride, FloatBuffer pointer);
  public void glViewport (int x, int y, int width, int height);
}
