package de.lessvoid.nifty.renderer.lwjgl3.render;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Lwjgl RenderDevice Implementation.
 *
 * @author void
 *
 * @deprecated Use {@link de.lessvoid.nifty.render.batch.BatchRenderDevice} with either
 * {@link Lwjgl3BatchRenderBackendFactory#create()} or {@link Lwjgl3BatchRenderBackendCoreProfileFactory#create()}.
 */
@Deprecated
public class Lwjgl3RenderDevice implements RenderDevice {
  private static final Logger log = Logger.getLogger(Lwjgl3RenderDevice.class.getName());
  private static final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  private final TimeProvider timeProvider = new AccurateTimeProvider();
  private final long glfwWindow;
  private NiftyResourceLoader resourceLoader;
  private int viewportWidth = -1;
  private int viewportHeight = -1;
  private long time;
  private long frames;
  private boolean displayFPS = false;
  private boolean logFPS = false;
  private RenderFont fpsFont;

  // we keep track of which GL states we've already set to make sure we don't set
  // the same state twice.
  private boolean currentTexturing = true;
  @Nullable
  private BlendMode currentBlendMode = null;
  private boolean currentClipping = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;

  @Nonnull
  private StringBuilder buffer = new StringBuilder();
  private int quadCount;
  private int glyphCount;
  @Nullable
  private MouseCursor mouseCursor;

  /**
   * The standard constructor. You'll use this in production code. Using this
   * constructor will configure the RenderDevice to not log FPS on System.out.
   */
  public Lwjgl3RenderDevice(final long glfwWindow) {
    this.glfwWindow = glfwWindow;
    time = timeProvider.getMsTime();
    frames = 0;
  }

  /**
   * The development mode constructor allows to display the FPS on screen when
   * the given flag is set to true. Note that setting displayFPS to false will
   * still log the FPS on System.out every couple of frames.
   */
  public Lwjgl3RenderDevice(final long glfwWindow, final boolean displayFPS) {
    this(glfwWindow);
    this.logFPS = true;
    this.displayFPS = displayFPS;
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;

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
    if (viewportWidth == -1) {
      getViewport();
    }
    return viewportWidth;
  }

  /**
   * Get Height.
   *
   * @return height of display mode
   */
  @Override
  public int getHeight() {
    if (viewportHeight == -1) {
      getViewport();
    }
    return viewportHeight;
  }

  private void getViewport() {
    GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Viewport: " + viewportWidth + ", " + viewportHeight);
    }
  }

  @Override
  public void beginFrame() {
    log.fine("beginFrame()");

    // set inital states for each frame
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    setBlendMode(BlendMode.BLEND);

    GL11.glEnable(GL11.GL_TEXTURE_2D);
    currentTexturing = true;

    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    currentClipping = false;
    currentClippingX0 = 0;
    currentClippingY0 = 0;
    currentClippingX1 = 0;
    currentClippingY1 = 0;
    quadCount = 0;
    glyphCount = 0;
  }

  @Override
  public void endFrame() {
    log.fine("endFrame");
    frames++;
    long diff = timeProvider.getMsTime() - time;
    if (diff >= 1000) {
      time += diff;
      long lastFrames = frames;

      buffer.setLength(0);
      buffer.append("FPS: ");
      buffer.append(lastFrames);
      buffer.append(" (");
      buffer.append(String.format("%f", 1000.f / lastFrames));
      buffer.append(" ms)");
      buffer.append(", Total Tri: ");
      buffer.append(quadCount * 2);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 2);
      buffer.append(")");
      buffer.append(", Total Vert: ");
      buffer.append(quadCount * 4);
      buffer.append(" (Text: ");
      buffer.append(glyphCount * 4);
      buffer.append(")");

      if (logFPS) {
        System.out.println(buffer.toString());
      }
      frames = 0;
    }
    if (displayFPS) {
      renderFont(fpsFont, buffer.toString(), 10, getHeight() - fpsFont.getHeight() - 10, Color.WHITE, 1.0f, 1.0f);
    }

    // currently the RenderDevice interface does not support a way to be notified when the resolution is changed
    // so we reset the viewportWidth and viewportHeight here so that we only call getViewport() once per frame and
    // not each time someone calls getWidth() or getHeight().
    viewportWidth = -1;
    viewportHeight = -1;

    checkGLError();
  }

  @Override
  public void clear() {
    log.fine("clear()");

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  /**
   * Create a new RenderImage.
   *
   * @param filename     filename
   * @param filterLinear linear filter the image
   * @return RenderImage
   */
  @Override
  @Nonnull
  public RenderImage createImage(@Nonnull final String filename, final boolean filterLinear) {
    return new Lwjgl3RenderImage(filename, filterLinear, resourceLoader);
  }

  /**
   * Create a new RenderFont.
   *
   * @param filename filename
   * @return RenderFont
   */
  @Override
  @Nonnull
  public RenderFont createFont(@Nonnull final String filename) {
    return new Lwjgl3RenderFont(filename, resourceLoader);
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
  public void renderQuad(final int x, final int y, final int width, final int height, @Nonnull final Color color) {
    log.fine("renderQuad()");

    if (currentTexturing) {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      currentTexturing = false;
    }

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glVertex2i(x, y);
    GL11.glVertex2i(x + width, y);
    GL11.glVertex2i(x + width, y + height);
    GL11.glVertex2i(x, y + height);
    GL11.glEnd();
    quadCount++;
  }

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
    log.fine("renderQuad2()");

    if (currentTexturing) {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      currentTexturing = false;
    }
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glColor4f(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha());
    GL11.glVertex2i(x, y);
    GL11.glColor4f(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha());
    GL11.glVertex2i(x + width, y);
    GL11.glColor4f(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(), bottomRight.getAlpha());
    GL11.glVertex2i(x + width, y + height);
    GL11.glColor4f(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(), bottomLeft.getAlpha());
    GL11.glVertex2i(x, y + height);
    GL11.glEnd();
    quadCount++;
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
      @Nonnull final RenderImage image,
      final int x,
      final int y,
      final int width,
      final int height,
      @Nonnull final Color color,
      final float scale) {
    log.fine("renderImage()");

    if (width < 0) {
      log.warning("Attempt to render image with negative width");
      return;
    }
    if (height < 0) {
      log.warning("Attempt to render image with negative height");
      return;
    }

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    GL11.glPushMatrix();
    GL11.glTranslatef(x + width / 2, y + height / 2, 0.0f);
    GL11.glScalef(scale, scale, 1.0f);
    GL11.glTranslatef(-(x + width / 2), -(y + height / 2), 0.0f);

    Lwjgl3RenderImage internalImage = (Lwjgl3RenderImage) image;
    internalImage.bind();

    float textureWidth = (float) internalImage.getTextureWidth();
    float textureHeight = (float) internalImage.getTextureHeight();
    float imageWidth = (float) internalImage.getWidth();
    float imageHeight = (float) internalImage.getHeight();

    float u1 = imageWidth / textureWidth;
    float v1 = imageHeight / textureHeight;

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glTexCoord2f(0.0f, 0.0f);
    GL11.glVertex2i(x, y);
    GL11.glTexCoord2f(u1, 0.0f);
    GL11.glVertex2i(x + width, y);
    GL11.glTexCoord2f(u1, v1);
    GL11.glVertex2i(x + width, y + height);
    GL11.glTexCoord2f(0.0f, v1);
    GL11.glVertex2i(x, y + height);
    GL11.glEnd();
    GL11.glPopMatrix();
    quadCount++;
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
    log.fine("renderImage2()");

    if (w < 0) {
      log.warning("Attempt to render image with negative width");
      return;
    }
    if (h < 0) {
      log.warning("Attempt to render image with negative height");
      return;
    }

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    GL11.glPushMatrix();
    GL11.glTranslatef(centerX, centerY, 0.0f);
    GL11.glScalef(scale, scale, 1.0f);
    GL11.glTranslatef(-(centerX), -(centerY), 0.0f);

    Lwjgl3RenderImage internalImage = (Lwjgl3RenderImage) image;
    internalImage.bind();

    float textureWidth = (float) internalImage.getTextureWidth();
    float textureHeight = (float) internalImage.getTextureHeight();

    float u0 = srcX / textureWidth;
    float v0 = srcY / textureHeight;
    float u1 = (srcX + srcW) / textureWidth;
    float v1 = (srcY + srcH) / textureHeight;

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glTexCoord2f(u0, v0);
    GL11.glVertex2i(x, y);
    GL11.glTexCoord2f(u1, v0);
    GL11.glVertex2i(x + w, y);
    GL11.glTexCoord2f(u1, v1);
    GL11.glVertex2i(x + w, y + h);
    GL11.glTexCoord2f(u0, v1);
    GL11.glVertex2i(x, y + h);
    GL11.glEnd();

    GL11.glPopMatrix();
    quadCount++;
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
    log.fine("renderFont()");

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    setBlendMode(BlendMode.BLEND);
    int count = ((Lwjgl3RenderFont) font).getFont().renderWithSizeAndColor(x, y, text, fontSizeX, fontSizeY,
        color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

    glyphCount += count;
    quadCount += count;
  }

  private void checkGLError() {
    int error = GL11.glGetError();
    if (error != GL11.GL_NO_ERROR) {
      String glerrmsg = GLUtil.getErrorString(error);
      log.warning("Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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

    if (currentClipping && currentClippingX0 == x0 && currentClippingY0 == y0 && currentClippingX1 == x1 &&
        currentClippingY1 == y1) {
      return;
    }
    currentClipping = true;
    currentClippingX0 = x0;
    currentClippingY0 = y0;
    currentClippingX1 = x1;
    currentClippingY1 = y1;

    GL11.glScissor(x0, getHeight() - y1, x1 - x0, y1 - y0);
    GL11.glEnable(GL11.GL_SCISSOR_TEST);
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
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
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
    if (currentBlendMode.equals(BlendMode.BLEND)) {
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    } else if (currentBlendMode.equals(BlendMode.MULIPLY)) {
      GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
    }
  }

  @Override
  @Nonnull
  public MouseCursor createMouseCursor(
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    return new Lwjgl3MouseCursor(glfwWindow, filename, hotspotX, hotspotY, resourceLoader);
  }

  @Override
  public void enableMouseCursor(@Nonnull final MouseCursor mouseCursor) {
    this.mouseCursor = mouseCursor;
    mouseCursor.enable();
  }

  @Override
  public void disableMouseCursor() {
    if (mouseCursor != null) {
      mouseCursor.disable();
    }
  }
}
