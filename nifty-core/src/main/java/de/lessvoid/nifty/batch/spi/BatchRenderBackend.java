package de.lessvoid.nifty.batch.spi;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Nifty BatchRenderBackend used to interface any graphics backend to the new BatchRenderDevice.
 * <p/>
 * The idea of the BatchRenderDevice is to prevent as many state changes as possible while rendering a Nifty scene.
 * <p/>
 * Details:
 * <p/>
 * 1) Texture atlases are used to pack as many textures as possible into single large textures. The actual texture
 *    atlas sizes are provided by the user but will usually be around 2048x2048 pixels. A BatchRenderBackend has to
 *    provide mechanisms to create atlas textures, load images, and insert them at a given position into the specified
 *    atlas texture. It also must provide a way to create and render non-atlas textures with a separate draw call for
 *    each. Non-atlas textures will each have their own batch since they are a separate texture and therefore cannot be
 *    rendered at the same time as sub-textures of an atlas.
 * <p/>
 * 2) The only actual render data that Nifty will call this BatchRenderBackend with are textured quads with individual
 *    vertex colors. Nifty will provide the texture coordinates which will either be relative to the specified texture
 *    atlas, or in the case of non-atlas textures, relative to the texture itself. A BatchRenderBackend implementation
 *    should cache or send these quads to the GPU to be later rendered in as few draw calls as possible when Nifty
 *    calls {@link #render()} on a BatchRenderBackend implementation.
 * <p/>
 * 3) The batch size, e.g. how many quads will fit into a single batch is decided by the BatchRenderBackend
 *    implementation. If a quad does not fit into the current batch the BatchRenderBackend implementation should create
 *    a new batch automatically.
 * <p/>
 * 4) Nifty will start a new batch when the BlendMode changes or when changing textures (i.e., switching between
 *    multiple atlases or between an atlas and a non-atlas texture).
 * <p/>
 * 5) All quads will always be called in render order from back to front.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface BatchRenderBackend {
  /**
   * Gives this RenderDevice access to the NiftyResourceLoader so that the same paths can be used for resolving
   * resources as Nifty would do.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * Gets width of the display mode.
   *
   * @return the width of display mode
   */
  int getWidth();

  /**
   * Gets height of the display mode.
   *
   * @return the height of display mode
   */
  int getHeight();

  /**
   * Called at the beginning of every frame.
   */
  void beginFrame();

  /**
   * Called at the end of every frame.
   */
  void endFrame();

  /**
   * Clears the screen.
   */
  void clear();

  /**
   * Creates a new mouse cursor.
   *
   * @param filename image file for the cursor
   * @param hotspotX hotspot x with 0 being left of the screen
   * @param hotspotY hotspot y with 0 being top of the screen
   *
   * @return the loaded mouse cursor resource ready to be applied
   */
  @Nullable
  MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException;

  /**
   * Enables the given mouse cursor.
   *
   * @param mouseCursor the mouse cursor to enable
   */
  void enableMouseCursor(@Nonnull MouseCursor mouseCursor);

  /**
   * Disables the current mouse cursor.
   */
  void disableMouseCursor();

  /**
   * Creates a new texture atlas.
   * 
   * @param atlasWidth the width of the atlas texture
   * @param atlasHeight the height of the atlas texture
   *
   * @return the texture id of the new atlas texture
   */
  int createTextureAtlas(int atlasWidth, int atlasHeight);

  /**
   * Clears an entire texture atlas.
   *
   * @param atlasTextureId the texture id of the atlas texture to clear
   */
  void clearTextureAtlas(int atlasTextureId);

  /**
   * Loads the given image (provide the size of the image using the {@link BatchRenderBackend.Image} interface.
   * If the image fails to load, the implementation must return a non-null instance.
   *
   * @param filename the filename to load
   */
  @Nonnull
  Image loadImage(@Nonnull String filename);

  /**
   * Wraps the given buffer into an Image interface;
   */
  @Nullable
  Image loadImage(@Nonnull ByteBuffer imageData, int imageWidth, int imageHeight);

  /**
   * Adds the given image to the specified texture atlas at the given position.
   *
   * @param image the Image data loaded by loadImage()
   * @param atlasX the x position in the atlas to insert the image at
   * @param atlasY the y position in the atlas to insert the image at
   * @param atlasTextureId the texture id of the atlas texture to add the image to
   */
  void addImageToAtlas(Image image, int atlasX, int atlasY, int atlasTextureId);

  /**
   * Creates the given image as a separate, non-atlas texture. This will be called when the texture is not within atlas
   * tolerance, i.e., it would take up too large a percentage of the atlas. This is especially useful when dealing with
   * large textures, such as high-res background images.
   *
   * @param image the Image data loaded by loadImage()
   *
   * @return the texture id of the non-atlas texture
   */
  int createNonAtlasTexture(@Nonnull Image image);

  /**
   * Deletes the separate, non-atlas texture from memory. This will be called when a texture that was created
   * with {@link #createNonAtlasTexture(de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image)} needs to be deleted.
   *
   * @param textureId the texture id of the non-atlas texture
   */
  void deleteNonAtlasTexture(int textureId);

  /**
   * Checks whether the non-atlas texture with a texture id specified by textureId exists, i.e. it was created
   * successfully by {@link #createNonAtlasTexture(de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image)}.
   *
   * @param textureId the texture id of the non-atlas texture
   *
   * @return true if the texture exists, false otherwise
   */
  boolean existsNonAtlasTexture(int textureId);

  /**
   * Adds a quad textured with a sub-texture from a texture atlas, or for non-atlas quads, adds a quad textured with
   * the non-atlas texture to a new batch. There will always be a beginBatch() call before any addQuad() call. There
   * will usually be many addQuad() calls after a beginBatch() call, but not necessarily. Non-atlas texture batches may
   * have as few as one addQuad() call.
   *
   * @param x the x position in screen coordinates to render this quad at (0,0 is the top left corner)
   * @param y the y position in screen coordinates to render this quad at (0,0 is the top left corner)
   * @param width the width of the quad to render
   * @param height the height of the quad to render
   * @param color1 the color of the left upper quad vertex
   * @param color2 the color of the right upper quad vertex
   * @param color3 the color of the right bottom quad vertex
   * @param color4 the color of the left bottom quad vertex
   * @param textureX x coordinate of the top left corner of the sub-texture within the atlas texture, or, for non-atlas
   *                 textures, of a sub-texture within the non-atlas texture (already normalized in the range 0 to 1 in
   *                 uv texture atlas coordinates, or for non-atlas textures, in local uv texture coordinates)
   * @param textureY y coordinate of the top left corner of the sub-texture within the atlas texture, or, for non-atlas
   *                 textures, of a sub-texture within the non-atlas texture (already normalized in the range 0 to 1 in
   *                 uv texture atlas coordinates, or for non-atlas textures, in local uv texture coordinates)
   * @param textureWidth the width of the texture (already normalized in the range 0 to 1 in uv atlas space or, for
   *                     non-atlas textures, in local uv texture space)
   * @param textureHeight the height of the texture (already normalized in the range 0 to 1 in uv atlas space, or, for
   *                      non-atlas textures, in local uv texture space)
   * @param textureId the id of the texture
   */
  void addQuad(
      float x,
      float y,
      float width,
      float height,
      @Nonnull Color color1,
      @Nonnull Color color2,
      @Nonnull Color color3,
      @Nonnull Color color4,
      float textureX,
      float textureY,
      float textureWidth,
      float textureHeight,
      int textureId);

  /**
   * Begins a new batch with the given BlendMode. Starting a new batch with beginBatch() should store the current batch
   * for later rendering and should start a new batch.
   *
   * @param blendMode the blending mode this batch should use (will be BlendMode.BLEND in most cases and only very
   *                  rarely will it be BlendMode.MULTIPLY.
   * @param textureId the id of the texture that all drawing in this batch will bind to - usually this will be the id
   *                  of a texture atlas, but in the case of non-atlas textures, we need a separate batch for each
   *                  one, so textureId will be the id of that single, non-atlas texture
   */
  void beginBatch(@Nonnull BlendMode blendMode, int textureId);

  /**
   * Renders all batches.
   *
   * @return the number of batches rendered for statistics.
   */
  int render();

  /**
   * Removes the specified image from the specified texture atlas. This really could be an empty implementation because
   * there really is nothing that this method needs to do. After a call to this method Nifty might reuse the place in
   * the texture with other calls to
   * {@link #addImageToAtlas(de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image, int, int, int)}.
   *
   * @param image the image to remove from the atlas
   * @param atlasX the x position in the atlas to insert the image at
   * @param atlasY the y position in the atlas to insert the image at
   * @param imageWidth the width of the image in the atlas
   * @param imageHeight the height of the image in the atlas
   * @param atlasTextureId the texture id of the atlas texture to remove the image from
   *
   */
  void removeImageFromAtlas(@Nonnull Image image, int atlasX, int atlasY, int imageWidth, int imageHeight, int atlasTextureId);

  /**
   * Whether or not to render textures with high quality settings. Usually, setting to true will result in slower
   * performance, but nicer looking textures, and vice versa. How high quality textures are rendered versus low quality
   * textures will vary depending on the {@link de.lessvoid.nifty.batch.spi.BatchRenderBackend} implementation.
   */
  void useHighQualityTextures(final boolean shouldUseHighQualityTextures);

  /**
   * Whether or not to overwrite previously used atlas space with blank data. Setting to true will result in slower
   * performance, but may be useful in debugging when visually inspecting the atlas, since there will not be portions
   * of old images visible in currently unused atlas space.
   */
  void fillRemovedImagesInAtlas(final boolean shouldFill);

  /**
   * Helper interface to pass the image size and any other custom data from {@link #loadImage(String)} to
   * {@link #addImageToAtlas(de.lessvoid.nifty.batch.spi.BatchRenderBackend.Image, int, int, int)}.
   *
   * @author void
   */
  public interface Image {
    int getWidth();
    int getHeight();
  }

  /**
   * Generic implementation of Image interface backed up with byte buffer as main or optional storage.
   * @author iamtakingiteasy
   */
  public static class ByteBufferedImage implements Image {
    protected final ByteBuffer buffer;
    protected final int width;
    protected final int height;

    public ByteBufferedImage() {
      this(null, 0, 0);
    }

    public ByteBufferedImage(ByteBuffer buffer, int width, int height) {
      this.buffer = buffer;
      this.width = width;
      this.height = height;
    }

    @Override
    public int getWidth() {
      return width;
    }

    @Override
    public int getHeight() {
      return height;
    }

    public ByteBuffer getBuffer() {
      return buffer;
    }
  }
}
