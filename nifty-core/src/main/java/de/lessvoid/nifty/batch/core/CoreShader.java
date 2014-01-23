package de.lessvoid.nifty.batch.core;

import de.lessvoid.nifty.batch.CheckGL;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.core.CoreGL;
import de.lessvoid.nifty.batch.spi.core.CoreMatrix4f;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an abstraction of an OpenGL shader. It is actually a combination of a vertex shader & a fragment shader,
 * which in OpenGL is referred to as a 'program'.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreShader {
  @Nonnull
  private static final Logger log = Logger.getLogger(CoreShader.class.getName());
  @Nonnull
  private final CoreGL gl;
  @Nonnull
  private final BufferFactory bufferFactory;
  @Nonnull
  private final HashMap<String, Integer> parameter = new HashMap<String, Integer>();
  @Nonnull
  private final FloatBuffer matrixBuffer;
  private final String[] attributes;
  private final int program;
  private IntBuffer params;

  /**
   * Creates a new Shader.
   *
   * @return The new CoreShader instance.
   */
  @Nonnull
  public static CoreShader createShader(@Nonnull final CoreGL gl, @Nonnull final BufferFactory bufferFactory) {
    return new CoreShader(gl, bufferFactory);
  }

  /**
   * Creates a new Shader with the specified vertex attributes that automatically binds to the generic attribute
   * indices in ascending order beginning with 0. This method can be used when you want to control the vertex attribute
   * binding on your own.
   *
   * @param vertexAttributes The name of the vertex attribute. The first String gets generic attribute index 0. The
   *                         second String gets generic attribute index 1 and so on.
   * @return The CoreShader instance.
   */
  @Nonnull
  public static CoreShader createShaderWithVertexAttributes(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final String... vertexAttributes) {
    return new CoreShader(gl, bufferFactory, vertexAttributes);
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source, and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public int vertexShader(@Nonnull final String filename) {
    return vertexShader(getStream(filename));
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public int fragmentShader(@Nonnull final String filename) {
    return fragmentShader(getStream(filename));
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public int geometryShader(@Nonnull final String filename) {
    return geometryShader(getStream(filename));
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source, and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public int vertexShader(@Nonnull final File file) throws FileNotFoundException {
    return vertexShader(getStream(file));
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public int fragmentShader(@Nonnull final File file) throws FileNotFoundException {
    return fragmentShader(getStream(file));
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public int geometryShader(@Nonnull final File file) throws FileNotFoundException {
    return geometryShader(getStream(file));
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source, and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public int vertexShader(@Nonnull final InputStream source) {
    int shaderId = gl.glCreateShader(gl.GL_VERTEX_SHADER());
    checkGLError("glCreateShader(GL_VERTEX_SHADER)");
    prepareShader(source, shaderId);
    gl.glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public int fragmentShader(@Nonnull final InputStream source) {
    int shaderId = gl.glCreateShader(gl.GL_FRAGMENT_SHADER());
    checkGLError("glCreateShader(GL_FRAGMENT_SHADER)");
    prepareShader(source, shaderId);
    gl.glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source, and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public int geometryShader(@Nonnull final InputStream source) {
    int shaderId = gl.glCreateShader(gl.GL_GEOMETRY_SHADER());
    checkGLError("glCreateShader(GL_GEOMETRY_SHADER)");
    prepareShader(source, shaderId);
    gl.glAttachShader(program, shaderId);
    checkGLError("glAttachShader");
    return shaderId;
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public void vertexShader(final int shaderId, @Nonnull final String filename) {
    vertexShader(shaderId, getStream(filename));
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public void fragmentShader(final int shaderId, @Nonnull final String filename) {
    fragmentShader(shaderId, getStream(filename));
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param filename The filename of the shader.
   */
  public void geometryShader(final int shaderId, @Nonnull final String filename) {
    geometryShader(shaderId, getStream(filename));
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public void vertexShader(final int shaderId, @Nonnull final File file) throws FileNotFoundException {
    vertexShader(shaderId, getStream(file));
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public void fragmentShader(final int shaderId, @Nonnull final File file) throws FileNotFoundException {
    fragmentShader(shaderId, getStream(file));
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param file The file of the shader.
   */
  public void geometryShader(final int shaderId, @Nonnull final File file) throws FileNotFoundException {
    geometryShader(shaderId, getStream(file));
  }

  /**
   * Attaches the specified vertex shader file to this CoreShader. This calls glCreateShader(), loads and compiles the
   * shader source and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public void vertexShader(final int shaderId, @Nonnull final InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Attaches the specified fragment shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public void fragmentShader(final int shaderId, @Nonnull final InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Attaches the specified geometry shader file to this CoreShader. This calls glCreateShader(), loads and compiles
   * the shader source and finally attaches the shader.
   *
   * @param source The file of the shader.
   */
  public void geometryShader(final int shaderId, @Nonnull InputStream source) {
    prepareShader(source, shaderId);
  }

  /**
   * Links the Shader.
   */
  public void link() {
    for (int i = 0; i < attributes.length; i++) {
      gl.glBindAttribLocation(program, i, attributes[i]);
      checkGLError("glBindAttribLocation (" + attributes[i] + ")");
    }

    gl.glLinkProgram(program);
    checkGLError("glLinkProgram");

    params.clear();
    gl.glGetProgramiv(program, gl.GL_LINK_STATUS(), params);

    if (params.get(0) != gl.GL_TRUE()) {
      log.warning("link error: " + gl.glGetProgramInfoLog(program));
      checkGLError("glGetProgramInfoLog");
    }
    checkGLError("glGetProgram");
  }

  /**
   * Sets the uniform with the specified name to the specified float value.
   *
   * @param name  The name of the uniform to set.
   * @param value The new float value.
   */
  public void setUniformf(@Nonnull final String name, final float value) {
    gl.glUniform1f(getLocation(name), value);
    checkGLError("glUniform1f");
  }

  /**
   * Sets the uniform with the specified name to the specified value (two floats = vec2).
   *
   * @param name The name of the uniform to set.
   * @param v1   The first component of the vec2.
   * @param v2   The second component of the vec2.
   */
  public void setUniformf(@Nonnull final String name, final float v1, final float v2) {
    gl.glUniform2f(getLocation(name), v1, v2);
    checkGLError("glUniform2f");
  }

  /**
   * Sets the uniform with the specified name to the specified value (three floats = vec3).
   *
   * @param name The name of the uniform to set.
   * @param v1   The first component of the vec3.
   * @param v2   The second component of the vec3.
   * @param v3   The third component of the vec3.
   */
  public void setUniformf(@Nonnull final String name, final float v1, final float v2, final float v3) {
    gl.glUniform3f(getLocation(name), v1, v2, v3);
    checkGLError("glUniform3f");
  }

  /**
   * Sets the uniform with the specified name to the specified value (four floats = vec4).
   *
   * @param name The name of the uniform to set.
   * @param x    The first component of the vec4.
   * @param y    The second component of the vec4.
   * @param z    The third component of the vec4.
   * @param w    The fourth component of the vec4.
   */
  public void setUniformf(@Nonnull final String name, final float x, final float y, final float z, final float w) {
    gl.glUniform4f(getLocation(name), x, y, z, w);
    checkGLError("glUniform4f");
  }

  /**
   * Sets the uniform with the specified name to the specified int value.
   *
   * @param name The name of the uniform to set.
   * @param v1   The new int value.
   */
  public void setUniformi(@Nonnull final String name, final int v1) {
    gl.glUniform1i(getLocation(name), v1);
    checkGLError("glUniform1i");
  }

  /**
   * Sets the uniform with the specified name to the specified int values (two values = ivec2).
   *
   * @param name The name of the uniform to set.
   * @param v1   The first int value of the ivec2.
   * @param v2   The second int value of the ivec2.
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2) {
    gl.glUniform2i(getLocation(name), v1, v2);
    checkGLError("glUniform2i");
  }

  /**
   * Sets the uniform with the specified name to the specified int values (three values = ivec3).
   *
   * @param name The name of the uniform to set.
   * @param v1   The first int value of the ivec3.
   * @param v2   The second int value of the ivec3.
   * @param v3   The third int value of the ivec3.
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2, final int v3) {
    gl.glUniform3i(getLocation(name), v1, v2, v3);
    checkGLError("glUniform3i");
  }

  /**
   * Sets the uniform with the specified name to the specified int values (four values = ivec4).
   *
   * @param name The name of the uniform to set.
   * @param v1   The first int value of the ivec4.
   * @param v2   The second int value of the ivec4.
   * @param v3   The third int value of the ivec4.
   * @param v4   The fourth int value of the ivec4.
   */
  public void setUniformi(@Nonnull final String name, final int v1, final int v2, final int v3, final int v4) {
    gl.glUniform4i(getLocation(name), v1, v2, v3, v4);
    checkGLError("glUniform4i");
  }

  /**
   * Sets the uniform mat4 with the specified name to the specified matrix (Matrix4f).
   *
   * @param name   The name of the uniform.
   * @param matrix The matrix4f to set.
   */
  public void setUniformMatrix4f(@Nonnull final String name, @Nonnull final CoreMatrix4f matrix) {
    matrixBuffer.clear();
    matrix.store(matrixBuffer);
    matrixBuffer.rewind();
    gl.glUniformMatrix4(getLocation(name), false, matrixBuffer);
    checkGLError("glUniformMatrix4");
  }

  /**
   * Sets the uniform float array with the specified name to a new value.
   *
   * @param name   The name of the uniform.
   * @param values The new float array to set.
   */
  public void setUniformfArray(@Nonnull final String name, @Nonnull final float... values) {
    FloatBuffer buffer = bufferFactory.createNativeOrderedFloatBuffer(values.length);
    buffer.put(values);
    buffer.rewind();
    gl.glUniform1(getLocation(name), buffer);
    checkGLError("glUniform1");
  }

  /**
   * Gets the location of the vertex attribute with the specified name.
   *
   * @param name The name of the vertex attribute.
   *
   * @return The generic vertex attribute index value
   */
  public int getAttribLocation(@Nonnull final String name) {
    int result = gl.glGetAttribLocation(program, name);
    checkGLError("glGetAttribLocation");
    return result;
  }

  /**
   * Manually binds the vertex attribute with the specified name to the specified index value. You'll need to call this
   * method before calling the {@link #link()} method!
   *
   * @param name  The name of the vertex attribute.
   * @param index The new index you want to give the vertex attribute.
   */
  public void bindAttribLocation(@Nonnull final String name, final int index) {
    gl.glBindAttribLocation(program, index, name);
    checkGLError("glBindAttribLocation");
  }

  /**
   * Activates this program.
   */
  public void activate() {
    gl.glUseProgram(program);
    checkGLError("glUseProgram");
  }

  private CoreShader(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final String... vertexAttributes) {
    this.gl = gl;
    this.bufferFactory = bufferFactory;
    this.attributes = vertexAttributes;
    this.matrixBuffer = bufferFactory.createNativeOrderedFloatBuffer(16);
    this.program = gl.glCreateProgram();
    params = bufferFactory.createNativeOrderedIntBuffer(1);
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
    int result = gl.glGetUniformLocation(program, uniformName);
    checkGLError("glGetUniformLocation for [" + uniformName + "] failed");
    log.info(getLoggingPrefix() + "glUniformLocation for [" + uniformName + "] = [" + result + "]");
    return result;
  }

  private void prepareShader(@Nonnull final InputStream source, final int shaderId) {
    gl.glShaderSource(shaderId, loadShader(source));
    checkGLError("glShaderSource");

    gl.glCompileShader(shaderId);
    checkGLError("glCompileShader");

    params.clear();
    gl.glGetShaderiv(shaderId, gl.GL_COMPILE_STATUS(), params);

    if (params.get(0) == gl.GL_FALSE()) {
      log.warning("compile error: " + gl.glGetShaderInfoLog(shaderId));
    }

    printLogInfo(shaderId);
    checkGLError(String.valueOf(shaderId));
  }

  private String loadShader(@Nonnull final InputStream source) {
    byte[] data = read(source);
    return new String(data, Charset.forName("UTF-8"));
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
    String infoLog = gl.glGetShaderInfoLog(obj);
    checkGLError("glGetShaderInfoLog");
    log.info(getLoggingPrefix() + "Info log:\n" + infoLog);
  }

  private void checkGLError(@Nonnull final String message) {
    CheckGL.checkGLError(gl, getLoggingPrefix() + message);
  }

  @Nonnull
  private String getLoggingPrefix() {
    return "[" + program + "] ";
  }

  @Nonnull
  private InputStream getStream(@Nonnull final File file) throws FileNotFoundException {
    log.fine("loading shader file [" + file + "]");
    return new ByteArrayInputStream(read(new FileInputStream(file)));
  }

  @Nullable
  private InputStream getStream(@Nonnull final String filename) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
  }
}
