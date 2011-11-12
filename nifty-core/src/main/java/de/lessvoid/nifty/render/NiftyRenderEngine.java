package de.lessvoid.nifty.render;

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * NiftyRenderEngine interface.
 * @author void
 */
public interface NiftyRenderEngine {

  /**
   * Get Width of Display mode. This will always return the base resolution
   * if auto scaling is enabled.
   *
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height of Display mode. This will always return the base resolution
   * if auto scaling is enabled.
   *
   * @return height of display mode
   */
  int getHeight();

  /**
   * This will always return the current native display resolution independent
   * of the auto scaling mode.
   *
   * @return the native display width
   */
  int getNativeWidth();

  /**
   * This will always return the current native display resolution independent
   * of the auto scaling mode.
   *
   * @return the native display height
   */
  int getNativeHeight();

  /**
   * Called when a frame begins.
   */
  void beginFrame();

  /**
   * Called when a frame ends.
   */
  void endFrame();

  /**
   * Clear the screen.
   */
  void clear();

  /**
   * Create a new Image.
   * @param name file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  NiftyImage createImage(String name, boolean filterLinear);

  /**
   * Create a new RenderFont.
   * @param name name of the font
   * @return RenderFont instance
   */
  RenderFont createFont(String name);

  /**
   * Returns the original filename of the given RenderFont.
   * @param font RenderFont to get the name from
   * @return the filename of the font
   */
  String getFontname(RenderFont font);

  /**
   * render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  void renderQuad(int x, int y, int width, int height);

  /**
   * Renders a quad with different colors at the quad vertices.
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
   * Render Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param width the width
   * @param height the height
   */
  void renderImage(NiftyImage image, int x, int y, int width, int height);

  /**
   * renderText.
   * @param text text
   * @param x x
   * @param y y
   * @param selectionStart selection start
   * @param selectionEnd selection end
   * @param textSelectionColor color for text selections
   */
  void renderText(String text, int x, int y, int selectionStart, int selectionEnd, Color textSelectionColor);

  /**
   * set font.
   * @param font font
   */
  void setFont(RenderFont font);

  /**
   * get font.
   * @return font
   */
  RenderFont getFont();

  /**
   * Set a new color.
   * @param colorParam new current color to set
   */
  void setColor(Color colorParam);

  /**
   * set only the color alpha.
   * @param newColorAlpha new alpha value
   */
  void setColorAlpha(float newColorAlpha);

  /**
   * Set only the color component of the given color. This assumes that alpha has already been changed.
   * @param color color
   */
  void setColorIgnoreAlpha(Color color);

  /**
   * return true when color has been changed.
   * @return color changed
   */
  boolean isColorChanged();

  /**
   * return true when color alpha has been changed.
   * @return color changed
   */
  boolean isColorAlphaChanged();

  /**
   * Set BlendMode.
   */
  void setBlendMode(BlendMode blendMode);

  /**
   * Move to the given x/y position.
   * @param xParam x
   * @param yParam y
   */
  void moveTo(float xParam, float yParam);

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Disable the clipping.
   */
  void disableClip();

  /**
   * Set RenderTextSize.
   * @param size size
   */
  void setRenderTextSize(float size);

  /**
   * set image size.
   * @param scale new image size
   */
  void setImageScale(float scale);

  /**
   * set global position.
   * @param xPos x
   * @param yPos y
   */
  void setGlobalPosition(float xPos, float yPos);

  /**
   * save given states.
   * @param statesToSave set of renderstates to save
   */
  void saveState(RenderStates statesToSave);

  /**
   * restore states.
   */
  void restoreState();

  /**
   * Get RenderDevice.
   * @return
   */
  RenderDevice getRenderDevice();

  /**
   * Dispose image.
   * @param image image to dispose
   */
  void disposeImage(RenderImage image);

  /**
   * Dispose the given image and reload it.
   * @param image image
   * @return the reloaded image
   */
  RenderImage reload(RenderImage image);

  /**
   * This is called from Nifty when it receives the resolutionChange notify from application code.
   * The RenderEngine will update the cached values of width/height from the RenderDevice it has
   * stored inside.
   */
  void displayResolutionChanged();

  void enableAutoScaling(int baseResolutionX, int baseResolutionY);
  void enableAutoScaling(int baseResolutionX, int baseResolutionY, float scaleX, float scaleY);
  void disableAutoScaling();

  int convertToNativeX(int x);
  int convertToNativeY(int y);
  int convertToNativeWidth(int x);
  int convertToNativeHeight(int y);
  int convertFromNativeX(int x);
  int convertFromNativeY(int y);
  float convertToNativeTextSizeX(float size);
  float convertToNativeTextSizeY(float size);

}
