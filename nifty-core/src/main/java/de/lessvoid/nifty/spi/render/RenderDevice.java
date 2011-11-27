package de.lessvoid.nifty.spi.render;

import java.io.IOException;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Nifty RenderDevice.
 * @author void
 */
public interface RenderDevice {

  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(NiftyResourceLoader niftyResourceLoader);

  /**
   * Create a new RenderImage.
   * @param filename filename
   * @param filterLinear filter
   * @return RenderImage
   */
  RenderImage createImage(String filename, boolean filterLinear);

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  RenderFont createFont(String filename);

  /**
   * Get Width.
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height.
   * @return height of display mode
   */
  int getHeight();

  /**
   * Called every begin frame.
   */
  void beginFrame();

  /**
   * Called every end frame.
   */
  void endFrame();

  /**
   * clear screen.
   */
  void clear();

  /**
   * Change the RenderMode to the given Mode.
   * @param renderMode RenderMode
   */
  void setBlendMode(BlendMode renderMode);

  /**
   * Render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   */
  void renderQuad(int x, int y, int width, int height, Color color);

  /**
   * Render a quad with different colors at the vertices.
   * @param x
   * @param y
   * @param width
   * @param height
   * @param topLeft
   * @param topRight
   * @param bottomRight
   * @param bottomLeft
   */
  void renderQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft);

  /**
   * Render the image.
   * @param x x
   * @param y y
   * @param width w
   * @param height h
   * @param color color
   * @param imageScale image scale
   */
  void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float imageScale);

  /**
   * Render a sub image of this image.
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @param srcX source x
   * @param srcY source y
   * @param srcW source width
   * @param srcH source height
   * @param color color
   */
  void renderImage(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH, Color color, float scale, int centerX, int centerY);

  /**
   * Render the given text at the given position.
   * @param text text to render
   * @param x x position
   * @param y y position
   * @param fontColor font color
   * @param size size
   */
  void renderFont(RenderFont font, String text, int x, int y, Color fontColor, float sizeX, float sizeY);

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Disable Clipping.
   */
  void disableClip();

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
}
