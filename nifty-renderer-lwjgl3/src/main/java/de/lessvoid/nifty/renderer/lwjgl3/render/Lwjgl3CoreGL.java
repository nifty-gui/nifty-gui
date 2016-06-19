package de.lessvoid.nifty.renderer.lwjgl3.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;

import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

/**
 * Note: Requires OpenGL 3.2 or higher.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class Lwjgl3CoreGL extends Lwjgl3GL implements CoreGL {

  @Override
  public int GL_ACTIVE_TEXTURE() {
    return GL13.GL_ACTIVE_TEXTURE;
  }

  @Override
  public int GL_ARRAY_BUFFER() {
    return GL15.GL_ARRAY_BUFFER;
  }

  @Override
  public int GL_BITMAP() {
    return GL11.GL_BITMAP;
  }

  @Override
  public int GL_BGR() {
    return GL12.GL_BGR;
  }

  @Override
  public int GL_BGRA() {
    return GL12.GL_BGRA;
  }

  @Override
  public int GL_BLUE() {
    return GL11.GL_BLUE;
  }

  @Override
  public int GL_COLOR_INDEX() {
    return GL11.GL_COLOR_INDEX;
  }

  @Override
  public int GL_COMPILE_STATUS() {
    return GL20.GL_COMPILE_STATUS;
  }

  @Override
  public int GL_COMPRESSED_ALPHA() {
    return GL13.GL_COMPRESSED_ALPHA;
  }

  @Override
  public int GL_COMPRESSED_LUMINANCE() {
    return GL13.GL_COMPRESSED_LUMINANCE;
  }

  @Override
  public int GL_COMPRESSED_LUMINANCE_ALPHA() {
    return GL13.GL_COMPRESSED_LUMINANCE_ALPHA;
  }

  @Override
  public int GL_COMPRESSED_RGB() {
    return GL13.GL_COMPRESSED_RGB;
  }

  @Override
  public int GL_COMPRESSED_RGBA() {
    return GL13.GL_COMPRESSED_RGBA;
  }

  @Override
  public int GL_CURRENT_PROGRAM() {
    return GL20.GL_CURRENT_PROGRAM;
  }

  @Override
  public int GL_DYNAMIC_DRAW() {
    return GL15.GL_DYNAMIC_DRAW;
  }

  @Override
  public int GL_ELEMENT_ARRAY_BUFFER() {
    return GL15.GL_ELEMENT_ARRAY_BUFFER;
  }

  @Override
  public int GL_FRAGMENT_SHADER() {
    return GL20.GL_FRAGMENT_SHADER;
  }

  @Override
  public int GL_GEOMETRY_SHADER() {
    return GL32.GL_GEOMETRY_SHADER;
  }

  @Override
  public int GL_GREEN() {
    return GL11.GL_GREEN;
  }

  @Override
  public int GL_INT() {
    return GL11.GL_INT;
  }

  @Override
  public int GL_LINK_STATUS() {
    return GL20.GL_LINK_STATUS;
  }

  @Override
  public int GL_PRIMITIVE_RESTART() {
    return GL31.GL_PRIMITIVE_RESTART;
  }

  @Override
  public int GL_PRIMITIVE_RESTART_INDEX() {
    return GL31.GL_PRIMITIVE_RESTART_INDEX;
  }

  @Override
  public int GL_RED() {
    return GL11.GL_RED;
  }

  @Override
  public int GL_SAMPLER_BINDING() {
    return GL33.GL_SAMPLER_BINDING;
  }

  @Override
  public int GL_STATIC_DRAW() {
    return GL15.GL_STATIC_DRAW;
  }

  @Override
  public int GL_STREAM_DRAW() {
    return GL15.GL_STREAM_DRAW;
  }

  @Override
  public int GL_TEXTURE0() {
    return GL13.GL_TEXTURE0;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_X() {
    return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y() {
    return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z() {
    return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_X() {
    return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Y() {
    return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
  }

  @Override
  public int GL_TEXTURE_CUBE_MAP_POSITIVE_Z() {
    return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
  }

  @Override
  public int GL_UNSIGNED_BYTE_2_3_3_REV() {
    return GL12.GL_UNSIGNED_BYTE_2_3_3_REV;
  }

  @Override
  public int GL_UNSIGNED_BYTE_3_3_2() {
    return GL12.GL_UNSIGNED_BYTE_3_3_2;
  }

  @Override
  public int GL_UNSIGNED_INT() {
    return GL11.GL_UNSIGNED_INT;
  }

  @Override
  public int GL_UNSIGNED_INT_10_10_10_2() {
    return GL12.GL_UNSIGNED_INT_10_10_10_2;
  }

  @Override
  public int GL_UNSIGNED_INT_2_10_10_10_REV() {
    return GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
  }

  @Override
  public int GL_UNSIGNED_INT_8_8_8_8() {
    return GL12.GL_UNSIGNED_INT_8_8_8_8;
  }

  @Override
  public int GL_UNSIGNED_INT_8_8_8_8_REV() {
    return GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_5_6_5_REV() {
    return GL12.GL_UNSIGNED_SHORT_5_6_5_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_4_4_4_4_REV() {
    return GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV;
  }

  @Override
  public int GL_UNSIGNED_SHORT_1_5_5_5_REV() {
    return GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
  }

  @Override
  public int GL_VERTEX_SHADER() {
    return GL20.GL_VERTEX_SHADER;
  }

  @Override
  public int GL_WRITE_ONLY() {
    return GL15.GL_WRITE_ONLY;
  }

  @Override
  public void glActiveTexture(int texture) {
    GL13.glActiveTexture(texture);
  }

  @Override
  public void glAttachShader(int program, int shader) {
    GL20.glAttachShader(program, shader);
  }

  @Override
  public void glBindAttribLocation(int program, int index, String name) {
    GL20.glBindAttribLocation(program, index, name);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    GL15.glBindBuffer(target, buffer);
  }

  @Override
  public void glBindSampler(int unit, int sampler) {
    GL33.glBindSampler(unit, sampler);
  }

  @Override
  public void glBindVertexArray(int array) {
    GL30.glBindVertexArray(array);
  }

  @Override
  public void glBufferData(int target, IntBuffer data, int usage) {
    GL15.glBufferData(target, data, usage);
  }

  @Override
  public void glBufferData(int target, FloatBuffer data, int usage) {
    GL15.glBufferData(target, data, usage);
  }

  @Override
  public void glCompileShader(int shader) {
    GL20.glCompileShader(shader);
  }

  @Override
  public int glCreateProgram() {
    return GL20.glCreateProgram();
  }

  @Override
  public int glCreateShader(int type) {
    return GL20.glCreateShader(type);
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    GL15.glDeleteBuffers(buffers);
  }

  @Override
  public void glDeleteVertexArrays(int n, IntBuffer arrays) {
    GL30.glDeleteVertexArrays(arrays);
  }

  @Override
  public void glDrawArraysInstanced(int mode, int first, int count, int primcount) {
    GL31.glDrawArraysInstanced(mode, first, count, primcount);
  }

  @Override
  public void glEnableVertexAttribArray(int index) {
    GL20.glEnableVertexAttribArray(index);
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    GL15.glGenBuffers(buffers);
  }

  @Override
  public void glGenerateMipmap(int target) {
    GL30.glGenerateMipmap(target);
  }

  @Override
  public void glGenVertexArrays(int n, IntBuffer arrays) {
    GL30.glGenVertexArrays(arrays);
  }

  @Override
  public int glGetAttribLocation(int program, String name) {
    return GL20.glGetAttribLocation(program, name);
  }

  @Override
  public void glGetProgramiv(int program, int pname, IntBuffer params) {
    GL20.glGetProgramiv(program, pname, params);
  }

  @Override
  public String glGetProgramInfoLog(int program) {
    int logLength = GL20.glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH);
    return GL20.glGetProgramInfoLog(program, logLength);
  }

  @Override
  public void glGetShaderiv(int shader, int pname, IntBuffer params) {
    GL20.glGetShaderiv(shader, pname, params);
  }

  @Override
  public String glGetShaderInfoLog(int shader) {
    int logLength = GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH);
    return GL20.glGetShaderInfoLog(shader, logLength);
  }

  @Override
  public int glGetUniformLocation(int program, String name) {
    return GL20.glGetUniformLocation(program, name);
  }

  @Override
  public void glLinkProgram(int program) {
    GL20.glLinkProgram(program);
  }

  @Override
  public ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer) {
    return GL15.glMapBuffer(target, access, oldBuffer);
  }

  @Override
  public void glPrimitiveRestartIndex(int index) {
    GL31.glPrimitiveRestartIndex(index);
  }

  @Override
  public void glShaderSource(int shader, String string) {
    GL20.glShaderSource(shader, string);
  }

  @Override
  public void glUniform1(int location, FloatBuffer values) {
    GL20.glUniform1fv(location, values);
  }

  @Override
  public void glUniform1f(int location, float v0) {
    GL20.glUniform1f(location, v0);
  }

  @Override
  public void glUniform2f(int location, float v0, float v1) {
    GL20.glUniform2f(location, v0, v1);
  }

  @Override
  public void glUniform3f(int location, float v0, float v1, float v2) {
    GL20.glUniform3f(location, v0, v1, v2);
  }

  @Override
  public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
    GL20.glUniform4f(location, v0, v1, v2, v3);
  }

  @Override
  public void glUniform1i(int location, int v0) {
    GL20.glUniform1i(location, v0);
  }

  @Override
  public void glUniform2i(int location, int v0, int v1) {
    GL20.glUniform2i(location, v0, v1);
  }

  @Override
  public void glUniform3i(int location, int v0, int v1, int v2) {
    GL20.glUniform3i(location, v0, v1, v2);
  }

  @Override
  public void glUniform4i(int location, int v0, int v1, int v2, int v3) {
    GL20.glUniform4i(location, v0, v1, v2, v3);
  }

  @Override
  public void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
    GL20.glUniformMatrix4fv(location, transpose, matrices);
  }

  @Override
  public boolean glUnmapBuffer(int target) {
    return GL15.glUnmapBuffer(target);
  }

  @Override
  public void glUseProgram(int program) {
    GL20.glUseProgram(program);
  }

  @Override
  public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long offset) {
    GL20.glVertexAttribPointer(index, size, type, normalized, stride, offset);
  }
}
