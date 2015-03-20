package de.lessvoid.nifty.renderer.jogl.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL3ES3;
import com.jogamp.opengl.GLContext;

import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

/**
 * Note: Requires OpenGL 3.2 or higher.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglCoreGL extends JoglGL implements CoreGL {

  @Override
  public int GL_ACTIVE_TEXTURE() {
    return GL.GL_ACTIVE_TEXTURE;
  }

  @Override
  public int GL_ARRAY_BUFFER() {
    return GL.GL_ARRAY_BUFFER;
  }

  @Override
  public int GL_BITMAP() {
    return GL2.GL_BITMAP;
  }

  @Override
  public int GL_BGR() {
    return GL2.GL_BGR;
  }

  @Override
  public int GL_BGRA() {
    return GL.GL_BGRA;
  }

  @Override
  public int GL_BLUE() {
    return GL2.GL_BLUE;
  }

  @Override
  public int GL_COLOR_INDEX() {
    return GL2.GL_COLOR_INDEX;
  }

  @Override
  public int GL_COMPILE_STATUS() {
    return GL2.GL_COMPILE_STATUS;
  }

  @Override
  public int GL_COMPRESSED_ALPHA() {
    return GL2.GL_COMPRESSED_ALPHA;
  }

  @Override
  public int GL_COMPRESSED_LUMINANCE() {
    return GL2.GL_COMPRESSED_LUMINANCE;
  }

  @Override
  public int GL_COMPRESSED_LUMINANCE_ALPHA() {
    return GL2.GL_COMPRESSED_LUMINANCE_ALPHA;
  }

  @Override
  public int GL_COMPRESSED_RGB() {
    return GL2.GL_COMPRESSED_RGB;
  }

  @Override
  public int GL_COMPRESSED_RGBA() {
    return GL2.GL_COMPRESSED_RGBA;
  }

  @Override
  public int GL_CURRENT_PROGRAM() {
    return GL2.GL_CURRENT_PROGRAM;
  }

  @Override
  public int GL_DYNAMIC_DRAW() {
    return GL.GL_DYNAMIC_DRAW;
  }

  @Override
  public int GL_ELEMENT_ARRAY_BUFFER() {
    return GL.GL_ELEMENT_ARRAY_BUFFER;
  }

  @Override
  public int GL_FRAGMENT_SHADER() {
    return GL2.GL_FRAGMENT_SHADER;
  }

  @Override
  public int GL_GEOMETRY_SHADER() {
    return GL3.GL_GEOMETRY_SHADER;
  }

  @Override
  public int GL_GREEN() {
    return GL2.GL_GREEN;
  }

  @Override
  public int GL_INT() {
    return GL2.GL_INT;
  }

  @Override
  public int GL_LINK_STATUS() {
    return GL2.GL_LINK_STATUS;
  }

  @Override
  public int GL_PRIMITIVE_RESTART() {
    return GL2.GL_PRIMITIVE_RESTART;
  }

  @Override
  public int GL_PRIMITIVE_RESTART_INDEX() {
    return GL2.GL_PRIMITIVE_RESTART_INDEX;
  }

  @Override
  public int GL_RED() {
    return GL2.GL_RED;
  }

  @Override
  public int GL_SAMPLER_BINDING() {
    return GL3ES3.GL_SAMPLER_BINDING;
  }

  @Override
  public int GL_STATIC_DRAW() {
    return GL.GL_STATIC_DRAW;
  }

  @Override
  public int GL_STREAM_DRAW() {
    return GL2.GL_STREAM_DRAW;
  }

  @Override
  public int GL_TEXTURE0() {
    return GL.GL_TEXTURE0;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_X() {
    return GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y() {
    return GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z() {
    return GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_X() {
    return GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Y() {
    return GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Z() {
    return GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
  }

  @Override
  public int GL_UNSIGNED_BYTE_2_3_3_REV() {
    return GL2.GL_UNSIGNED_BYTE_2_3_3_REV;
  }

  @Override
  public int GL_UNSIGNED_BYTE_3_3_2() {
    return GL2.GL_UNSIGNED_BYTE_3_3_2;
  }

  @Override
  public int GL_UNSIGNED_INT() {
    return GL.GL_UNSIGNED_INT;
  }

  @Override
  public int GL_UNSIGNED_INT_10_10_10_2() {
    return GL2.GL_UNSIGNED_INT_10_10_10_2;
  }

  @Override
  public int GL_UNSIGNED_INT_2_10_10_10_REV() {
    return GL2.GL_UNSIGNED_INT_2_10_10_10_REV;
  }

  @Override
  public int GL_UNSIGNED_INT_8_8_8_8() {
    return GL2.GL_UNSIGNED_INT_8_8_8_8;
  }

  @Override
  public int GL_UNSIGNED_INT_8_8_8_8_REV() {
    return GL2.GL_UNSIGNED_INT_8_8_8_8_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_6_5_REV() {
    return GL2.GL_UNSIGNED_SHORT_5_6_5_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_4_4_4_4_REV() {
    return GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_1_5_5_5_REV() {
    return GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV;
  }

  @Override
  public int GL_VERTEX_SHADER() {
    return GL2.GL_VERTEX_SHADER;
  }

  @Override
  public int GL_WRITE_ONLY() {
    return GL.GL_WRITE_ONLY;
  }

  @Override
  public void glActiveTexture(int texture) {
    GLContext.getCurrentGL().getGL3().glActiveTexture(texture);
  }

  @Override
  public void glAttachShader(int program, int shader) {
    GLContext.getCurrentGL().getGL3().glAttachShader(program, shader);
  }

  @Override
  public void glBindAttribLocation(int program, int index, String name) {
    GLContext.getCurrentGL().getGL3().glBindAttribLocation(program, index, name);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    GLContext.getCurrentGL().getGL3().glBindBuffer(target, buffer);
  }

  @Override
  public void glBindSampler(int unit, int sampler) {
    GLContext.getCurrentGL().getGL3ES3().glBindSampler(unit, sampler);
  }

  @Override
  public void glBindVertexArray(int array) {
    GLContext.getCurrentGL().getGL3().glBindVertexArray(array);
  }

  @Override
  public void glBufferData(int target, IntBuffer data, int usage) {
    GLContext.getCurrentGL().getGL3().glBufferData(target, data.remaining() * 4, data, usage);
  }

  @Override
  public void glBufferData(int target, FloatBuffer data, int usage) {
    GLContext.getCurrentGL().getGL3().glBufferData(target, data.remaining() * 4, data, usage);
  }

  @Override
  public void glCompileShader(int shader) {
    GLContext.getCurrentGL().getGL3().glCompileShader(shader);
  }

  @Override
  public int glCreateProgram() {
    return GLContext.getCurrentGL().getGL3().glCreateProgram();
  }

  @Override
  public int glCreateShader(int type) {
    return GLContext.getCurrentGL().getGL3().glCreateShader(type);
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    GLContext.getCurrentGL().getGL3().glDeleteBuffers(n, buffers);
  }

  @Override
  public void glDeleteVertexArrays(int n, IntBuffer arrays) {
    GLContext.getCurrentGL().getGL3().glDeleteVertexArrays(n, arrays);
  }

  @Override
  public void glDrawArraysInstanced(int mode, int first, int count, int primcount) {
    GLContext.getCurrentGL().getGL3().glDrawArraysInstanced(mode, first, count, primcount);
  }

  @Override
  public void glEnableVertexAttribArray(int index) {
    GLContext.getCurrentGL().getGL3().glEnableVertexAttribArray(index);
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    GLContext.getCurrentGL().getGL3().glGenBuffers(n, buffers);
  }

  @Override
  public void glGenerateMipmap(int target) {
    GLContext.getCurrentGL().getGL3().glGenerateMipmap(target);
  }

  @Override
  public void glGenVertexArrays(int n, IntBuffer arrays) {
    GLContext.getCurrentGL().getGL3().glGenVertexArrays(n, arrays);
  }

  @Override
  public int glGetAttribLocation(int program, String name) {
    return GLContext.getCurrentGL().getGL3().glGetAttribLocation(program, name);
  }

  @Override
  public void glGetProgramiv(int program, int pname, IntBuffer params) {
    GLContext.getCurrentGL().getGL3().glGetProgramiv(program, pname, params);
  }

  @Override
  public String glGetProgramInfoLog(int program) {
    int[] logLength = new int[1];
    GLContext.getCurrentGL().getGL3().glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
    byte[] log = new byte[logLength[0]];
    GLContext.getCurrentGL().getGL3().glGetProgramInfoLog(program, logLength[0], null, 0, log, 0);
    return new String(log);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, IntBuffer params) {
    GLContext.getCurrentGL().getGL3().glGetShaderiv(shader, pname, params);
  }

  @Override
  public String glGetShaderInfoLog(int shader) {
    int[] logLength = new int[1];
    GLContext.getCurrentGL().getGL3().glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
    byte[] log = new byte[logLength[0]];
    if (log.length > 0) {
      GLContext.getCurrentGL().getGL3().glGetShaderInfoLog(shader, logLength[0], null, 0, log, 0);
    }
    return new String(log);
  }

  @Override
  public int glGetUniformLocation(int program, String name) {
    return GLContext.getCurrentGL().getGL3().glGetUniformLocation(program, name);
  }

  @Override
  public void glLinkProgram(int program) {
    GLContext.getCurrentGL().getGL3().glLinkProgram(program);
  }

  @Override
  public ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer) {
    return GLContext.getCurrentGL().getGL3().glMapBuffer(target, access);
  }

  @Override
  public void glPrimitiveRestartIndex(int index) {
    GLContext.getCurrentGL().getGL3().glPrimitiveRestartIndex(index);
  }

  @Override
  public void glShaderSource(int shader, String string) {
    String[] sources = new String[]{string};
    int[] sourceLengths = new int[]{sources[0].length()};
    GLContext.getCurrentGL().getGL3().glShaderSource(shader, sources.length, sources, sourceLengths, 0);
  }

  @Override
  public void glUniform1(int location, FloatBuffer values) {
    GLContext.getCurrentGL().getGL3().glUniform1fv(location, values.remaining(), values);
  }

  @Override
  public void glUniform1f(int location, float v0) {
    GLContext.getCurrentGL().getGL3().glUniform1f(location, v0);
  }

  @Override
  public void glUniform2f(int location, float v0, float v1) {
    GLContext.getCurrentGL().getGL3().glUniform2f(location, v0, v1);
  }

  @Override
  public void glUniform3f(int location, float v0, float v1, float v2) {
    GLContext.getCurrentGL().getGL3().glUniform3f(location, v0, v1, v2);
  }

  @Override
  public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
    GLContext.getCurrentGL().getGL3().glUniform4f(location, v0, v1, v2, v3);
  }

  @Override
  public void glUniform1i(int location, int v0) {
    GLContext.getCurrentGL().getGL3().glUniform1i(location, v0);
  }

  @Override
  public void glUniform2i(int location, int v0, int v1) {
    GLContext.getCurrentGL().getGL3().glUniform2i(location, v0, v1);
  }

  @Override
  public void glUniform3i(int location, int v0, int v1, int v2) {
    GLContext.getCurrentGL().getGL3().glUniform3i(location, v0, v1, v2);
  }

  @Override
  public void glUniform4i(int location, int v0, int v1, int v2, int v3) {
    GLContext.getCurrentGL().getGL3().glUniform4i(location, v0, v1, v2, v3);
  }

  @Override
  public void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
    GLContext.getCurrentGL().getGL3().glUniformMatrix4fv(location, 1, transpose, matrices);
  }

  @Override
  public boolean glUnmapBuffer(int target) {
    return GLContext.getCurrentGL().getGL3().glUnmapBuffer(target);
  }

  @Override
  public void glUseProgram(int program) {
    GLContext.getCurrentGL().getGL3().glUseProgram(program);
  }

  @Override
  public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset) {
    GLContext.getCurrentGL().getGL3().glVertexAttribPointer(index, size, type, normalized, stride, offset);
  }
}
