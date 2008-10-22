package de.lessvoid.nifty.render.spi.lwjgl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.spi.RenderDevice;
import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * Lwjgl RenderDevice Implementation.
 * @author void
 */
public class RenderDeviceLwjgl implements RenderDevice {

  /**
   * Buffersize.
   */
  private static final int INTERNAL_BUFFERSIZE_IN_BYTES = 1024;

  /**
   * ByteBuffer.
   */
  private static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(INTERNAL_BUFFERSIZE_IN_BYTES);

  /**
   * DoubleBuffer.
   */
  private static DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();

  /**
   * Get Width.
   * @return width of display mode
   */
  public int getWidth() {
    return Display.getDisplayMode().getWidth();
  }

  /**
   * Get Height.
   * @return height of display mode
   */
  public int getHeight() {
    return Display.getDisplayMode().getHeight();
  }

  /**
   * Clear Screen.
   */
  public void clear() {
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
    return new RenderImageLwjgl(filename, filterLinear);
  }

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  public RenderFont createFont(final String filename) {
    return new RenderFontLwjgl(filename, this);
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
    GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_ENABLE_BIT);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glVertex2i(x,         y);
      GL11.glVertex2i(x + width, y);
      GL11.glVertex2i(x + width, y + height);
      GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
    GL11.glPopAttrib();
  }

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    GL11.glScissor(x0, getHeight() - y1, x1 - x0, y1 - y0);
    GL11.glEnable(GL11.GL_SCISSOR_TEST);
  }

  /**
   * Disable Clip.
   */
  public void disableClip() {
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
  }

}
