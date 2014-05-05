package de.lessvoid.nifty.render;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * NiftyRenderEngine interface. This is considered a private API. Use methods on the main Nifty instance instead.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface NiftyRenderEngine {
  /**
   * Get Width of Display mode. This will always return the base resolution if auto scaling is enabled.
   *
   * @return width of display mode
   */
  int getWidth();

  /**
   * Get Height of Display mode. This will always return the base resolution if auto scaling is enabled.
   *
   * @return height of display mode
   */
  int getHeight();

  /**
   * This will always return the current native display resolution independent of the auto scaling mode.
   *
   * @return the native display width
   */
  int getNativeWidth();

  /**
   * This will always return the current native display resolution independent of the auto scaling mode.
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
   * Create a new Image. Attention: use nifty.createImage() instead! This method has changed in Nifty 1.3.3 - sorry :)
   * You should probably never need to call methods on the NiftyRenderEngine directly though.
   *
   * @param screen       the Screen this image is connected to
   * @param name         file name to use
   * @param filterLinear filter
   * @return the created nifty image or {@code null} if loading a image with a assigned name failed
   */
  @Nullable
  NiftyImage createImage(@Nonnull Screen screen, @Nonnull String name, boolean filterLinear);

  /**
   * Create a new RenderFont.
   *
   * @param name name of the font
   * @return RenderFont instance
   */
  @Nullable
  RenderFont createFont(@Nonnull String name);

  /**
   * Returns the original filename of the given RenderFont.
   *
   * @param font RenderFont to get the name from
   * @return the filename of the font
   * @throws IllegalArgumentException in case the render font was not load by the engine and can't be matched to a
   *                                  file name
   */
  @Nonnull
  String getFontname(@Nonnull RenderFont font);

  /**
   * Render a quad.
   */
  void renderQuad(int x, int y, int width, int height);

  /**
   * Renders a quad with different colors at the quad vertices.
   */
  void renderQuad(
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color topLeft,
      @Nonnull Color topRight,
      @Nonnull Color bottomRight,
      @Nonnull Color bottomLeft);

  /**
   * Render Image.
   *
   * @param image  the image to render
   * @param x      the x position on the screen
   * @param y      the y position on the screen
   * @param width  the width
   * @param height the height
   */
  void renderImage(@Nonnull NiftyImage image, int x, int y, int width, int height);

  /**
   * renderText.
   *
   * @param text               text
   * @param x                  x
   * @param y                  y
   * @param selectionStart     selection start
   * @param selectionEnd       selection end
   * @param textSelectionColor color for text selections
   */
  void renderText(
      @Nonnull String text,
      int x,
      int y,
      int selectionStart,
      int selectionEnd,
      @Nonnull Color textSelectionColor);

  /**
   * Set the font that is supposed to be used to render a text.
   * <p/>
   * In case the font is never set or unloaded by setting it to {@code null} any attempt to render a text will fail.
   *
   * @param font the font to use or {@code null} to unload the font
   */
  void setFont(@Nullable RenderFont font);

  /**
   * Get the font that is currently applied to the engine and used for all text rendering operations. This may be
   * {@code null} in case no font is set.
   *
   * @return font the font to use
   */
  @Nullable
  RenderFont getFont();

  /**
   * Set the color used for the next rendering operations. In case the rendering functions have own color arguments,
   * this color value is not used.
   *
   * @param colorParam new color value to use, this instance is not stored in the engine,
   *                   rather its values are copied to a internal storage
   */
  void setColor(@Nonnull Color colorParam);

  /**
   * Set only the alpha value of the current color to a new value.
   *
   * @param newColorAlpha new alpha value
   */
  void setColorAlpha(float newColorAlpha);

  /**
   * Set only the color component of the given color. This will not apply the alpha value.
   *
   * @param color color the new color value
   */
  void setColorIgnoreAlpha(@Nonnull Color color);

  /**
   * Check if the color got changed since the last start of a frame.
   *
   * @return {@code true} if the color got changed since starting the frame
   */
  boolean isColorChanged();

  /**
   * Check if the alpha component of the color got changed since the start of the current frame.
   *
   * @return {@code true} in case the alpha component got changed
   */
  boolean isColorAlphaChanged();

  /**
   * Set BlendMode.
   */
  void setBlendMode(@Nonnull BlendMode blendMode);

  /**
   * Move to the given x/y position.
   */
  void moveTo(float xParam, float yParam);

  /**
   * Move to the given x/y position but respect any previously set position using moveTo.
   *
   * @param xParam x
   * @param yParam y
   */
  void moveToRelative(float xParam, float yParam);

  /**
   * Enable clipping to the given region.
   */
  void enableClip(int x0, int y0, int x1, int y1);

  /**
   * Clip an absolute region
   * @see Nifty#setAbsoluteClip(int, int, int, int) 
   * @param x0 X coordinates of left-upper corner
   * @param y0 Y coordinates of left-upper corner
   * @param x1 X coordinates of right-bottom corner
   * @param y1 Y coordinates of right-bottom corner
   */
  void setAbsoluteClip(int x0,int y0,int x1,int y1);
  
  void applyAbsoluteClip();

  /**
   * Disable absolute clipping.
   */
  void disableAbsoluteClip();

  /**
   * Disable the clipping.
   */
  void disableClip();

  /**
   * Set RenderTextSize.
   *
   * @param size size
   */
  void setRenderTextSize(float size);

  /**
   * set image size.
   *
   * @param scale new image size
   */
  void setImageScale(float scale);

  /**
   * set global position.
   */
  void setGlobalPosition(float xPos, float yPos);

  /**
   * Save all internal state values of the render engine.
   * <p/>
   * The saved state is stored inside a stack. Calling the {@link #restoreStates()} function retrieves the last
   * stored value from the stack and restores it.
   * <p/>
   * The stack has to be cleared out until the {@link #beginFrame()} function is called next,
   * else the states will be wiped and a severe warning is raised.
   */
  void saveStates();

  /**
   * Restore the last saved state.
   *
   * @throws IllegalStateException in case the stack of stored states is empty
   */
  void restoreStates();

  /**
   * Get the render device used by the engine.
   *
   * @return the render device
   */
  @Nonnull
  RenderDevice getRenderDevice();

  /**
   * Dispose image.
   *
   * @param image image to dispose
   */
  void disposeImage(@Nonnull RenderImage image);

  /**
   * Dispose the given image and reload it.
   *
   * @param image image
   * @return the reloaded image
   */
  @Nonnull
  RenderImage reload(@Nonnull RenderImage image);

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

  /**
   * Called by Nifty when the given screen has started.
   *
   * @param screen the screen that has just started
   */
  void screenStarted(@Nonnull Screen screen);

  /**
   * Called by Nifty when the given screen has ended.
   *
   * @param screen the screen that has just ended
   */
  void screenEnded(@Nonnull Screen screen);

  /**
   * All screens are about to be removed because a new XML is being loaded.
   *
   * @param screens the collection of Screens that will be removed
   */
  void screensClear(@Nonnull Collection<Screen> screens);

  /**
   * The given Screen has been added.
   *
   * @param screen the added Screen
   */
  void screenAdded(@Nonnull Screen screen);

  /**
   * The given Screen has been removed.
   *
   * @param screen the removed Screen
   */
  void screenRemoved(@Nonnull Screen screen);
}
