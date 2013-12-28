package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;


import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

/**
 * Helper class that represents a shader (actually as the combination of a vertex
 * and a fragment shader - what GL actually calls program).
 *
 * @author void
 */
public class CoreShader {
  private static final Logger log = Logger.getLogger(CoreShader.class.getName());
  private final int program;
  @Nonnull
  private final HashMap<String, Integer> parameter = new HashMap<String, Integer>();
  private final FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
  private final String[] attributes;

  /**
   * Create a new Shader.
   *
   * @return the new CoreShader instance
   */
  @Nonnull
  public static CoreShader newShader() {
    return new CoreShader();
  }

  /**
   * Create a new Shader with the given vertex attributes automatically bind to the generic attribute indizes in
   * ascending order beginning with 0. This method can be used when you want to control the vertex attribute binding
   * on your own.
   *
   * @param vertexAttributes the name of the vertex attribute. The first String gets generic attribute index 0. the
   *                         second String gets generic attribute index 1 and so on.
   * @return the CoreShader instance
   */
  @Nonnull
  public static CoreShader newShaderWithVertexAttributes(final String... vertexAttributes) {
    return new CoreShader(vertexAttributes);
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public int vertexShader(final String filename) {
    return vertexShader(getStream(filename));
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public int fragmentShader(final String filename) {
    return fragmentShader(getStream(filename));
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public int geometryShader(final String filename) {
    return geometryShader(getStream(filename));
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public int vertexShader(final File file) throws FileNotFoundException {
    return vertexShader(getStream(file));
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public int fragmentShader(final File file) throws FileNotFoundException {
    return fragmentShader(getStream(file));
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public int geometryShader(final File file) throws FileNotFoundException {
    return geometryShader(getStream(file));
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public int vertexShader(@Nonnull final InputStream source) {
    int shaderId = glCreateShader(GL_VERTEX_SHADER);
    checkGLError("glCreateShader(GL_VERTEX_SHADER)");
    prepareShader(source, shaderId);
    glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public int fragmentShader(@Nonnull final InputStream source) {
    int shaderId = glCreateShader(GL_FRAGMENT_SHADER);
    checkGLError("glCreateShader(GL_FRAGMENT_SHADER)");
    prepareShader(source, shaderId);
    glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public int geometryShader(@Nonnull final InputStream source) {
    int shaderId = glCreateShader(GL_GEOMETRY_SHADER);
    checkGLError("glCreateShader(GL_GEOMETRY_SHADER)");
    prepareShader(source, shaderId);
    glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public void vertexShader(final int shaderId, final String filename) {
    vertexShader(shaderId, getStream(filename));
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public void fragmentShader(final int shaderId, final String filename) {
    fragmentShader(shaderId, getStream(filename));
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename the filename of the shader
   */
  public void geometryShader(final int shaderId, final String filename) {
    geometryShader(shaderId, getStream(filename));
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public void vertexShader(final int shaderId, final File file) throws FileNotFoundException {
    vertexShader(shaderId, getStream(file));
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public void fragmentShader(final int shaderId, final File file) throws FileNotFoundException {
    fragmentShader(shaderId, getStream(file));
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file the file of the shader
   */
  public void geometryShader(final int shaderId, final File file) throws FileNotFoundException {
    geometryShader(shaderId, getStream(file));
  }

  /**
   * Attach the given vertex shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public void vertexShader(final int shaderId, @Nonnull final InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Attach the given fragment shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public void fragmentShader(final int shaderId, @Nonnull final InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Attach the given geometry shader file to this CoreShader. This will call glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source the file of the shader
   */
  public void geometryShader(final int shaderId, @Nonnull InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Link the Shader.
   */
  public void link() {
    for (int i = 0; i < attributes.length; i++) {
      glBindAttribLocation(program, i, attributes[i]);
      checkGLError("glBindAttribLocation (" + attributes[i] + ")");
    }

    glLinkProgram(program);
    checkGLError("glLinkProgram");

    if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
      log.warning("link error: " + glGetProgramInfoLog(program, 1024));
      checkGLError("glGetProgramInfoLog");
    }
    checkGLError("glGetProgram");
  }

  /**
   * Set the uniform with the given name to the given float value.
   *
   * @param name  name of the uniform to set
   * @param value the new float value
   */
  public void setUniformf(@Nonnull final String name, final float value) {
    glUniform1f(getLocation(name), value);
    checkGLError("glUniform1f");
  }

  /**
   * Set the uniform with the given name to the given value (two floats = vec2).
   *
   * @param name name of the uniform to set
   * @param v1   first component of the vec2
   * @param v2   second component of the vec2
   */
  public void setUniformf(@Nonnull final String name, final float v1, final float v2) {
    glUniform2f(getLocation(name), v1, v2);
    checkGLError("glUniform2f");
  }

  /**
   * Set the uniform with the given name to the given value (three floats = vec3).
   *
   * @param name name of the uniform to set
   * @param v1   first component of the vec3
   * @param v2   second component of the vec3
   * @param v3   third component of the vec3
   */
  public void setUniformf(@Nonnull final String name, final float v1, final float v2, final float v3) {
    glUniform3f(getLocation(name), v1, v2, v3);
    checkGLError("glUniform3f");
  }

  /**
   * Set the uniform with the given name to the given value (four floats = vec4).
   *
   * @param name name of the uniform to set
   * @param x    first component of the vec4
   * @param y    second component of the vec4
   * @param z    third component of the vec4
   * @param w    fourth component of the vec4
   */
  public void setUniformf(@Nonnull final String name, final float x, final float y, final float z, final float w) {
    glUniform4f(getLocation(name), x, y, z, w);
    checkGLError("glUniform4f");
  }

  /**
   * Set the uniform with the given name to the given int value.
   *
   * @param name name of the uniform to set
   * @param v1   the new int value
   */
  public void setUniformi(@Nonnull final String name, final int v1) {
    glUniform1i(getLocation(name), v1);
    checkGLError("glUniform1i");
  }

  /**
   * Set the uniform with the given name to the given int values (two values = ivec2).
   *
   * @param name name of the uniform to set
   * @param v1   the first int value of the ivec2
   * @param v2   the second int value of the ivec2
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2) {
    glUniform2i(getLocation(name), v1, v2);
    checkGLError("glUniform2i");
  }

  /**
   * Set the uniform with the given name to the given int values (three values = ivec3).
   *
   * @param name name of the uniform to set
   * @param v1   the first int value of the ivec3
   * @param v2   the second int value of the ivec3
   * @param v3   the third int value of the ivec3
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2, final int v3) {
    glUniform3i(getLocation(name), v1, v2, v3);
    checkGLError("glUniform3i");
  }

  /**
   * Set the uniform with the given name to the given int values (four values = ivec4).
   *
   * @param name name of the uniform to set
   * @param v1   the first int value of the ivec4
   * @param v2   the second int value of the ivec4
   * @param v3   the third int value of the ivec4
   * @param v4   the fourth int value of the ivec4
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2, final int v3, final int v4) {
    glUniform4i(getLocation(name), v1, v2, v3, v4);
    checkGLError("glUniform4i");
  }

  /**
   * Set the uniform mat4 with the given name to the given matrix (Matrix4f).
   *
   * @param name   the name of the uniform
   * @param matrix the Matrix4f to set
   */
  public void setUniformMatrix4f(@Nonnull final String name, @Nonnull final Matrix4f matrix) {
    matBuffer.clear();
    matrix.store(matBuffer);
    matBuffer.rewind();
    glUniformMatrix4(getLocation(name), false, matBuffer);
    checkGLError("glUniformMatrix4");
  }

  /**
   * Set the uniform mat4 with the given name to the given matrix (Matrix4f).
   *
   * @param name   the name of the uniform
   * @param matrix the Matrix4f to set
   */
  public void setUniformMatrix3f(@Nonnull final String name, @Nonnull final Matrix3f matrix) {
    matBuffer.clear();
    matrix.store(matBuffer);
    matBuffer.rewind();
    glUniformMatrix3(getLocation(name), false, matBuffer);
    checkGLError("glUniformMatrix3");
  }

  /**
   * Set the uniform float array with the given name to a new value.
   *
   * @param name   the name of the uniform
   * @param values the new float array to set
   */
  public void setUniformfArray(@Nonnull final String name, @Nonnull final float... values) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
    buffer.put(values);
    buffer.rewind();
    glUniform1(getLocation(name), buffer);
    checkGLError("glUniform1");
  }

  /**
   * Get the vertex attribute location of the vertex attribute with the given name.
   *
   * @param name the name of the vertex attribute
   * @return the generic vertex attribute index value
   */
  public int getAttribLocation(@Nonnull final String name) {
    int result = glGetAttribLocation(program, name);
    checkGLError("glGetAttribLocation");
    return result;
  }

  /**
   * You can manually bind the vertex attribute with the given name to the given specific index value. You'll need to
   * call this method before calling the link() method!
   *
   * @param name  the name of the vertex attribute
   * @param index the new index you want to give that vertex attribute
   */
  public void bindAttribLocation(final String name, final int index) {
    glBindAttribLocation(program, index, name);
    checkGLError("glBindAttribLocation");
  }

  /**
   * Activate this program.
   */
  public void activate() {
    glUseProgram(program);
    checkGLError("glUseProgram");
  }

  private CoreShader(final String... vertexAttributes) {
    this.attributes = vertexAttributes;
    this.program = glCreateProgram();
    checkGLError("glCreateProgram");
  }

  private int registerParameter(@Nonnull final String name) {
    int location = getUniform(name);
    parameter.put(name, location);
    return location;
  }

  private int getLocation(@Nonnull final String name) {
    Integer value = parameter.get(name);
    if (value == null) {
      return registerParameter(name);
    }
    return value;
  }

  private int getUniform(@Nonnull final String uniformName) {
    try {
      byte[] bytes = uniformName.getBytes("ISO-8859-1");
      ByteBuffer name = BufferUtils.createByteBuffer(bytes.length + 1);
      name.put(bytes);
      name.put((byte) 0x00);
      name.rewind();
      int result = glGetUniformLocation(program, name);
      checkGLError("glGetUniformLocation for [" + uniformName + "] failed");
      log.info(getLoggingPrefix() + "glUniformLocation for [" + uniformName + "] = [" + result + "]");
      return result;
    } catch (UnsupportedEncodingException e) {
      log.log(Level.WARNING, getLoggingPrefix() + e.getMessage(), e);
      return -1;
    }
  }

  private void prepareShader(@Nonnull final InputStream source, final int shaderId) {
    glShaderSource(shaderId, loadShader(source));
    checkGLError("glShaderSource");

    glCompileShader(shaderId);
    checkGLError("glCompileShader");

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
      log.warning("compile error: " + glGetShaderInfoLog(shaderId, 1024));
    }

    printLogInfo(shaderId);
    checkGLError(String.valueOf(shaderId));
  }

  private ByteBuffer loadShader(@Nonnull final InputStream source) {
    byte[] data = read(source);

    ByteBuffer result = BufferUtils.createByteBuffer(data.length);
    result.put(data);
    result.flip();
    return result;
  }

  private byte[] read(@Nonnull final InputStream dataStream) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    byte[] readBuffer = new byte[1024];
    int bytesRead;
    try {
      while ((bytesRead = dataStream.read(readBuffer)) > 0) {
        out.write(readBuffer, 0, bytesRead);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        dataStream.close();
      } catch (IOException ignored) {
      }
    }

    return out.toByteArray();
  }

  private void printLogInfo(final int obj) {
    ByteBuffer infoLog = BufferUtils.createByteBuffer(2048);
    IntBuffer lengthBuffer = BufferUtils.createIntBuffer(1);
    glGetShaderInfoLog(obj, lengthBuffer, infoLog);
    checkGLError("glGetShaderInfoLog");

    byte[] infoBytes = new byte[lengthBuffer.get()];
    infoLog.get(infoBytes);
    if (infoBytes.length == 0) {
      return;
    }
    try {
      log.info(getLoggingPrefix() + "Info log:\n" + new String(infoBytes, "ISO-8859-1"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    checkGLError("printLogInfo");
  }

  private void checkGLError(final String message) {
    CoreCheckGL.checkGLError(getLoggingPrefix() + message);
  }

  @Nonnull
  private String getLoggingPrefix() {
    return "[" + program + "] ";
  }

  @Nonnull
  private InputStream getStream(final File file) throws FileNotFoundException {
    log.fine("loading shader file [" + file + "]");
    return new ByteArrayInputStream(read(new FileInputStream(file)));
  }

  private InputStream getStream(final String filename) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
  }
}
