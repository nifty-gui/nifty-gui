package de.lessvoid.nifty.renderer.lwjgl.render;

import java.nio.IntBuffer;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * Lwjgl RenderDevice Implementation.
 * @author void
 */
public class LwjglRenderDevice implements RenderDevice {
  private static Logger log = Logger.getLogger(LwjglRenderDevice.class.getName());
  private static IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  private int viewportWidth;
  private int viewportHeight;
  private long time;
  private long frames;
  private long lastFrames;
  private boolean displayFPS = false;
  private boolean logFPS = false;
  private RenderFont fpsFont;

  // we keep track of which GL states we've already set to make sure we don't set
  // the same state twice.
  private boolean currentTexturing = true;
  private BlendMode currentBlendMode = null;
  private boolean currentClipping = false;
  private int currentClippingX0 = 0;
  private int currentClippingY0 = 0;
  private int currentClippingX1 = 0;
  private int currentClippingY1 = 0;

  /**
   * The standard constructor. You'll use this in production code. Using this
   * constructor will configure the RenderDevice to not log FPS on System.out.
   */
  public LwjglRenderDevice() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    viewportWidth = viewportBuffer.get(2);
    viewportHeight = viewportBuffer.get(3);
    log.info("Viewport: " + viewportWidth + ", " + viewportHeight);

    time = System.currentTimeMillis();
    frames = 0;
  }

  /**
   * The development mode constructor allows to display the FPS on screen when
   * the given flag is set to true. Note that setting displayFPS to false will
   * still log the FPS on System.out every couple of frames.
   * @param displayFPS
   */
  public LwjglRenderDevice(final boolean displayFPS) {
    this();
    this.logFPS = true;
    this.displayFPS = displayFPS;
    if (this.displayFPS) {
      fpsFont = createFont("fps.fnt");
    }
  }

  /**
   * Get Width.
   * @return width of display mode
   */
  public int getWidth() {
    return viewportWidth;
  }

  /**
   * Get Height.
   * @return height of display mode
   */
  public int getHeight() {
    return viewportHeight;
  }

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
  }

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
    if (displayFPS) {
      renderFont(fpsFont, "FPS: " + String.valueOf(lastFrames), 10, getHeight() - fpsFont.getHeight() - 10, Color.WHITE, 1.0f);
    }
  }

  public void clear() {
    log.fine("clear()");

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  /**
   * Create a new RenderImage.
   * @param filename filename
   * @param filterLinear linear filter the image
   * @return RenderImage
   */
  public RenderImage createImage(final String filename, final boolean filterLinear) {
    return new LwjglRenderImage(filename, filterLinear);
  }

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  public RenderFont createFont(final String filename) {
    return new LwjglRenderFont(filename, this);
  }

  /**
   * Render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   */
  public void renderQuad(final int x, final int y, final int width, final int height, final Color color) {
    log.fine("renderQuad()");

    if (currentTexturing) {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      currentTexturing = false;
    }

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glVertex2i(x,         y);
      GL11.glVertex2i(x + width, y);
      GL11.glVertex2i(x + width, y + height);
      GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
  }

  public void renderQuad(final int x, final int y, final int width, final int height, final Color topLeft, final Color topRight, final Color bottomRight, final Color bottomLeft) {
    log.fine("renderQuad2()");

    if (currentTexturing) {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      currentTexturing = false;
    }
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glColor4f(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha());
      GL11.glVertex2i(x,         y);
      GL11.glColor4f(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha());
      GL11.glVertex2i(x + width, y);
      GL11.glColor4f(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(), bottomRight.getAlpha());
      GL11.glVertex2i(x + width, y + height);
      GL11.glColor4f(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(), bottomLeft.getAlpha());
      GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
  }

  /**
   * Render the image using the given Box to specify the render attributes.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   * @param scale scale
   */
  public void renderImage(final RenderImage image, final int x, final int y, final int width, final int height, final Color color, final float scale) {
    log.fine("renderImage()");

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    GL11.glPushMatrix();
    GL11.glTranslatef(x + width / 2, y + height / 2, 0.0f);
    GL11.glScalef(scale, scale, 1.0f);
    GL11.glTranslatef(-(x + width / 2), -(y + height / 2), 0.0f);

    LwjglRenderImage internalImage = (LwjglRenderImage) image;
    internalImage.bind();

    float textureWidth = (float)internalImage.getTextureWidth();
    float textureHeight = (float)internalImage.getTextureHeight();
    float imageWidth = (float)internalImage.getWidth();
    float imageHeight = (float)internalImage.getHeight();

    float u1 = imageWidth / textureWidth;
    float v1 = imageHeight / textureHeight;

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex2i(x,         y);
      GL11.glTexCoord2f(  u1, 0.0f); GL11.glVertex2i(x + width, y);
      GL11.glTexCoord2f(  u1,   v1); GL11.glVertex2i(x + width, y + height);
      GL11.glTexCoord2f(0.0f,   v1); GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
    GL11.glPopMatrix();
  }

  /**
   * Render sub image.
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @param srcX x
   * @param srcY y
   * @param srcW w
   * @param srcH h
   * @param color color
   */
  public void renderImage(
      final RenderImage image,
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH,
      final Color color,
      final float scale,
      final int centerX,
      final int centerY) {
    log.fine("renderImage2()");

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    GL11.glPushMatrix();
    GL11.glTranslatef(centerX, centerY, 0.0f);
    GL11.glScalef(scale, scale, 1.0f);
    GL11.glTranslatef(-(centerX), -(centerY), 0.0f);

    LwjglRenderImage internalImage = (LwjglRenderImage) image;
    internalImage.bind();

    float textureWidth = (float)internalImage.getTextureWidth();
    float textureHeight = (float)internalImage.getTextureHeight();

    float u0 = srcX / textureWidth;
    float v0 = srcY / textureHeight;
    float u1 = (srcX + srcW) / textureWidth;
    float v1 = (srcY + srcH) / textureHeight;

    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glTexCoord2f(u0, v0); GL11.glVertex2i(x,     y);
      GL11.glTexCoord2f(u1, v0); GL11.glVertex2i(x + w, y);
      GL11.glTexCoord2f(u1, v1); GL11.glVertex2i(x + w, y + h);
      GL11.glTexCoord2f(u0, v1); GL11.glVertex2i(x,     y + h);
    GL11.glEnd();

    GL11.glPopMatrix();
  }

  /**
   * render the text.
   * @param text text
   * @param x x
   * @param y y
   * @param color color
   * @param fontSize size
   */
  public void renderFont(final RenderFont font, final String text, final int x, final int y, final Color color, final float fontSize) {
    log.fine("renderFont()");

    if (!currentTexturing) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      currentTexturing = true;
    }
    setBlendMode(BlendMode.BLEND);
    if (color == null) {
      ((LwjglRenderFont)font).getFont().drawStringWithSize(x, y, text, fontSize);
    } else {
      ((LwjglRenderFont)font).getFont().renderWithSizeAndColor(x, y, text, fontSize, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
  }

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    log.fine("enableClip()");

    if (currentClipping && currentClippingX0 == x0 && currentClippingY0 == y0 && currentClippingX1 == x1 && currentClippingY1 == y1) {
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

  public void setBlendMode(final BlendMode renderMode) {
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
}
