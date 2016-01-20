package de.lessvoid.nifty.slick2d.render;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.slick2d.loaders.SlickMouseCursorLoaders;
import de.lessvoid.nifty.slick2d.loaders.SlickRenderFontLoaders;
import de.lessvoid.nifty.slick2d.loaders.SlickRenderImageLoaders;
import de.lessvoid.nifty.slick2d.render.cursor.SlickMouseCursor;
import de.lessvoid.nifty.slick2d.render.font.SlickRenderFont;
import de.lessvoid.nifty.slick2d.render.image.SlickRenderImage;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.renderer.Renderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The render device that takes care for rendering the Nifty GUI inside of Slick.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 *
 * @deprecated Use {@link de.lessvoid.nifty.render.batch.BatchRenderDevice} with
 * {@link de.lessvoid.nifty.slick2d.render.batch.SlickBatchRenderBackendFactory#create(GameContainer)}.
 */
@Deprecated
public final class SlickRenderDevice implements RenderDevice {
  /**
   * The mouse cursor that is currently active.
   */
  @Nullable
  private MouseCursor activeMouseCursor = null;

  /**
   * The game container that hosts the render area the Nifty GUI is supposed to be rendered inside
   */
  @Nonnull
  private final GameContainer gameContainer;

  /**
   * The shape filling instance that is used to render the 4 colored rectangles.
   */
  @Nullable
  private SlickQuadFill quadFill = null;

  /**
   * This temporary slick color is just used to avoid the need to create Slick Color instances again and again for a
   * short time while rendering.
   */
  @Nonnull
  private final org.newdawn.slick.Color tempSlickColor;

  /**
   * Create a new render device and set the game container used to render the GUI inside.
   *
   * @param container the game container
   */
  public SlickRenderDevice(@Nonnull final GameContainer container) {
    gameContainer = container;
    tempSlickColor = new org.newdawn.slick.Color(0.0f, 0.0f, 0.0f, 0.0f);
  }

  /**
   * Start rendering a new frame.
   */
  @Override
  public void beginFrame() {
    disableClip();
    setBlendMode(BlendMode.BLEND);
  }

  /**
   * Clear the image.
   */
  @Override
  public void clear() {
    gameContainer.getGraphics().clear();
  }

  /**
   * Create a new font that can be rendered on the screen.
   */
  @Override
  public RenderFont createFont(@Nonnull final String filename) {
    return SlickRenderFontLoaders.getInstance().loadFont(gameContainer.getGraphics(), filename);
  }

  @Override
  public RenderImage createImage(@Nonnull final String filename, final boolean filterLinear) {
    return SlickRenderImageLoaders.getInstance().loadImage(filename, filterLinear);
  }

  /**
   * Create a new mouse cursor.
   */
  @Override
  public MouseCursor createMouseCursor(@Nonnull final String filename, final int hotspotX, final int hotspotY) {
    return SlickMouseCursorLoaders.getInstance().loadCursor(filename, hotspotX, hotspotY, gameContainer);
  }

  /**
   * Disable clipping.
   */
  @Override
  public void disableClip() {
    gameContainer.getGraphics().clearClip();
  }

  /**
   * Disable the current mouse cursor.
   */
  @Override
  public void disableMouseCursor() {
    if (activeMouseCursor != null) {
      activeMouseCursor.disable();
      activeMouseCursor = null;
    }
  }

  /**
   * Enable clipping to a specified area on the screen.
   */
  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    gameContainer.getGraphics().setClip(x0, y0, x1 - x0, y1 - y0);
  }

  /**
   * Enable the mouse cursor.
   */
  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    activeMouseCursor = mouseCursor;
    activeMouseCursor.enable();
  }

  /**
   * Finish rendering a frame.
   */
  @Override
  public void endFrame() {
    if (activeMouseCursor instanceof SlickMouseCursor) {
      final Input input = gameContainer.getInput();
      ((SlickMouseCursor)activeMouseCursor).render(gameContainer.getGraphics(), input.getMouseX(), input.getMouseY());
    }
  }

  /**
   * Get the height of the game.
   */
  @Override
  public int getHeight() {
    return gameContainer.getHeight();
  }

  /**
   * Get the width of the screen.
   */
  @Override
  public int getWidth() {
    return gameContainer.getWidth();
  }

  /**
   * Draw some text on the screen using a specified font.
   */
  @Override
  public void renderFont(
      @Nonnull final RenderFont font,
      @Nonnull final String text,
      final int x,
      final int y,
      @Nonnull final Color color,
      final float fontSizeX,
      final float fontSizeY) {

    if (!(font instanceof SlickRenderFont)) {
      throw new IllegalArgumentException("Invalid font implementation.");
    }

    final SlickRenderFont slickFont = (SlickRenderFont) font;
    slickFont.renderText(gameContainer.getGraphics(), text, x, y, color, fontSizeX, fontSizeY);
  }

  /**
   * Render a image on the screen.
   */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color color,
      final float scale) {

    if (!(image instanceof SlickRenderImage)) {
      throw new IllegalArgumentException("Invalid render image implementation");
    }

    final SlickRenderImage slickImage = (SlickRenderImage) image;
    slickImage.renderImage(gameContainer.getGraphics(), x, y, width, height, color, scale);
  }

  /**
   * Render a part of a image on the screen.
   */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      @Nonnull final Color color,
      final float scale,
      final int centerX,
      final int centerY) {

    if (!(image instanceof SlickRenderImage)) {
      throw new IllegalArgumentException("Invalid render image implementation");
    }

    final SlickRenderImage slickImage = (SlickRenderImage) image;
    slickImage.renderImage(gameContainer.getGraphics(), x, y, w, h, srcX, srcY, srcW, srcH, color, scale, centerX,
        centerY);
  }

  /**
   * Render a rectangle with a single color.
   */
  @Override
  public void renderQuad(final int x, final int y, final int width, final int height, @Nonnull final Color color) {
    gameContainer.getGraphics().setColor(SlickRenderUtils.convertColorNiftySlick(color, tempSlickColor));
    gameContainer.getGraphics().fillRect(x, y, width, height);
  }

  /**
   * Render a rectangle with different colors on each edge.
   */
  @Override
  public void renderQuad(
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color topLeft,
      @Nonnull final Color topRight,
      @Nonnull final Color bottomRight,
      @Nonnull final Color bottomLeft) {

    if (quadFill == null) {
      quadFill = new SlickQuadFill(topLeft, topRight, bottomLeft, bottomRight);
    } else {
      quadFill.changeColors(topLeft, topRight, bottomLeft, bottomRight);
    }

    final Rectangle rect = new Rectangle(x, y, width, height);
    gameContainer.getGraphics().fill(rect, quadFill);
  }

  /**
   * Set the blending mode that is used when rendering on the screen.
   */
  @Override
  public void setBlendMode(@Nonnull final BlendMode renderMode) {
    final Graphics g = gameContainer.getGraphics();

    if (renderMode == BlendMode.BLEND) {
      g.setDrawMode(Graphics.MODE_NORMAL);
    } else {
      g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
      Renderer.get().glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
    }
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    // resource loader is not used
  }
}
