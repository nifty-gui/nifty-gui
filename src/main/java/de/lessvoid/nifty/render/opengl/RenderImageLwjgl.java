package de.lessvoid.nifty.render.opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.RenderImage;

/**
 * Lwjgl/Slick implementation for the RenderImage interface.
 * @author void
 *
 */
public class RenderImageLwjgl implements RenderImage {

  /**
   * The slick image.
   */
  private org.newdawn.slick.Image image;

  /**
   * sub image type to use.
   */
  private SubImageMode subImageMode;

  /**
   * resize helper for the ResizeHint scale type.
   */
  private ResizeHelper resizeHelper;

  /**
   * sub image source x position.
   */
  private int subImageX;

  /**
   * sub image source y position.
   */
  private int subImageY;

  /**
   * sub image source width.
   */
  private int subImageW;

  /**
   * sub image source height.
   */
  private int subImageH;

  /**
   * Create a new RenderImage.
   * @param name the name of the resource in the file system
   * @param filter use linear filter (true) or nearest filter (false)
   */
  public RenderImageLwjgl(final String name, final boolean filter) {
    this.subImageMode = SubImageMode.Disabled;

    try {
      this.image = new org.newdawn.slick.Image(name, false, filter ? Image.FILTER_LINEAR : Image.FILTER_NEAREST);
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get height of image.
   * @return height
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Get width of image.
   * @return width
   */
  public int getWidth() {
    return image.getWidth();
  }

  /**
   * Render the image.
   * @param x x
   * @param y y
   * @param width w
   * @param height h
   */
  public void render(final int x, final int y, final int width, final int height) {
    switch (subImageMode) {
      case Scale:
        render(x, y, width, height, subImageX, subImageY, subImageW, subImageH);
        break;

      case ResizeHint:
        resizeHelper.performRender(x, y, width, height);
        break;

      case Disabled:
      default:
        internalRender(x, y, width, height);
      break;
    }
  }

  /**
   * Actual perform the render.
   * @param x x
   * @param y y
   * @param width w
   * @param height h
   */
  private void internalRender(final int x, final int y, final int width, final int height) {
    beginRender();
    Color color = convertColor();

    image.draw(
        x, y,
        width, height,
        color);

    endRender();
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
   */
  public void render(
      final int x,
      final int y,
      final int w,
      final int h,
      final int srcX,
      final int srcY,
      final int srcW,
      final int srcH) {
    beginRender();
    Color color = convertColor();

    image.draw(
        x, y,
        x + w, y + h,
        srcX,
        srcY,
        srcX + srcW,
        srcY + srcH,
        color);

    endRender();
  }

  /**
   * helper to prepare render state.
   */
  private void beginRender() {
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    image.bind();
  }

  /**
   * helper to restore state.
   */
  private void endRender() {
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
  }

  /**
   * Helper to convert color to slick color.
   * @return Slick color
   */
  private Color convertColor() {
    FloatBuffer color = BufferUtils.createFloatBuffer(16);
    GL11.glGetFloat(GL11.GL_CURRENT_COLOR, color);
    return new Color(color.get(0), color.get(1), color.get(2), color.get(3));
  }

  /**
   * Set a new sub image active state.
   * @param newSubImageMode new type
   */
  public void setSubImageMode(final SubImageMode newSubImageMode) {
    this.subImageMode = newSubImageMode;
  }

  /**
   * Set the resize hint.
   * @param resizeHint string representing the resize hint
   */
  public void setResizeHint(final String resizeHint) {
    resizeHelper = new ResizeHelper(resizeHint);
  }

  /**
   * Set a new SubImage size.
   * @param newSubImageX sub image x
   * @param newSubImageY sub image y
   * @param newSubImageW sub image width
   * @param newSubImageH sub image height
   */
  public void setSubImage(
      final int newSubImageX,
      final int newSubImageY,
      final int newSubImageW,
      final int newSubImageH) {
    this.subImageX = newSubImageX;
    this.subImageY = newSubImageY;
    this.subImageW = newSubImageW;
    this.subImageH = newSubImageH;
  }

  /**
   * The resize helper adds support for a sub image format that is
   * resizable using different parts of the image.
   * @author void
   */
  private class ResizeHelper {

    /**
     * All boxes as calulated from the String.
     */
    private Box[] box = new Box[12];

    /**
     * Create from the given String.
     * @param resizeString the String in the format: b1,b2,b3,h1,b4,b5,b6,h2,b7,b8,b9,h3
     */
    public ResizeHelper(final String resizeString) {
      parseFromString(resizeString, box);
    }

    /**
     * Parse the given String and extract the images boxes.
     * @param resizeString the string
     * @param boxArray the box array to fill
     */
    private void parseFromString(final String resizeString, final Box[] boxArray) {
      String[] data = resizeString.split(",");
      int y = 0;
      y = processRow(y, boxArray, 0, data, 0);
      y = processRow(y, boxArray, 3, data, 4);
      y = processRow(y, boxArray, 6, data, 8);
    }

    /**
     * @param y TODO
     * @param boxArray the box array
     * @param offsetBox offset into box array
     * @param data the data to parse from
     * @param offsetData offset into data array
     * @return TODO
     */
    private int processRow(
        final int y,
        final Box[] boxArray,
        final int offsetBox,
        final String[] data, final int offsetData) {
      int leftWidth = Integer.parseInt(data[offsetData + 0]);
      int middleWidth = Integer.parseInt(data[offsetData + 1]);
      int rightWidth = Integer.parseInt(data[offsetData + 2]);
      int height = Integer.parseInt(data[offsetData + 3]);

      boxArray[offsetBox + 0] = new Box(0, y, leftWidth, height);
      boxArray[offsetBox + 1] = new Box(leftWidth, y, middleWidth, height);
      boxArray[offsetBox + 2] = new Box(leftWidth + middleWidth, y, rightWidth, height);

      return y + height;
    }

    /**
     * Render the given image at x and y position with the given width and height
     * using the resize hint boxes.
     * @param renderDevice the RenderDevice to render on
     * @param theImage the image to render
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     */
    public void performRender(
        final int x,
        final int y,
        final int width,
        final int height) {
      int middlePartHeight = height - box[0].getHeight() - box[3].getHeight() - box[6].getHeight();

      renderRow(x, y,                      width, 0,                box[0], box[1], box[2]);
      renderRow(x, y + box[0].getHeight(), width, middlePartHeight, box[3], box[4], box[5]);
      renderRow(x, y + middlePartHeight + box[3].getHeight() + box[0].getHeight(),   width, 0,                box[6], box[7], box[8]);
    }

    /**
     * @param renderDevice RenderDevice
     * @param theImage image to render
     * @param x x
     * @param y y
     * @param width width
     * @param addHeight additional height
     * @param left left box
     * @param middle middle box
     * @param right right box
     */
    private void renderRow(
        final int x,
        final int y,
        final int width,
        final int addHeight,
        final Box left,
        final Box middle,
        final Box right) {
      render(
          x,
          y,
          left.getWidth(),
          left.getHeight() + addHeight,
          left.getX(),
          left.getY(),
          left.getWidth(),
          left.getHeight());

      render(
          x + left.getWidth(),
          y,
          width - left.getWidth() - right.getWidth(),
          middle.getHeight() + addHeight,
          middle.getX(),
          middle.getY(),
          middle.getWidth(),
          middle.getHeight());

      render(
          x + width - right.getWidth(),
          y,
          right.getWidth(),
          right.getHeight() + addHeight,
          right.getX(),
          right.getY(),
          right.getWidth(),
          right.getHeight());
    }
  }

}
