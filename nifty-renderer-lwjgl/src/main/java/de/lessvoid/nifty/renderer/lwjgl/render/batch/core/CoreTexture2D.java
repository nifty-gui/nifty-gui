package de.lessvoid.nifty.renderer.lwjgl.render.batch.core;

import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.*;
import java.util.logging.Logger;

/**
 * The CoreTexture2D represents a 2D texture in OpenGL space.
 * <p/>
 * This class takes care for loading the texture to OpenGL and for generating mipmaps as needed.
 * <p/>
 * This class does <b>not</b> handle proxy textures.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class CoreTexture2D {
  /**
   * Image resizing mode. This enumerator is used in simple and defines the used filter for the magnifying and
   * minimizing automatically.
   */
  public enum ResizeFilter {
    /**
     * Nearest filter. This filter applies the nearest filter to both the magnifying and the minimizing filter.
     */
    Nearest(GL11.GL_NEAREST, GL11.GL_NEAREST),

    /**
     * Linear filter. This applies the linear filter to ot the magnifying and the minimizing filter.
     */
    Linear(GL11.GL_LINEAR, GL11.GL_LINEAR),

    /**
     * This filter applies the linear nearest to the magnifying filter and the nearest mipmap nearest filter to the
     * minimizing filter. This filter is the fastest mipmap based filtering
     */
    NearestMipMapNearest(GL11.GL_NEAREST_MIPMAP_NEAREST, GL11.GL_NEAREST),

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap nearest filter to the
     * minimizing filter. This filter is slightly slower then {@link #NearestMipMapNearest} but creates a better
     * quality.
     */
    NearestMipMapLinear(GL11.GL_NEAREST_MIPMAP_LINEAR, GL11.GL_LINEAR),

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap nearest linear to the
     * minimizing filter. This filter is slightly slower then {@link #NearestMipMapLinear} but creates a better
     * quality.
     */
    LinearMipMapNearest(GL11.GL_LINEAR_MIPMAP_NEAREST, GL11.GL_LINEAR),

    /**
     * This filter applies the linear linear to the magnifying filter and the linear mipmap linear linear to the
     * minimizing filter. This filter creates the best quality but is also the slowest filter method.
     */
    LinearMipMapLinear(GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR);

    /**
     * The value of the minimizing filter.
     */
    private final int minFilterValue;

    /**
     * The value of the magnifying filter.
     */
    private final int magFilterValue;

    /**
     * Default constructor.
     *
     * @param minFilter the minimizing filter
     * @param magFilter the magnifying filter
     */
    ResizeFilter(final int minFilter, final int magFilter) {
      minFilterValue = minFilter;
      magFilterValue = magFilter;
    }

    /**
     * The magnifying filter.
     *
     * @return the magnifying filter
     */
    public int getMagFilter() {
      return magFilterValue;
    }

    /**
     * The minimizing filter.
     *
     * @return the minimizing filter
     */
    public int getMinFilter() {
      return minFilterValue;
    }
  }

  /**
   * This enumerator contains the default settings for different color values the texture could be stored as.
   */
  public enum ColorFormat {
    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>red</b> color of the newly created RGB texture.
     */
    Red(GL11.GL_RED, GL11.GL_RGB, GL13.GL_COMPRESSED_RGB),

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>green</b> color of the newly created RGB texture.
     */
    Green(GL11.GL_GREEN, GL11.GL_RGB, GL13.GL_COMPRESSED_RGB),

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>blue</b> color of the newly created RGB texture.
     */
    Blue(GL11.GL_BLUE, GL11.GL_RGB, GL13.GL_COMPRESSED_RGB),

    /**
     * In case this format is used the pixel data is expected to contain only one color channel. This color channel is
     * used as the <b>alpha</b> color of the newly created alpha texture.
     */
    Alpha(GL11.GL_ALPHA, GL11.GL_ALPHA, GL13.GL_COMPRESSED_ALPHA),

    /**
     * In case this format is used the pixel data is expected to contain three color channels. The colors are
     * <b>red</b>, <b>green</b> and <b>blue</b> in this order. The created texture will be a RGB texture.
     */
    RGB(GL11.GL_RGB, GL11.GL_RGB, GL13.GL_COMPRESSED_RGB),

    /**
     * In case this format is used the pixel data is expected to contain three color channels. The colors are
     * <b>blue</b>, <b>green</b> and <b>red</b> in this order. The created texture will be a RGB texture.
     */
    BGR(GL12.GL_BGR, GL11.GL_RGB, GL13.GL_COMPRESSED_RGB),

    /**
     * In case this format is used the pixel data is expected to contain four color channels. The colors are
     * <b>red</b>, <b>green</b>, <b>blue</b> and <b>alpha</b> in this order. The created texture will be a RGBA texture.
     */
    RGBA(GL11.GL_RGBA, GL11.GL_RGBA, GL13.GL_COMPRESSED_RGBA),

    /**
     * In case this format is used the pixel data is expected to contain four color channels. The colors are
     * <b>blue</b>, <b>green</b>, <b>red</b> and <b>alpha</b> in this order. The created texture will be a RGBA texture.
     */
    BGRA(GL12.GL_BGRA, GL11.GL_RGBA, GL13.GL_COMPRESSED_RGBA),

    /**
     * In case this format is used the pixel data is expected to contain only one color channels. The color is used
     * as luminance level, so the created texture will be a gray-scale texture.
     */
    Luminance(GL11.GL_LUMINANCE, GL11.GL_LUMINANCE, GL13.GL_COMPRESSED_LUMINANCE),

    /**
     * In case this format is used the pixel data is expected to contain only two color channels. The channels are
     * expected to be the <b>luminance</b> and the <b>alpha</b> channel. The created texture will be gray-scale texture
     * with transparency.
     */
    LuminanceAlpha(GL11.GL_LUMINANCE_ALPHA, GL11.GL_LUMINANCE_ALPHA, GL13.GL_COMPRESSED_LUMINANCE_ALPHA);

    /**
     * The pixel data format.
     */
    private final int format;

    /**
     * The texture format.
     */
    private final int internalFormat;

    /**
     * The compressed kind of the texture format.
     */
    private final int compressedInternalFormat;

    /**
     * Default constructor.
     *
     * @param newFormat                   the pixel data format
     * @param newInternalFormat           the internal format
     * @param newCompressedInternalFormat the compressed internal format
     */
    ColorFormat(final int newFormat, final int newInternalFormat, final int newCompressedInternalFormat) {
      format = newFormat;
      internalFormat = newInternalFormat;
      compressedInternalFormat = newCompressedInternalFormat;
    }

    /**
     * Get the compressed internal texture format.
     *
     * @return the compressed internal texture format
     */
    public int getCompressedInternalFormat() {
      return compressedInternalFormat;
    }

    /**
     * Get the pixel data format.
     *
     * @return the pixel data format
     */
    public int getFormat() {
      return format;
    }

    /**
     * Get the internal texture format.
     *
     * @return the internal texture format
     */
    public int getInternalFormat() {
      return internalFormat;
    }
  }

  /**
   * This constant can be used as the data type to enable the automated selection of the data type. Also his value is
   * used for the texture ID in case the class is supposed to acquire the texture ID automatically.
   */
  private static final int AUTO = -1;

  /**
   * The logger used to spam your logfile.
   */
  private static final Logger LOG = Logger.getLogger(CoreTexture2D.class.getName());

  /**
   * The buffered value of the maximal texture size.
   */
  private static int maxTextureSize = -1;

  /**
   * As long as this variable is set {@code true} the class will perform checks for errors.
   */
  private static boolean errorChecks = true;

  /**
   * This flag is switched {@code true} once the texture is disposed. It is afterwards used for two things. First to
   * throw a exception in case a disposed texture is bound again, secondly to show a warning in case the java garbage
   * collector consumes this class while the assigned texture was not disposed.
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
   * This is one of the simple constructors that only allow very limited possibilities for settings. How ever they use
   * settings that should fit the need on most cases.
   *
   * @param format the texture format
   * @param width  the width of the texture
   * @param height the height of the texture
   * @param data   the pixel data
   * @param filter the used filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      @Nonnull final ColorFormat format, final int width, final int height, final Buffer data,
      @Nonnull final ResizeFilter filter) {
    this(format, false, width, height, data, filter);
  }

  /**
   * This is the constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param internalFormat the internal format of the texture
   * @param width          the width of the texture in pixels
   * @param height         the height of the texture in pixels
   * @param format         the format of the pixel data
   * @param data           the pixel data
   * @param filter         the used filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      final int internalFormat, final int width, final int height, final int format,
      final Buffer data, @Nonnull final ResizeFilter filter) {
    this(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, data, filter.getMagFilter(), filter.getMinFilter());
  }

  /**
   * This is one of the simple constructors that only allow very limited possibilities for settings. How ever they use
   * settings that should fit the need on most cases.
   *
   * @param format     the texture format
   * @param compressed {@code true} in case the internal texture data is supposed to be compressed if possible
   * @param width      the width of the texture
   * @param height     the height of the texture
   * @param data       the pixel data
   * @param filter     the used filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      @Nonnull final ColorFormat format, final boolean compressed, final int width, final int height,
      final Buffer data, @Nonnull final ResizeFilter filter) {
    this(format.getInternalFormat(), width, height,
        (compressed ? format.getCompressedInternalFormat() : format.getFormat()), data, filter);
  }

  /**
   * This is the constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param internalFormat the internal format of the texture
   * @param width          the width of the texture in pixels
   * @param height         the height of the texture in pixels
   * @param format         the format of the pixel data
   * @param data           the pixel data
   * @param magFilter      the magnifying filter
   * @param minFilter      the minimizing filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      final int internalFormat, final int width, final int height, final int format,
      final Buffer data, final int magFilter, final int minFilter) {
    this(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, data, magFilter, minFilter);
  }

  /**
   * This is the constructor is a slightly reduced version that defines some common options automatically.
   *
   * @param target         the target type of the texture operations, has to be a valid 2D texture target
   * @param internalFormat the internal format of the texture
   * @param width          the width of the texture in pixels
   * @param height         the height of the texture in pixels
   * @param format         the format of the pixel data
   * @param data           the pixel data
   * @param magFilter      the magnifying filter
   * @param minFilter      the minimizing filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      final int target, final int internalFormat, final int width, final int height, final int format,
      final Buffer data, final int magFilter, final int minFilter) {
    this(AUTO, target, 0, internalFormat, width, height, 0, format, AUTO, data, magFilter, minFilter);
  }

  /**
   * This is the constructor that allows to define all the settings required to create a texture. Using this causes the
   * class to disable all assumptions and do exactly what you want.
   *
   * @param textureId      the ID that is supposed to be used with the texture, it has to be a valid texture ID for the
   *                       selected target. Use {@link #AUTO} to tell the class to fetch a texture ID in its own.
   * @param target         the target type of the texture operations, has to be a valid 2D texture target
   * @param level          the mipmap level of the texture, in case you want the automated mipmap generation to kick
   *                       in leave
   *                       this value on {@code 0} and selected a fitting minimizing filter
   * @param internalFormat the internal format of the texture
   * @param width          the width of the texture in pixels
   * @param height         the height of the texture in pixels
   * @param border         the width of the border of the texture
   * @param format         the format of the pixel data
   * @param type           the data type of the pixel data
   * @param data           the pixel data
   * @param magFilter      the magnifying filter
   * @param minFilter      the minimizing filter
   * @throws CoreGLException in case the creation of the texture fails for any reason
   */
  public CoreTexture2D(
      final int textureId, final int target, final int level, final int internalFormat, final int width,
      final int height, final int border, final int format, final int type, final Buffer data,
      final int magFilter, final int minFilter) {
    this.textureId = createTexture(textureId, target, level, internalFormat, width, height, border, format, type, data,
        magFilter, minFilter);
    textureTarget = target;
    this.width = width;
    this.height = height;
  }

  /**
   * Fetch the maximal allowed size of a texture.
   *
   * @return the maximal size of a texture in pixels
   * @throws CoreGLException in case reading the maximal texture size from OpenGL failed
   */
  public static int getMaxTextureSize() {
    if (maxTextureSize == -1) {
      maxTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
      checkGLError("glGetInteger", true);
    }
    return maxTextureSize;
  }

  /**
   * Check if non-power-of-two textures are supported hardware-accelerated.
   *
   * @return {@code true} in case non-power-of-two are supported hardware-accelerated
   */
  public static boolean isNPOTHardwareSupported() {
    return GLContext.getCapabilities().GL_ARB_texture_non_power_of_two;
  }

  /**
   * Check if non-power-of-two textures are supported. The support is maybe done in software and so very slow.
   *
   * @return {@code true} in case this OpenGL context is able to handle non-power-of-two textures
   * @see #isNPOTHardwareSupported()
   */
  public static boolean isNPOTSupported() {
    return GLContext.getCapabilities().OpenGL20 || isNPOTHardwareSupported();
  }

  /**
   * Calling this functions causes the error checks of this function to be disabled. No matter the nonsense you send
   * into this class, everything will reach OpenGL. This should be done in case you are worries about the extra overhead
   * caused by the error checks. Also in case you just don't ever do mistakes you can safely disables those checks.
   */
  public static void disableErrorChecking() {
    errorChecks = false;
  }

  /**
   * Calling this function causes the error checks to be enabled again. By default the error checks are active. It is
   * only needed to call this in case you disabled the checks sometime before.
   */
  public static void enableErrorChecking() {
    errorChecks = true;
  }

  /**
   * Bind the texture to the current context.
   *
   * @throws CoreGLException in case binding the texture fails
   */
  public void bind() {
    if (isDisposed) {
      throw new CoreGLException("This texture was disposed. You can't bind it anymore. Its gone for good.");
    }
    GL11.glBindTexture(textureTarget, textureId);
    checkGLError("glBindTexture", true);
  }

  /**
   * Dispose this texture.
   *
   * @throws CoreGLException in case disposing the texture fails
   */
  public void dispose() {
    GL11.glDeleteTextures(textureId);
    checkGLError("dispose", true);
    isDisposed = true;
  }

  /**
   * Get the height of this texture.
   *
   * @return the height of the texture
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the width of this texture.
   *
   * @return the width of the texture
   */
  public int getWidth() {
    return width;
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
   * Apply the filter values for the texture scaling.
   *
   * @param target    the texture target
   * @param minFilter the minimize filter
   * @param magFilter the maximize filter
   * @throws CoreGLException in case setting the filter parameter fails
   */
  private static void applyFilters(final int target, final int minFilter, final int magFilter) {
    GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
    checkGLError("glTexParameteri", true);
    GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
    checkGLError("glTexParameteri", true);
  }

  /**
   * Generate and bind a texture ID.
   *
   * @param target    the target type of the texture
   * @param textureId the ID of the texture, in case this is {@code -1} a new texture ID will be generated.
   * @return the texture ID that was actually bound
   * @throws CoreGLException in case creating or binding the texture fails
   */
  private static int applyTextureId(final int target, final int textureId) {
    final int usedTextureId;
    if (textureId == AUTO) {
      usedTextureId = createTextureID();
    } else {
      usedTextureId = textureId;
    }

    GL11.glBindTexture(target, usedTextureId);
    checkGLError("glBindTexture", true);
    return usedTextureId;
  }

  /**
   * Check if the border parameter is valid.
   *
   * @param border the border parameter
   * @throws CoreGLException in case the border parameter is not valid
   */
  private static void checkBorder(final int border) {
    if (border != 0 && border != 1) {
      throw new CoreGLException("Border has illegal value: 0x" + Integer.toHexString(border));
    }
  }

  /**
   * Check the format, the size and the data type value.
   *
   * @param format the selected format
   * @param type   the selected data type
   * @throws CoreGLException in case the parameters don't work together
   */
  private static void checkFormatSizeData(final int format, final int type, @Nullable final Buffer data) {
    if (data == null) {
      throw new CoreGLException("Pixeldata must not be NULL");
    }

    if (type == GL11.GL_BITMAP && format != GL11.GL_COLOR_INDEX) {
      throw new CoreGLException("GL_BITMAP requires the format to be GL_COLOR_INDEX");
    }

    switch (type) {
      case GL11.GL_UNSIGNED_BYTE:
      case GL11.GL_BYTE:
      case GL11.GL_BITMAP:
      case GL12.GL_UNSIGNED_BYTE_3_3_2:
      case GL12.GL_UNSIGNED_BYTE_2_3_3_REV:
        if (!(data instanceof ByteBuffer)) {
          throw new CoreGLException("The selected type requires its data as byte values.");
        }
        break;

      case GL11.GL_UNSIGNED_SHORT:
      case GL11.GL_SHORT:
      case GL12.GL_UNSIGNED_SHORT_5_6_5:
      case GL12.GL_UNSIGNED_SHORT_5_6_5_REV:
      case GL12.GL_UNSIGNED_SHORT_4_4_4_4:
      case GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV:
      case GL12.GL_UNSIGNED_SHORT_5_5_5_1:
      case GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV:
        if (!(data instanceof ShortBuffer)) {
          throw new CoreGLException("The selected type requires its data as short values.");
        }
        break;

      case GL11.GL_UNSIGNED_INT:
      case GL11.GL_INT:
      case GL12.GL_UNSIGNED_INT_8_8_8_8:
      case GL12.GL_UNSIGNED_INT_8_8_8_8_REV:
      case GL12.GL_UNSIGNED_INT_10_10_10_2:
      case GL12.GL_UNSIGNED_INT_2_10_10_10_REV:
        if (!(data instanceof IntBuffer)) {
          throw new CoreGLException("The selected type requires its data as integer values.");
        }
        break;

      case GL11.GL_FLOAT:
        if (!(data instanceof FloatBuffer) && !(data instanceof DoubleBuffer)) {
          throw new CoreGLException("The selected type requires its data as floating-point values.");
        }
        break;

      default:
        throw new CoreGLException("Unknown type value.");
    }
  }

  /**
   * This functions causes the OpenGL errors to be checked in case error checking is enabled.
   *
   * @param message        the message in case a error is detected
   * @param throwException {@code true} in case an exception is supposed to be thrown in case a error is detected
   */
  private static void checkGLError(final String message, final boolean throwException) {
    if (errorChecks) {
      CoreCheckGL.checkGLError(message, throwException);
    }
  }

  /**
   * This function is used to check if the size value fit the capabilities of OpenGL.
   *
   * @param width  the width of the new texture
   * @param height the height of the new texture
   * @throws CoreGLException in case the texture dimensions are too large or negative
   */
  private static void checkSize(final int width, final int height) {
    final int maxSize = getMaxTextureSize();

    if ((width > maxSize) || (height > maxSize)) {
      throw new CoreGLException("Attempt to allocate a texture to big for the current hardware");
    }
    if (width < 0) {
      throw new CoreGLException("Attempt to allocate a texture with a width value below 0.");
    }
    if (height < 0) {
      throw new CoreGLException("Attempt to allocate a texture with a height value below 0.");
    }

    if (!isPowerOfTwo(height) || !isPowerOfTwo(width)) {
      if (!isNPOTSupported()) {
        throw new CoreGLException("Non-power-of-two textures are not supported.");
      }
      if (!isNPOTHardwareSupported()) {
        LOG.warning("Non-pwer-of-two textures are supported, but software emulated.");
      }
    }
  }

  /**
   * Check if the target ID is valid to be used with this class.
   *
   * @param target the target ID
   * @throws CoreGLException in case the {@code target} parameter contains a illegal value
   */
  private static void checkTarget(final int target) {
    switch (target) {
      case GL11.GL_TEXTURE_2D:
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X:
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X:
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y:
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y:
      case GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z:
      case GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z:
        break;

      default:
        throw new CoreGLException("Illegal target ID: 0x" + Integer.toHexString(target));
    }
  }

  /**
   * Create a new texture and transfer the data into the OpenGL space. Also generate mipmaps as needed.
   *
   * @param textureId      the ID of the texture to use, in case this is {@code -1} a new texture ID is generated
   * @param target         the target of the create operation, has to be a valid 2D texture target
   * @param level          the mipmap level, automatic mipmap generation is disabled in case this valid is not equal
   *                       to 0
   * @param internalFormat the internal texture format
   * @param width          the width of the texture
   * @param height         the height of the texture
   * @param border         the border width of the texture
   * @param format         the format of the pixel data
   * @param type           the data type of the pixel data
   * @param data           the pixel data
   * @param magFilter      the magnifying filter
   * @param minFilter      the minimizing filter, in case a filter that uses mipmaps is selected,
   *                       those mipmaps are generated
   *                       automatically
   * @return the texture ID of the newly created texture
   * @throws CoreGLException in case anything goes wrong
   */
  private int createTexture(
      final int textureId, final int target, final int level, final int internalFormat,
      final int width, final int height, final int border, final int format,
      final int type, final Buffer data, final int magFilter, final int minFilter) {
    final int usedType = getType(type, data);

    if (errorChecks) {
      checkTarget(target);
      checkBorder(border);
      checkSize(width - 2 * border, height - 2 * border);
      checkFormatSizeData(format, usedType, data);
    }

    final int usedTextureId = applyTextureId(target, textureId);
    try {
      applyFilters(target, minFilter, magFilter);

      if (isCreatingMipMaps(level, minFilter)) {
        if (GLContext.getCapabilities().OpenGL30) {
          glTexImage2D(target, 0, internalFormat, width, height, border, format, usedType, data);
          GL30.glGenerateMipmap(target);
          checkGLError("glGenerateMipmap", true);
        } else if (GLContext.getCapabilities().GL_EXT_framebuffer_object) {
          glTexImage2D(target, 0, internalFormat, width, height, border, format, usedType, data);
          EXTFramebufferObject.glGenerateMipmapEXT(target);
          checkGLError("glGenerateMipmapEXT", true);
        } else if (GLContext.getCapabilities().GL_SGIS_generate_mipmap &&
            ((isPowerOfTwo(height) && isPowerOfTwo(width)) || isNPOTHardwareSupported())) {
          GL11.glTexParameteri(target, SGISGenerateMipmap.GL_GENERATE_MIPMAP_SGIS, GL11.GL_TRUE);
          checkGLError("glTexParameteri", true);
          glTexImage2D(target, 0, internalFormat, width, height, border, format, usedType, data);
        } else {
          gluBuild2DMipmaps(target, internalFormat, width, height, format, usedType, data);
        }
      } else {
        glTexImage2D(target, level, internalFormat, width, height, border, format, usedType, data);
      }
    } catch (@Nonnull final CoreGLException ex) {
      if (textureId == -1) {
        GL11.glDeleteTextures(usedTextureId);
        checkGLError("glDeleteTextures", false);
      }
      throw ex;
    }

    return usedTextureId;
  }

  /**
   * Create a new texture ID.
   *
   * @return the newly generated texture ID
   * @throws CoreGLException in case generation a new texture ID fails
   */
  private static int createTextureID() {
    final int id = GL11.glGenTextures();
    checkGLError("glGenTextures", true);
    return id;
  }

  /**
   * Automatically select a type in case its requested. This function only determines the best fitting type
   * automatically in case the {@code type} parameter is set to {@link #AUTO}.
   *
   * @param type the selected type of the pixel data
   * @param data the pixel data
   * @return the value of {@code type} or a automatically determined type
   * @throws CoreGLException in case the automated detection of the type fails
   */
  private static int getType(final int type, final Buffer data) {
    if (type == AUTO) {
      if (data instanceof ByteBuffer) {
        return GL11.GL_UNSIGNED_BYTE;
      } else if (data instanceof ShortBuffer) {
        return GL11.GL_UNSIGNED_SHORT;
      } else if (data instanceof IntBuffer) {
        return GL11.GL_UNSIGNED_INT;
      } else if (data instanceof FloatBuffer) {
        return GL11.GL_FLOAT;
      } else if (data instanceof DoubleBuffer) {
        return GL11.GL_FLOAT;
      } else {
        throw new CoreGLException("Unknown buffer type; " + data.getClass().toString());
      }
    } else {
      return type;
    }
  }

  /**
   * This is a wrapper function for the actual call to {@code glTexImage2D}. It uses the generic {@link Buffer} and
   * internally casts it as needed to fit the different implementations of the {@code glTexImage2D} function.
   *
   * @param target         the target of the texture creation operation
   * @param level          the level of this texture
   * @param internalformat the internal format
   * @param width          the width of the texture
   * @param height         the height of the texture
   * @param border         the border width of the texture
   * @param format         the format of the pixel data
   * @param type           the data type of the pixel data
   * @param pixels         the pixel data
   * @throws CoreGLException in case OpenGL reports a error or in case the type of the buffer is unknown
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
    this.texImageInternalFormat = format;
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
      throw new CoreGLException("updateTextureData() call can only be used to update texture data");
    }

    if (pixels instanceof ByteBuffer) {
      GL11.glTexImage2D(texImageTarget, texImageLevel, texImageInternalFormat, texImageWidth, texImageHeight,
          texBorder, texFormat, texType, (ByteBuffer) pixels);
    } else if (pixels instanceof ShortBuffer) {
      GL11.glTexImage2D(texImageTarget, texImageLevel, texImageInternalFormat, texImageWidth, texImageHeight,
          texBorder, texFormat, texType, (ShortBuffer) pixels);
    } else if (pixels instanceof IntBuffer) {
      GL11.glTexImage2D(texImageTarget, texImageLevel, texImageInternalFormat, texImageWidth, texImageHeight,
          texBorder, texFormat, texType, (IntBuffer) pixels);
    } else if (pixels instanceof FloatBuffer) {
      GL11.glTexImage2D(texImageTarget, texImageLevel, texImageInternalFormat, texImageWidth, texImageHeight,
          texBorder, texFormat, texType, (FloatBuffer) pixels);
    } else if (pixels instanceof DoubleBuffer) {
      GL11.glTexImage2D(texImageTarget, texImageLevel, texImageInternalFormat, texImageWidth, texImageHeight,
          texBorder, texFormat, texType, (DoubleBuffer) pixels);
    } else {
      throw new CoreGLException("Unknown buffer type; " + pixels.getClass().toString());
    }
    checkGLError("glTexImage2D", true);
  }

  /**
   * This functions wraps the last resort function for the mipmap generation. This function only works in base
   * {@code data} is a {@link ByteBuffer}. It will create textures and its mipmaps.
   *
   * @param target     the target of the texture creation operation
   * @param components the internal texture format
   * @param width      the width of the texture
   * @param height     the height of the texture
   * @param format     the format of the pixel data
   * @param type       the type of the pixel data
   * @param data       the pixel data
   * @throws CoreGLException in case the creation of the mipmap fails
   */
  private static void gluBuild2DMipmaps(
      final int target, final int components, final int width, final int height,
      final int format, final int type, final Buffer data) {
    if (data instanceof ByteBuffer) {
      GLU.gluBuild2DMipmaps(target, components, width, height, format, type, (ByteBuffer) data);
      checkGLError("gluBuild2DMipmaps", true);
    } else {
      throw new CoreGLException("MipMap creation not supported on this platform for non-byte buffers.");
    }
  }

  /**
   * Check if mipmaps are supposed to be generated.
   *
   * @param level     the level settings
   * @param minFilter the minimizing filter
   * @return {@code true} in case mipmaps are supposed to be generated
   */
  private static boolean isCreatingMipMaps(final int level, final int minFilter) {
    if (level > 0) {
      return false;
    }

    switch (minFilter) {
      case GL11.GL_NEAREST_MIPMAP_NEAREST:
      case GL11.GL_LINEAR_MIPMAP_NEAREST:
      case GL11.GL_NEAREST_MIPMAP_LINEAR:
      case GL11.GL_LINEAR_MIPMAP_LINEAR:
        return true;

      default:
        return false;
    }
  }

  /**
   * Check if a value is power of two.
   *
   * @param n the value to check
   * @return {@code true} in case the value is power of two
   */
  private static boolean isPowerOfTwo(final int n) {
    return ((n != 0) && (n & (n - 1)) == 0);
  }
}
