package de.lessvoid.nifty.renderer.jogl.render;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import java.awt.*;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JoglRenderDevice implements RenderDevice {
  @Nonnull
  private static final Logger log = Logger.getLogger(JoglRenderDevice.class.getName());
  @Nonnull
  private static final IntBuffer viewportBuffer = Buffers.newDirectIntBuffer(4);
  @Nullable
  private RenderFont fpsFont;
  @Nullable
  private BlendMode currentBlendMode = null;
  @Nullable
  private MouseCursor mouseCursor;
  private NiftyResourceLoader resourceLoader;
  // we keep track of which GL states we've already set to make sure we don't set
  // the same state twice.
  private boolean currentTexturing = true;
  private long time;
  private long frames;
  private long lastFrames;
  private boolean displayFPS = false;
  private boolean logFPS = false;
  private boolean currentClipping = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;

  /**
   * The standard constructor. You'll use this in production code. Using this constructor will
   * configure the RenderDevice to not log FPS on System.out.
   */
  public JoglRenderDevice() {
    time = System.currentTimeMillis();
    frames = 0;
  }

  /**
   * The development mode constructor allows to display the FPS on screen when the given flag is
   * set to true. Note that setting displayFPS to false will still log the FPS on System.out every
   * couple of frames.
   *
   * @param displayFPS display the FPS counter on the screen
   */
  public JoglRenderDevice(final boolean displayFPS) {
    this();
    this.logFPS = true;
    this.displayFPS = displayFPS;
    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }
  }

  /**
   * Get Width.
   *
   * @return width of display mode
   */
  @Override
  public int getWidth() {
    return getViewport().x;
  }

  /**
   * Get Height.
   *
   * @return height of display mode
   */
  @Override
  public int getHeight() {
    return getViewport().y;
  }

  @Nonnull
  private Point getViewport() {
    final GL gl = GLContext.getCurrentGL();
    gl.glGetIntegerv(GL.GL_VIEWPORT, viewportBuffer);
    Point dimensions = new Point(viewportBuffer.get(2), viewportBuffer.get(3));
    log.fine("Viewport: " + dimensions.x + ", " + dimensions.y);
    return dimensions;
  }

  @Override
  public void beginFrame() {
    log.fine("beginFrame()");

    final GL gl = GLContext.getCurrentGL();
    // set inital states for each frame
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    setBlendMode(BlendMode.BLEND);

    gl.glEnable(GL.GL_TEXTURE_2D);
    currentTexturing = true;

    gl.glDisable(GL.GL_SCISSOR_TEST);
    currentClipping = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = 0;
    currentClippingY1 = 0;
  }

  @Override
  public void endFrame() {
    log.fine("endFrame");
    frames++;
    long diff = System.currentTimeMillis() - time;
    if (diff >= 1000) {
      time += diff;
      lastFrames = frames;
      if (logFPS) {
        System.out.println("fps: " + frames);
      }
      frames = 0;
    }
    if (displayFPS && fpsFont != null) {
      renderFont(fpsFont, "FPS: " + String.valueOf(lastFrames), 10,
          getHeight() - fpsFont.getHeight() - 10, Color.WHITE, 1.0f, 1.0f);
    }
  }

  @Override
  public void clear() {
    log.fine("clear()");
    final GL gl = GLContext.getCurrentGL();
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
  }

  /**
   * Create a new RenderImage.
   *
   * @param filename     filename
   * @param filterLinear linear filter the image
   * @return RenderImage
   */
  @Override
  public RenderImage createImage(@Nonnull final String filename, final boolean filterLinear) {
    return new JoglRenderImage(filename, filterLinear, resourceLoader);
  }

  /**
   * Create a new RenderFont.
   *
   * @param filename filename
   * @return RenderFont
   */
  @Override
  public RenderFont createFont(@Nonnull final String filename) {
    return new JoglRenderFont(filename, this, resourceLoader);
  }

  /**
   * Render a quad.
   *
   * @param x      x
   * @param y      y
   * @param width  width
   * @param height height
   * @param color  color
   */
  @Override
  public void renderQuad(
      final int x, final int y, final int width, final int height,
      @Nonnull final Color color) {
    log.fine("renderQuad()");
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    if (currentTexturing) {
      gl.glDisable(GL.GL_TEXTURE_2D);
      currentTexturing = false;
    }

    gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    gl.glBegin(GL2.GL_QUADS);
    gl.glVertex2i(x, y);
    gl.glVertex2i(x + width, y);
    gl.glVertex2i(x + width, y + height);
    gl.glVertex2i(x, y + height);
    gl.glEnd();
  }

  @Override
  public void renderQuad(
      final int x, final int y, final int width, final int height,
      @Nonnull final Color topLeft, @Nonnull final Color topRight, @Nonnull final Color bottomRight,
      @Nonnull final Color bottomLeft) {
    log.fine("renderQuad2()");
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    if (currentTexturing) {
      gl.glDisable(GL.GL_TEXTURE_2D);
      currentTexturing = false;
    }
    gl.glBegin(GL2.GL_QUADS);
    gl.glColor4f(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha());
    gl.glVertex2i(x, y);
    gl.glColor4f(topRight.getRed(), topRight.getGreen(), topRight.getBlue(),
        topRight.getAlpha());
    gl.glVertex2i(x + width, y);
    gl.glColor4f(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(),
        bottomRight.getAlpha());
    gl.glVertex2i(x + width, y + height);
    gl.glColor4f(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(),
        bottomLeft.getAlpha());
    gl.glVertex2i(x, y + height);
    gl.glEnd();
  }

  /**
   * Render the image using the given Box to specify the render attributes.
   *
   * @param x      x
   * @param y      y
   * @param width  width
   * @param height height
   * @param color  color
   * @param scale  scale
   */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image, final int x, final int y, final int width,
      final int height, @Nonnull final Color color, final float scale) {
    log.fine("renderImage()");
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    if (!currentTexturing) {
      gl.glEnable(GL.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    gl.glPushMatrix();
    gl.glTranslatef(x + width / 2, y + height / 2, 0.0f);
    gl.glScalef(scale, scale, 1.0f);
    gl.glTranslatef(-(x + width / 2), -(y + height / 2), 0.0f);

    JoglRenderImage internalImage = (JoglRenderImage) image;
    internalImage.bind();

    float textureWidth = internalImage.getTextureWidth();
    float textureHeight = internalImage.getTextureHeight();
    float imageWidth = internalImage.getWidth();
    float imageHeight = internalImage.getHeight();

    float u1 = imageWidth / textureWidth;
    float v1 = imageHeight / textureHeight;

    gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex2i(x, y);
    gl.glTexCoord2f(u1, 0.0f);
    gl.glVertex2i(x + width, y);
    gl.glTexCoord2f(u1, v1);
    gl.glVertex2i(x + width, y + height);
    gl.glTexCoord2f(0.0f, v1);
    gl.glVertex2i(x, y + height);
    gl.glEnd();
    gl.glPopMatrix();
  }

  /**
   * Render sub image.
   *
   * @param x     x
   * @param y     y
   * @param w     w
   * @param h     h
   * @param srcX  x
   * @param srcY  y
   * @param srcW  w
   * @param srcH  h
   * @param color color
   */
  @Override
  public void renderImage(
      @Nonnull final RenderImage image, final int x, final int y, final int w,
      final int h, final int srcX, final int srcY, final int srcW, final int srcH,
      @Nonnull final Color color, final float scale, final int centerX, final int centerY) {
    log.fine("renderImage2()");
    final GL2 gl = GLContext.getCurrentGL().getGL2();
    if (!currentTexturing) {
      gl.glEnable(GL.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    gl.glPushMatrix();
    gl.glTranslatef(centerX, centerY, 0.0f);
    gl.glScalef(scale, scale, 1.0f);
    gl.glTranslatef(-(centerX), -(centerY), 0.0f);

    JoglRenderImage internalImage = (JoglRenderImage) image;
    internalImage.bind();

    float textureWidth = internalImage.getTextureWidth();
    float textureHeight = internalImage.getTextureHeight();

    float u0 = srcX / textureWidth;
    float v0 = srcY / textureHeight;
    float u1 = (srcX + srcW) / textureWidth;
    float v1 = (srcY + srcH) / textureHeight;

    gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(u0, v0);
    gl.glVertex2i(x, y);
    gl.glTexCoord2f(u1, v0);
    gl.glVertex2i(x + w, y);
    gl.glTexCoord2f(u1, v1);
    gl.glVertex2i(x + w, y + h);
    gl.glTexCoord2f(u0, v1);
    gl.glVertex2i(x, y + h);
    gl.glEnd();

    gl.glPopMatrix();
  }

  /**
   * render the text.
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
    if (log.isLoggable(Level.FINER)) {
      log.entering(
          JoglRenderDevice.class.getName(),
          "renderFont",
          new Object[] { font, text, x, y, color, fontSizeX, fontSizeY });
    }
    final GL gl = GLContext.getCurrentGL();
    if (!currentTexturing) {
      gl.glEnable(GL.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    setBlendMode(BlendMode.BLEND);
    ((JoglRenderFont) font).getFont().renderWithSizeAndColor(x, y, text, fontSizeX, fontSizeY,
            color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }

  /**
   * Enable clipping to the given region.
   *
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  @Override
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    log.fine("enableClip()");

    if (currentClipping && currentClippingX0 == x0 && currentClippingY0 == y0
        && currentClippingX1 == x1 && currentClippingY1 == y1) {
      return;
    }
    currentClipping = true;
    currentClippingX0 = x0;
    currentClippingY0 = y0;
    currentClippingX1 = x1;
    currentClippingY1 = y1;
    final GL gl = GLContext.getCurrentGL();
    gl.glScissor(x0, getHeight() - y1, x1 - x0, y1 - y0);
    gl.glEnable(GL.GL_SCISSOR_TEST);
  }

  /**
   * Disable Clip.
   */
  @Override
  public void disableClip() {
    log.fine("disableClip()");

    if (!currentClipping) {
      return;
    }
    final GL gl = GLContext.getCurrentGL();
    gl.glDisable(GL.GL_SCISSOR_TEST);
    currentClipping = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = 0;
    currentClippingY1 = 0;
  }

  @Override
  public void setBlendMode(@Nonnull final BlendMode renderMode) {
    log.fine("setBlendMode()");

    if (renderMode.equals(currentBlendMode)) {
      return;
    }
    currentBlendMode = renderMode;
    final GL gl = GLContext.getCurrentGL();
    if (currentBlendMode.equals(BlendMode.BLEND)) {
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    } else if (currentBlendMode.equals(BlendMode.MULIPLY)) {
      gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
    }
  }

  @Override
  public MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException {
    return new JoglMouseCursor(filename, hotspotX, hotspotY, resourceLoader);
  }

  @Override
  public void enableMouseCursor(@Nonnull MouseCursor mouseCursor) {
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader niftyResourceLoader) {
    this.resourceLoader = niftyResourceLoader;
  }
}
