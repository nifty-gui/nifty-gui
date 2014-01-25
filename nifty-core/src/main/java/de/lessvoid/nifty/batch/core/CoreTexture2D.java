package de.lessvoid.nifty.batch.core;

import de.lessvoid.nifty.batch.CheckGL;
import de.lessvoid.nifty.batch.GLException;
import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.core.CoreGL;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The CoreTexture2D represents a 2D texture in OpenGL space.
 * <p/>
 * This class takes care of loading the texture in OpenGL and generating mipmaps as needed.
 * <p/>
 * This class does <b>not</b> handle proxy textures.
 *
 * Note: Requires OpenGL 3.2 or greater.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreTexture2D {
  /**
   * Image resizing mode. This enumerator is simple to use and defines the filters for the magnifying and
   * minimizing the texture automatically.
   */
  public enum ResizeFilter {
    /**
     * Nearest filter. This filter applies the nearest filter to both the magnifying and the minimizing filter.
     */
    Nearest,

    /**
     * Linear filter. This applies the linear filter to ot the magnifying and the minimizing filter.
     */
    Linear,

    /**
     * This filter applies the linear nearest to the magnifying filter and the nearest mipmap nearest filter to the
     * minimizing filter. This filter is the fastest mipmap based filtering.
     */
    NearestMipMapNearest,

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap nearest filter to the
     * minimizing filter. This filter is slightly slower then {@link #NearestMipMapNearest} but creates a better
     * quality.
     */
    NearestMipMapLinear,

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap nearest linear to the
     * minimizing filter. This filter is slightly slower then {@link #NearestMipMapLinear} but creates a better
     * quality.
     */
    LinearMipMapNearest,

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap linear linear to the
     * minimizing filter. This filter creates the best quality but is also the slowest filter method.
     */
    LinearMipMapLinear
  }

  /**
   * This enumerator contains the default settings for different color values the texture could be stored as.
   */
  public enum ColorFormat {
    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>red</b> color of the newly created RGB texture.
     */
    Red,

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>green</b> color of the newly created RGB texture.
     */
    Green,

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>blue</b> color of the newly created RGB texture.
     */
    Blue,

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>alpha</b> color of the newly created alpha texture.
     */
    Alpha,

    /**
     * In case this format is used the pixel data is expected to contain three color channels. The colors are
     * <b>red</b>, <b>green</b> and <b>blue</b> in this order. The created texture will be a RGB texture.
     */
    RGB,

    /**
     * In case this format is used the pixel data is expected to contain three color channels. The colors are
     * <b>blue</b>, <b>green</b> and <b>red</b> in this order. The created texture will be a RGB texture.
     */
    BGR,

    /**
     * In case this format is used the pixel data is expected to contain four color channels. The colors are
     * <b>red</b>, <b>green</b>, <b>blue</b> and <b>alpha</b> in this order. The created texture will be a RGBA texture.
     */
    RGBA,

    /**
     * In case this format is used the pixel data is expected to contain four color channels. The colors are
     * <b>blue</b>, <b>green</b>, <b>red</b> and <b>alpha</b> in this order. The created texture will be a RGBA texture.
     */
    BGRA,

    /**
     * In case this format is used the pixel data is expected to contain only one color channels. The color is used
     * as luminance level, so the created texture will be a gray-scale texture.
     */
    Luminance,

    /**
     * In case this format is used the pixel data is expected to contain only two color channels. The channels are
     * expected to be the <b>luminance</b> and the <b>alpha</b> channel. The created texture will be gray-scale texture
     * with transparency.
     */
    LuminanceAlpha
  }

  /**
   * This constant can be used as a data type to enable the automated selection of the data type. Also this value is
   * used for the texture ID in case the class is supposed to acquire the texture ID automatically.
   */
  private static final int AUTO = -1;

  @Nonnull
  private static final Logger LOG = Logger.getLogger(CoreTexture2D.class.getName());

  /**
   * The cached value of the maximal texture size.
   */
  private static int maxTextureSize = -1;

  /**
   * As long as this variable is set {@code true} the class will perform error checking.
   */
  private static boolean errorChecks = true;

  @Nonnull
  private final CoreGL gl;

  /**
   * Used to pass a single texture id to OpenGL functions such as glDeleteTextures().
   */
  @Nonnull
  private final IntBuffer textureIdBuffer;

  /**
   * This flag is switched {@code true} once the texture is disposed. It is afterwards used for two things. First, to
   * throw an exception in case a disposed texture is bound again, and second, to show a warning in case the java
   * garbage collector consumes this class while the assigned texture is not disposed.
   */
  private boolean isDisposed;

  /**
   * The target type of the texture.
   */
  private final int textureTarget;

  /**
   * The ID of the texture.
   */
  private final int textureId;

  /**
   * The width of the texture.
   */
  private final int width;

  /**
   * The height of the texture.
   */
  private final int height;


  /**
   * We remember the parameters used for the glTexture2D call so we can easily update the texture if we need later.
   */
  private boolean textureCanBeUpdated;
  private int texImageTarget;
  private int texImageLevel;
  private int texImageInternalFormat;
  private int texImageWidth;
  private int texImageHeight;
  private int texBorder;
  private int texFormat;
  private int texType;

  /**
   * This is one of the simple constructors that only allow very limited possibilities for settings. However, they use
   * settings that should cover most use cases.
   *
   * @param format The texture format.
   * @param width  The width of the texture.
   * @param height The height of the texture.
   * @param data   The pixel data.
   * @param filter The used filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ColorFormat format,
          final int width,
          final int height,
          @Nonnull final Buffer data,
          @Nonnull final ResizeFilter filter) {
    this(gl, bufferFactory, format, false, width, height, data, filter);
  }

  /**
   * This is one of the simple constructors that only allow very limited possibilities for settings. However, they use
   * settings that should cover most use cases.
   *
   * @param format     The texture format.
   * @param compressed {@code true} In case the internal texture data is supposed to be compressed if possible.
   * @param width      The width of the texture.
   * @param height     The height of the texture.
   * @param data       The pixel data.
   * @param filter     The used filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          @Nonnull final ColorFormat format,
          final boolean compressed,
          final int width,
          final int height,
          @Nonnull final Buffer data,
          @Nonnull final ResizeFilter filter) {
    this(
            gl,
            bufferFactory,
            getInternalFormat(gl, format),
            width,
            height,
            compressed ? getCompressedInternalFormat(gl, format) : getFormat(gl, format),
            data,
            filter);
  }

  /**
   * This constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param internalFormat The internal format of the texture.
   * @param width          The width of the texture in pixels.
   * @param height         The height of the texture in pixels.
   * @param format         The format of the pixel data.
   * @param data           The pixel data.
   * @param filter         The used filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int internalFormat,
          final int width,
          final int height,
          final int format,
          @Nonnull final Buffer data,
          @Nonnull final ResizeFilter filter) {
    this(
            gl,
            bufferFactory,
            gl.GL_TEXTURE_2D(),
            internalFormat,
            width,
            height,
            format,
            data,
            getMagFilter(gl, filter),
            getMinFilter(gl, filter));
  }

  /**
   * This constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param internalFormat The internal format of the texture.
   * @param width          The width of the texture in pixels.
   * @param height         The height of the texture in pixels.
   * @param format         The format of the pixel data.
   * @param data           The pixel data.
   * @param magFilter      The magnifying filter.
   * @param minFilter      The minimizing filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int internalFormat,
          final int width,
          final int height,
          final int format,
          @Nonnull final Buffer data,
          final int magFilter,
          final int minFilter) {
    this(gl, bufferFactory, gl.GL_TEXTURE_2D(), internalFormat, width, height, format, data, magFilter, minFilter);
  }

  /**
   * This constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param target         The target type of the texture operations, has to be a valid 2D texture target.
   * @param internalFormat The internal format of the texture.
   * @param width          The width of the texture in pixels.
   * @param height         The height of the texture in pixels.
   * @param format         The format of the pixel data.
   * @param data           The pixel data.
   * @param magFilter      The magnifying filter.
   * @param minFilter      The minimizing filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int target,
          final int internalFormat,
          final int width,
          final int height,
          final int format,
          @Nonnull final Buffer data,
          final int magFilter,
          final int minFilter) {
    this(gl, bufferFactory, AUTO, target, 0, internalFormat, width, height, 0, format, AUTO, data, magFilter, minFilter);
  }

  /**
   * This constructor allows you to define all the settings required to create a texture. Using this causes the class
   * to disable all assumptions and do exactly what you want.
   *
   * @param textureId      The ID that is supposed to be used with the texture, it has to be a valid texture ID for the
   *                       selected target. Use {@link #AUTO} to tell the class to fetch a texture ID in its own.
   * @param target         The target type of the texture operations, has to be a valid 2D texture target.
   * @param level          The mipmap level of the texture, in case you want the automated mipmap generation to kick
   *                       in leave this value on {@code 0} and selected a fitting minimizing filter.
   * @param internalFormat The internal format of the texture.
   * @param width          The width of the texture in pixels.
   * @param height         The height of the texture in pixels.
   * @param border         The width of the border of the texture.
   * @param format         The format of the pixel data.
   * @param type           The data type of the pixel data.
   * @param data           The pixel data.
   * @param magFilter      The magnifying filter.
   * @param minFilter      The minimizing filter.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case the creation of the texture fails for any reason.
   */
  public CoreTexture2D(
          @Nonnull final CoreGL gl,
          @Nonnull final BufferFactory bufferFactory,
          final int textureId,
          final int target,
          final int level,
          final int internalFormat,
          final int width,
          final int height,
          final int border,
          final int format,
          final int type,
          @Nonnull final Buffer data,
          final int magFilter,
          final int minFilter) {
    this.gl = gl;
    this.textureId = createTexture(
            textureId,
            target,
            level,
            internalFormat,
            width,
            height,
            border,
            format,
            type,
            data,
            magFilter,
            minFilter);
    textureIdBuffer = bufferFactory.createNativeOrderedIntBuffer(1);
    textureTarget = target;
    this.width = width;
    this.height = height;
  }

  /**
   * Fetches the maximum allowed size of a texture.
   *
   * @return The maximum size of a texture in pixels.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case reading the maximal texture size from OpenGL failed.
   */
  public static int getMaxTextureSize(@Nonnull final CoreGL gl) {
    if (maxTextureSize == -1) {
      int[] params = new int[1];
      gl.glGetIntegerv(gl.GL_MAX_TEXTURE_SIZE(), params, 0);
      maxTextureSize = params[0];
      checkGLError(gl,"glGetInteger", true);
    }
    return maxTextureSize;
  }

  /**
   * Disables error checking. No matter the nonsense you send into this class, everything will reach OpenGL. This
   * should be done in case you are worries about the extra overhead caused by the error checks. Also, in case you just
   * don't ever make mistakes you can safely disable error checking.
   */
  public static void disableErrorChecking() {
    errorChecks = false;
  }

  /**
   * Enables error checking. It is enabled by default so you don't need to call this unless you disabled error checking
   * at some point.
   */
  public static void enableErrorChecking() {
    errorChecks = true;
  }

  /**
   * Binds the texture to the current context.
   *
   * @throws de.lessvoid.nifty.batch.GLException In case binding the texture fails.
   */
  public void bind() {
    if (isDisposed) {
      throw new GLException("This texture was disposed. You can't bind it anymore. It's gone for good.");
    }
    gl.glBindTexture(textureTarget, textureId);
    checkGLError("glBindTexture", true);
  }

  /**
   * Disposes of this texture.
   *
   * @throws GLException In case disposing the texture fails.
   */
  public void dispose() {
    deleteTexture(textureId);
    isDisposed = true;
  }

  /**
   * Gets the height of this texture.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the width of this texture.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the OpenGL ID of this texture.
   */
  public int getId() {
    return textureId;
  }

  @Nonnull
  @Override
  public String toString() {
    return CoreTexture2D.class.getName() + '(' + "id:" + textureId + ", " + "w:" + width + ", " + "h:" + height + ')';
  }

  @Override
  protected void finalize() throws Throwable {
    if (!isDisposed) {
      LOG.warning("Memory Leak: Texture " + Integer.toString(textureId) +
          " is getting finalized by the Java GC without being disposed.");
    }
    super.finalize();
  }

  /**
   * Applies the filter values for the texture scaling.
   *
   * @param target    The texture target.
   * @param minFilter The minimize filter.
   * @param magFilter The maximize filter.
   *
   * @throws GLException In case setting the filter parameter fails.
   */
  private void applyFilters(final int target, final int minFilter, final int magFilter) {
    gl.glTexParameteri(target, gl.GL_TEXTURE_MIN_FILTER(), minFilter);
    checkGLError("glTexParameteri", true);
    gl.glTexParameteri(target, gl.GL_TEXTURE_MAG_FILTER(), magFilter);
    checkGLError("glTexParameteri", true);
  }

  /**
   * Generates and binds a texture ID.
   *
   * @param target    The target type of the texture.
   * @param textureId The ID of the texture, in case this is {@code -1} a new texture ID will be generated.
   *
   * @return The texture ID that was actually bound.
   *
   * @throws GLException In case creating or binding the texture fails.
   */
  private int applyTextureId(final int target, final int textureId) {
    final int usedTextureId;
    if (textureId == AUTO) {
      usedTextureId = createTextureID();
    } else {
      usedTextureId = textureId;
    }
    gl.glBindTexture(target, usedTextureId);
    checkGLError("glBindTexture", true);
    return usedTextureId;
  }

  /**
   * Checks if the border parameter is valid.
   *
   * @param border The border parameter.
   * @throws GLException In case the border parameter is not valid.
   */
  private void checkBorder(final int border) {
    if (border != 0 && border != 1) {
      throw new GLException("Border has illegal value: 0x" + Integer.toHexString(border));
    }
  }

  /**
   * Checks the format, the size and the data type value.
   *
   * @param format The selected format.
   * @param type   The selected data type.
   * @throws GLException In case the parameters don't work together.
   */
  private void checkFormatSizeData(final int format, final int type, @Nullable final Buffer data) {
    if (data == null) {
      throw new GLException("Pixeldata must not be NULL");
    }

    if (type == gl.GL_BITMAP() && format != gl.GL_COLOR_INDEX()) {
      throw new GLException("GL_BITMAP requires the format to be GL_COLOR_INDEX");
    }

    if (type == gl.GL_UNSIGNED_BYTE() ||
            type == gl.GL_BYTE() ||
            type == gl.GL_BITMAP() ||
            type == gl.GL_UNSIGNED_BYTE_3_3_2() ||
            type == gl.GL_UNSIGNED_BYTE_2_3_3_REV()) {
      if (!(data instanceof ByteBuffer)) {
        throw new GLException("The selected type requires its data as byte values.");
      }
    } else if (
            type == gl.GL_UNSIGNED_SHORT() ||
            type == gl.GL_SHORT() ||
            type == gl.GL_UNSIGNED_SHORT_5_6_5() ||
            type == gl.GL_UNSIGNED_SHORT_5_6_5_REV() ||
            type == gl.GL_UNSIGNED_SHORT_4_4_4_4() ||
            type == gl.GL_UNSIGNED_SHORT_4_4_4_4_REV() ||
            type == gl.GL_UNSIGNED_SHORT_5_5_5_1() ||
            type == gl.GL_UNSIGNED_SHORT_1_5_5_5_REV()) {
      if (!(data instanceof ShortBuffer)) {
        throw new GLException("The selected type requires its data as short values.");
      }
    } else if (
            type == gl.GL_UNSIGNED_INT() ||
            type == gl.GL_INT() ||
            type == gl.GL_UNSIGNED_INT_8_8_8_8() ||
            type == gl.GL_UNSIGNED_INT_8_8_8_8_REV() ||
            type == gl.GL_UNSIGNED_INT_10_10_10_2() ||
            type == gl.GL_UNSIGNED_INT_2_10_10_10_REV()) {
      if (!(data instanceof IntBuffer)) {
        throw new GLException("The selected type requires its data as integer values.");
      }
    } else if (type == gl.GL_FLOAT()) {
      if (!(data instanceof FloatBuffer) && !(data instanceof DoubleBuffer)) {
        throw new GLException("The selected type requires its data as floating-point values.");
      }
    } else {
      throw new GLException("Unknown type value.");
    }
  }

  /**
   * Checks OpenGL errors if error checking is enabled.
   *
   * @param message        The message in case an error is detected.
   * @param throwException {@code true} In case an exception is supposed to be thrown in case a error is detected.
   */
  private static void checkGLError(
          @Nonnull final CoreGL gl,
          @Nonnull final String message,
          final boolean throwException) {
    if (errorChecks) {
      CheckGL.checkGLError(gl, message, throwException);
    }
  }

  /**
   * Checks OpenGL errors if error checking is enabled.
   *
   * @param message        The message in case an error is detected.
   * @param throwException {@code true} In case an exception is supposed to be thrown in case a error is detected.
   */
  private void checkGLError(@Nonnull final String message, final boolean throwException) {
    if (errorChecks) {
      CheckGL.checkGLError(gl, message, throwException);
    }
  }

  /**
   * Checks if the target ID is valid to be used with this class.
   *
   * @param target The target ID.
   * @throws GLException In case the {@code target} parameter contains a illegal value.
   */
  private void checkTarget(final int target) {
    if (target != gl.GL_TEXTURE_2D() ||
            target != gl.GL_TEXTURE_CUBE_MAP_POSITIVE_X() ||
            target != gl.GL_TEXTURE_CUBE_MAP_NEGATIVE_X() ||
            target != gl.GL_TEXTURE_CUBE_MAP_POSITIVE_Y() ||
            target != gl.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y() ||
            target != gl.GL_TEXTURE_CUBE_MAP_POSITIVE_Z() ||
            target != gl.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z()) {
        throw new GLException("Illegal target ID: 0x" + Integer.toHexString(target));
    }
  }

  /**
   * Creates a new texture and transfers the data into the OpenGL space. Also generates mipmaps as needed.
   *
   * @param textureId      The ID of the texture to use, in case this is {@code -1} a new texture ID is generated.
   * @param target         The target of the create operation, has to be a valid 2D texture target.
   * @param level          The mipmap level, automatic mipmap generation is disabled in case this valid is not equal
   *                       to 0.
   * @param internalFormat The internal texture format.
   * @param width          The width of the texture.
   * @param height         The height of the texture.
   * @param border         The border width of the texture.
   * @param format         The format of the pixel data.
   * @param type           The data type of the pixel data.
   * @param data           The pixel data.
   * @param magFilter      The magnifying filter.
   * @param minFilter      The minimizing filter, in case a filter that uses mipmaps is selected, those mipmaps are
   *                       generated automatically.
   *
   * @return The texture ID of the newly created texture.
   *
   * @throws GLException In case anything goes wrong.
   */
  private int createTexture(
      final int textureId,
      final int target,
      final int level,
      final int internalFormat,
      final int width,
      final int height,
      final int border,
      final int format,
      final int type,
      final Buffer data,
      final int magFilter,
      final int minFilter) {
    final int usedType = getType(type, data);

    if (errorChecks) {
      checkTarget(target);
      checkBorder(border);
      CheckGL.checkGLTextureSize(gl, width - 2 * border, height - 2 * border);
      checkFormatSizeData(format, usedType, data);
    }

    final int usedTextureId = applyTextureId(target, textureId);
    try {
      applyFilters(target, minFilter, magFilter);

      if (isCreatingMipMaps(level, minFilter)) {
        glTexImage2D(target, 0, internalFormat, width, height, border, format, usedType, data);
        gl.glGenerateMipmap(target);
        checkGLError("glGenerateMipmap", true);
      } else {
        glTexImage2D(target, level, internalFormat, width, height, border, format, usedType, data);
      }
    } catch (@Nonnull final GLException ex) {
      if (textureId == -1) {
        deleteTexture(usedTextureId);
      }
      throw ex;
    }

    return usedTextureId;
  }

  /**
   * Deletes the texture with the specified ID.
   */
  private void deleteTexture(final int textureId) {
    textureIdBuffer.clear();
    textureIdBuffer.put(0, textureId);
    gl.glDeleteTextures(1, textureIdBuffer);
    checkGLError("glDeleteTextures", false);
  }

  /**
   * Creates a new texture ID.
   *
   * @return The newly generated texture ID.
   * @throws GLException In case generation a new texture ID fails.
   */
  private int createTextureID() {
    textureIdBuffer.clear();
    gl.glGenTextures(1, textureIdBuffer);
    checkGLError("glGenTextures", true);
    return textureIdBuffer.get(0);
  }

  /**
   * Automatically selects a type in case it's requested. Only determines the best fitting type automatically in case
   * the {@code type} parameter is set to {@link #AUTO}.
   *
   * @param type The selected type of the pixel data.
   * @param data The pixel data.
   *
   * @return The value of {@code type} or a automatically determined type.
   *
   * @throws GLException In case the automated detection of the type fails.
   */
  private int getType(final int type, final Buffer data) {
    if (type == AUTO) {
      if (data instanceof ByteBuffer) {
        return gl.GL_UNSIGNED_BYTE();
      } else if (data instanceof ShortBuffer) {
        return gl.GL_UNSIGNED_SHORT();
      } else if (data instanceof IntBuffer) {
        return gl.GL_UNSIGNED_INT();
      } else if (data instanceof FloatBuffer) {
        return gl.GL_FLOAT();
      } else if (data instanceof DoubleBuffer) {
        return gl.GL_FLOAT();
      } else {
        throw new GLException("Unknown buffer type; " + data.getClass().toString());
      }
    } else {
      return type;
    }
  }

  /**
   * Wraps the actual call to {@code glTexImage2D}. It uses the generic {@link java.nio.Buffer} and internally casts it
   * as needed to fit the different implementations of the {@code glTexImage2D} function.
   *
   * @param target         The target of the texture creation operation.
   * @param level          The level of this texture.
   * @param internalformat The internal format.
   * @param width          The width of the texture.
   * @param height         The height of the texture.
   * @param border         The border width of the texture.
   * @param format         The format of the pixel data.
   * @param type           The data type of the pixel data.
   * @param pixels         The pixel data.
   *
   * @throws GLException In case OpenGL reports a error or the type of the buffer is unknown.
   */
  private void glTexImage2D(
      final int target,
      final int level,
      final int internalformat,
      final int width,
      final int height,
      final int border,
      final int format,
      final int type,
      final Buffer pixels) {
    this.texImageTarget = target;
    this.texImageLevel = level;
    this.texImageInternalFormat = internalformat;
    this.texImageWidth = width;
    this.texImageHeight = height;
    this.texBorder = border;
    this.texFormat = format;
    this.texType = type;
    this.textureCanBeUpdated = true;
    updateTextureData(pixels);
  }

  public void updateTextureData(final Buffer pixels) {
    if (!textureCanBeUpdated) {
      throw new GLException("updateTextureData() call can only be used to update texture data");
    }

    if (pixels instanceof ByteBuffer) {
      gl.glTexImage2D(
              texImageTarget,
              texImageLevel,
              texImageInternalFormat,
              texImageWidth,
              texImageHeight,
              texBorder,
              texFormat,
              texType,
              (ByteBuffer) pixels);
    } else if (pixels instanceof ShortBuffer) {
      gl.glTexImage2D(
              texImageTarget,
              texImageLevel,
              texImageInternalFormat,
              texImageWidth,
              texImageHeight,
              texBorder,
              texFormat,
              texType,
              (ShortBuffer) pixels);
    } else if (pixels instanceof IntBuffer) {
      gl.glTexImage2D(
              texImageTarget,
              texImageLevel,
              texImageInternalFormat,
              texImageWidth,
              texImageHeight,
              texBorder,
              texFormat,
              texType,
              (IntBuffer) pixels);
    } else if (pixels instanceof FloatBuffer) {
      gl.glTexImage2D(
              texImageTarget,
              texImageLevel,
              texImageInternalFormat,
              texImageWidth,
              texImageHeight,
              texBorder,
              texFormat,
              texType,
              (FloatBuffer) pixels);
    } else if (pixels instanceof DoubleBuffer) {
      gl.glTexImage2D(
              texImageTarget,
              texImageLevel,
              texImageInternalFormat,
              texImageWidth,
              texImageHeight,
              texBorder,
              texFormat,
              texType,
              (DoubleBuffer) pixels);
    } else {
      throw new GLException("Unknown buffer type; " + pixels.getClass().toString());
    }
    checkGLError("glTexImage2D", true);
  }

  /**
   * Checks if mipmaps are supposed to be generated.
   *
   * @param level     The level settings.
   * @param minFilter The minimizing filter.
   *
   * @return {@code true} In case mipmaps are supposed to be generated.
   */
  private boolean isCreatingMipMaps(final int level, final int minFilter) {
    return level <= 0 &&
            (minFilter == gl.GL_NEAREST_MIPMAP_NEAREST() ||
             minFilter == gl.GL_LINEAR_MIPMAP_NEAREST() ||
             minFilter == gl.GL_NEAREST_MIPMAP_LINEAR() ||
             minFilter == gl.GL_LINEAR_MIPMAP_LINEAR());

  }

  /**
   * Gets the minimizing filter.
   */
  private static int getMinFilter(@Nonnull final CoreGL gl, @Nonnull final ResizeFilter resizeFilter) {
    switch(resizeFilter) {
      case Nearest: {
        return gl.GL_NEAREST();
      }
      case Linear: {
        return gl.GL_LINEAR();
      }
      case NearestMipMapNearest: {
        return gl.GL_NEAREST_MIPMAP_NEAREST();
      }
      case NearestMipMapLinear: {
        return gl.GL_NEAREST_MIPMAP_LINEAR();
      }
      case LinearMipMapNearest: {
        return gl.GL_LINEAR_MIPMAP_NEAREST();
      }
      case LinearMipMapLinear: {
        return gl.GL_LINEAR_MIPMAP_LINEAR();
      }
      default: {
        throw new GLException("Unknown ResizeFilter value for minimizing filter!");
      }
    }
  }

  /**
   * Gets the magnifying filter.
   */
  private static int getMagFilter(@Nonnull final CoreGL gl, @Nonnull final ResizeFilter resizeFilter) {
    switch(resizeFilter) {
      case Nearest: {
        return gl.GL_NEAREST();
      }
      case Linear: {
        return gl.GL_LINEAR();
      }
      case NearestMipMapNearest: {
        return gl.GL_NEAREST();
      }
      case NearestMipMapLinear: {
        return gl.GL_LINEAR();
      }
      case LinearMipMapNearest: {
        return gl.GL_LINEAR();
      }
      case LinearMipMapLinear: {
        return gl.GL_LINEAR();
      }
      default: {
        throw new GLException("Unknown ResizeFilter value for magnifying filter!");
      }
    }
  }

  /**
   * Gets the pixel data format.
   */
  private static  int getFormat(@Nonnull final CoreGL gl, @Nonnull final ColorFormat colorFormat) {
    switch(colorFormat) {
      case Red: {
        return gl.GL_RED();
      }
      case Green: {
        return gl.GL_GREEN();
      }
      case Blue: {
        return gl.GL_BLUE();
      }
      case Alpha: {
        return gl.GL_ALPHA();
      }
      case RGB: {
        return gl.GL_RGB();
      }
      case BGR: {
        return gl.GL_BGR();
      }
      case RGBA: {
        return gl.GL_RGBA();
      }
      case BGRA: {
        return gl.GL_BGRA();
      }
      case Luminance: {
        return gl.GL_LUMINANCE();
      }
      case LuminanceAlpha: {
        return gl.GL_LUMINANCE_ALPHA();
      }
      default: {
        throw new GLException("Unknown ColorFormat value for pixel data format!");
      }
    }
  }

  /**
   * Gets the internal texture format.
   */
  private static int getInternalFormat(@Nonnull final CoreGL gl, @Nonnull final ColorFormat colorFormat) {
    switch(colorFormat) {
      case Red: {
        return gl.GL_RGB();
      }
      case Green: {
        return gl.GL_RGB();
      }
      case Blue: {
        return gl.GL_RGB();
      }
      case Alpha: {
        return gl.GL_ALPHA();
      }
      case RGB: {
        return gl.GL_RGB();
      }
      case BGR: {
        return gl.GL_RGB();
      }
      case RGBA: {
        return gl.GL_RGBA();
      }
      case BGRA: {
        return gl.GL_RGBA();
      }
      case Luminance: {
        return gl.GL_LUMINANCE();
      }
      case LuminanceAlpha: {
        return gl.GL_LUMINANCE_ALPHA();
      }
      default: {
        throw new GLException("Unknown ColorFormat value for internal texture format!");
      }
    }
  }

  /**
   * Gets the compressed internal texture format.
   */
  private static int getCompressedInternalFormat(@Nonnull final CoreGL gl, @Nonnull final ColorFormat colorFormat) {
    switch(colorFormat) {
      case Red: {
        return gl.GL_COMPRESSED_RGB();
      }
      case Green: {
        return gl.GL_COMPRESSED_RGB();
      }
      case Blue: {
        return gl.GL_COMPRESSED_RGB();
      }
      case Alpha: {
        return gl.GL_COMPRESSED_ALPHA();
      }
      case RGB: {
        return gl.GL_COMPRESSED_RGB();
      }
      case BGR: {
        return gl.GL_COMPRESSED_RGB();
      }
      case RGBA: {
        return gl.GL_COMPRESSED_RGBA();
      }
      case BGRA: {
        return gl.GL_COMPRESSED_RGBA();
      }
      case Luminance: {
        return gl.GL_COMPRESSED_LUMINANCE();
      }
      case LuminanceAlpha: {
        return gl.GL_COMPRESSED_LUMINANCE_ALPHA();
      }
      default: {
        throw new GLException("Unknown ColorFormat value for compressed internal texture format!");
      }
    }
  }
}
