package de.lessvoid.nifty.render.spi.lwjgl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.spi.RenderDevice;
import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.render.spi.BlendMode;
import de.lessvoid.nifty.tools.Color;

/**
 * Lwjgl RenderDevice Implementation.
 * @author void
 */
public class RenderDeviceLwjgl implements RenderDevice {
  private static final int INTERNAL_BUFFERSIZE_IN_BYTES = 1024;
  private static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(INTERNAL_BUFFERSIZE_IN_BYTES);
  private static DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
  private RenderTools renderTools;

  public RenderDeviceLwjgl() {
    renderTools = new RenderTools();
  }

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
    return new RenderImageLwjgl(renderTools, filename, filterLinear);
  }

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  public RenderFont createFont(final String filename) {
    return new RenderFontLwjgl(renderTools, filename, this);
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
    renderTools.beginRender();
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glVertex2i(x,         y);
      GL11.glVertex2i(x + width, y);
      GL11.glVertex2i(x + width, y + height);
      GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
    renderTools.endRender();
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

  public void setBlendMode(final BlendMode renderMode) {
    renderTools.changeRenderMode(renderMode);
  }
}
