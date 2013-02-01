package de.lessvoid.nifty.batch.spi;

import java.io.IOException;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Nifty BatchRenderBackend used to interface any graphics backend to the new BatchRenderDevice. This looks like the
 * existing RenderDevice interface but without all of the render methods.
 *
 * The idea of the BatchRenderDevice is to remove as much state changes as possible from rendering a Nifty scene. The
 * way this is done is through these mechanism:
 *
 * 1) A texture atlas is used to store all textures in a single large texture. The actual texture size is provided
 *    by the user but will usually be around 2048x2048 pixel. A BatchRenderBackend has to provide mechanism to create
 *    that texture and a way to load an image and put it at a given position into that big texture.
 *
 * 2) The only actual render data that Nifty will call this BatchRenderBackend with are textured quads with individual
 *    vertex colors. Nifty will provide the texture coordinates which will always be relative to the texture atlas
 *    created before. A BatchRenderBackend implementation should cache or send these quads to the GPU to be later
 *    rendered in a single draw call.
 *
 * 3) The batch size, e.g. how many quads will fit into a single batch is decided by the BatchRenderBackend. The method
 *    to add a quad to the batch can return false to notify Nifty that a new batch should be created - by calling the
 *    appropriate method on this BatchRenderBackend for this purpose.
 *
 * 4) Nifty will only start a new batch when the glBlendMode will change. In this case it will start a new Batch.
 *
 * @author void
 */
public interface BatchRenderBackend {

  /**
   * Gives this RenderDevice access to the NiftyResourceLoader so that the same paths can be used for resolving
   * resources as Nifty would do.
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(NiftyResourceLoader niftyResourceLoader);

  /**
   * Get width of the display mode.
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get height of the display mode.
   * @return height of display mode
   */
  int getHeight();

  /**
   * Called every beginning of a frame.
   */
  void beginFrame();

  /**
   * Called every end of a frame.
   */
  void endFrame();

  /**
   * clear screen.
   */
  void clear();

  /**
   * Create a new mouse cursor.
   * @param filename image file for the cursor
   * @param hotspotX hotspot x with 0 being left of the screen
   * @param hotspotY hotspot y with 0 being top of the screen 
   * @return the loaded mouse cursor resource ready to be applied
   */
  MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException;

  /**
   * Enable the given mouse cursor.
   * @param mouseCursor the mouse cursor to enable
   */
  void enableMouseCursor(MouseCursor mouseCursor);

  /**
   * Disable the current mouse cursor.
   */
  void disableMouseCursor();

  /**
   * Create a texture that will later be used as the texture atlas of the given size.
   * @param width width of the texture atlas
   * @param height height of the texture atlas
   */
  void createAtlasTexture(int width, int height);

  /**
   * Load the given image and provide width and height of the image.
   * @param filename the filename to load
   * @param ImageDimension instance (could carry additional data like the actual image data if necessary)
   */
  Image loadImage(String filename);

  /**
   * Adds the given image to the main texture atlas at the given position.
   * @param image the Image data loaded by loadImage()
   * @param x the x position where to put the image
   * @param y the y position where to put the image
   */
  void addImageToTexture(Image image, int x, int y);

  /**
   * Begin a new batch with the given BlendMode.
   *
   * @param blendMode the blendMode this batch should use. This will be BlendMode.BLEND in most cases and very rare will
   * be MULTIPLY.
   */
  void beginBatch(BlendMode blendMode);

  /**
   * Render a quad with the given coordinates to the current batch. There will always be a beginBatch() call before any
   * addQuad() call. There will be lots of addQuad() calls after a beginBatch() call. However it's possible that there
   * will be multiple beginBatch() calls - there is no explicit endBatch() call. Starting a new batch with beginBatch()
   * should end the current one, store it for later rendering and start a new batch.
   *
   * This method can return false in case the quad does not fit into the current batch which might be for whatever
   * reason. In that case Nifty will begin a new batch and add the quad to the new one.
   *
   * @param x the x position in screen coordinates where to render this quad (0,0 is the left, upper corner)
   * @param y the y position in screen coordinates where to render this quad (0,0 is the left, upper corner)
   * @param width the width of the quad to render
   * @param height the height of the quad to render
   * @param color1 color of the left upper quad vertex
   * @param color2 color of the right upper quad vertex
   * @param color3 color of the right bottom quad vertex
   * @param color4 color of the left bottom quad vertex
   * @param textureX texture coordinate x of the left side (already normalized in the range 0 to 1)
   * @param textureY texture coordinate y of the upper line (already normalized in the range 0 to 1)
   * @param textureWidth texture width (already normalized in the range 0 to 1)
   * @param textureHeight texture height (already normalized in the range 0 to 1)
   * @return true if the quad has been added successfully and false if not
   */
  boolean addQuad(
      float x,
      float y,
      float width,
      float height,
      Color color1,
      Color color2,
      Color color3,
      Color color4,
      float textureX,
      float textureY,
      float textureWidth,
      float textureHeight);

  /**
   * Render all batches.
   */
  void render();

  /**
   * Helper interface to allow the provideImageDimensions() method to return the image dimension and if necessary
   * additional data not visible to Nifty.
   * @author void
   */
  public interface Image {
    int getWidth();
    int getHeight();
  }
}
