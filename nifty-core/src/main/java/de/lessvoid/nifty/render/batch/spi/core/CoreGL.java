package de.lessvoid.nifty.render.batch.spi.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.lessvoid.nifty.render.batch.spi.GL;

/**
 * OpenGL Core Profile abstraction to make it easy for internal OpenGL-based rendering classes to make direct OpenGL
 * calls, rather than forcing external rendering implementations to reinvent the wheel. For an example of how this
 * abstraction may be useful, see {@link de.lessvoid.nifty.render.batch.core.BatchRenderBackendCoreProfileInternal}. This
 * interface is not intended (at this time) to provide a complete OpenGL abstraction, but only access to the methods
 * (and constants) currently in use by internal OpenGL-based rendering classes.
 *
 * Note: Requires OpenGL 3.2 or higher.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface CoreGL extends GL {
  // OpenGL constants
  public int GL_ACTIVE_TEXTURE();
  public int GL_ARRAY_BUFFER();
  public int GL_BITMAP();
  public int GL_BGR();
  public int GL_BGRA();
  public int GL_BLUE();
  public int GL_COLOR_INDEX();
  public int GL_COMPILE_STATUS();
  public int GL_COMPRESSED_ALPHA();
  public int GL_COMPRESSED_LUMINANCE();
  public int GL_COMPRESSED_LUMINANCE_ALPHA();
  public int GL_COMPRESSED_RGB();
  public int GL_COMPRESSED_RGBA();
  public int GL_CURRENT_PROGRAM();
  public int GL_DYNAMIC_DRAW();
  public int GL_ELEMENT_ARRAY_BUFFER();
  public int GL_FRAGMENT_SHADER();
  public int GL_GEOMETRY_SHADER();
  public int GL_GREEN();
  public int GL_INT();
  public int GL_LINK_STATUS();
  public int GL_PRIMITIVE_RESTART();
  public int GL_PRIMITIVE_RESTART_INDEX();
  public int GL_RED();
  public int GL_SAMPLER_BINDING();
  public int GL_STATIC_DRAW();
  public int GL_STREAM_DRAW();
  public int GL_TEXTURE0();
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_X();
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y();
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z();
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_X();
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Y();
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Z();
  public int GL_UNSIGNED_BYTE_2_3_3_REV();
  public int GL_UNSIGNED_BYTE_3_3_2();
  public int GL_UNSIGNED_INT();
  public int GL_UNSIGNED_INT_10_10_10_2();
  public int GL_UNSIGNED_INT_2_10_10_10_REV();
  public int GL_UNSIGNED_INT_8_8_8_8();
  public int GL_UNSIGNED_INT_8_8_8_8_REV();
  public int GL_UNSIGNED_SHORT_5_6_5_REV();
  public int GL_UNSIGNED_SHORT_4_4_4_4_REV();
  public int GL_UNSIGNED_SHORT_1_5_5_5_REV();
  public int GL_VERTEX_SHADER();
  public int GL_WRITE_ONLY();

  // OpenGL methods
  public void glActiveTexture(int texture);
  public void glAttachShader(int program, int shader);
  public void glBindAttribLocation(int program, int index, String name);
  public void glBindBuffer(int target, int buffer);
  public void glBindSampler(int unit, int sampler);
  public void glBindVertexArray(int array);
  public void glBufferData(int target, IntBuffer data, int usage);
  public void glBufferData(int target, FloatBuffer data, int usage);
  public void glCompileShader(int shader);
  public int glCreateProgram();
  public int glCreateShader(int type);
  public void glDeleteBuffers(int n, IntBuffer buffers);
  public void glDeleteVertexArrays(int n, IntBuffer arrays);
  public void glDrawArraysInstanced(int mode, int first, int count, int primcount);
  public void glEnableVertexAttribArray(int index);
  public void glGenBuffers(int n, IntBuffer buffers);
  public void glGenerateMipmap(int target);
  public void glGenVertexArrays(int n, IntBuffer arrays);
  public int glGetAttribLocation(int program, String name);
  public void glGetProgramiv(int program, int pname, IntBuffer params);
  public String glGetProgramInfoLog(int program);
  public void glGetShaderiv(int shader, int pname, IntBuffer params);
  public String glGetShaderInfoLog(int shader);
  public int glGetUniformLocation(int program, String name);
  public void glLinkProgram(int program);
  public ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer);
  public void glPrimitiveRestartIndex(int index);
  public void glShaderSource(int shader, String string);
  public void glUniform1(int location, FloatBuffer values);
  public void glUniform1f(int location, float v0);
  public void glUniform2f(int location, float v0, float v1);
  public void glUniform3f(int location, float v0, float v1, float v2);
  public void glUniform4f(int location, float v0, float v1, float v2, float v3);
  public void glUniform1i(int location, int v0);
  public void glUniform2i(int location, int v0, int v1);
  public void glUniform3i(int location, int v0, int v1, int v2);
  public void glUniform4i(int location, int v0, int v1, int v2, int v3);
  public void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices);
  public boolean glUnmapBuffer(int target);
  public void glUseProgram(int program);
  public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset);
}
