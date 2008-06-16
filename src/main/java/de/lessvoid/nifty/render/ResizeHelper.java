package de.lessvoid.nifty.render;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * The resize helper adds support for a sub image format that is
 * resizable using different parts of the image.
 * @author void
 */
class ResizeHelper {

  /**
   * Size of box.
   */
  private static final int BOX_SIZE = 12;

  /**
   * All boxes as calculated from the String.
   */
  private Box[] box = new Box[BOX_SIZE];

  /**
   * RenderImage.
   */
  private RenderImage renderImage;

  /**
   * Create from the given String.
   * @param renderImageParam RenderImage
   * @param resizeString the String in the format: b1,b2,b3,h1,b4,b5,b6,h2,b7,b8,b9,h3
   */
  public ResizeHelper(final RenderImage renderImageParam, final String resizeString) {
    renderImage = renderImageParam;
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
   * @param x x position
   * @param y y position
   * @param width width
   * @param height height
   * @param color color
   */
  public void performRender(
      final int x,
      final int y,
      final int width,
      final int height,
      final Color color) {
    int middlePartHeight = height - box[0].getHeight() - box[3].getHeight() - box[6].getHeight();

    renderRow(x, y,                      width, 0,                box[0], box[1], box[2], color);
    renderRow(x, y + box[0].getHeight(), width, middlePartHeight, box[3], box[4], box[5], color);
    renderRow(x, y + middlePartHeight + box[3].getHeight() + box[0].getHeight(),   width, 0, box[6], box[7], box[8], color);
  }

  /**
   * @param x x
   * @param y y
   * @param width width
   * @param addHeight additional height
   * @param left left box
   * @param middle middle box
   * @param right right box
   * @param color color
   */
  private void renderRow(
      final int x,
      final int y,
      final int width,
      final int addHeight,
      final Box left,
      final Box middle,
      final Box right,
      final Color color) {
    renderImage.render(
        x, y, left.getWidth(), left.getHeight() + addHeight,
        left.getX(), left.getY(), left.getWidth(), left.getHeight(),
        color);

    renderImage.render(
        x + left.getWidth(), y, width - left.getWidth() - right.getWidth(), middle.getHeight() + addHeight,
        middle.getX(), middle.getY(), middle.getWidth(), middle.getHeight(),
        color);

    renderImage.render(
        x + width - right.getWidth(), y, right.getWidth(), right.getHeight() + addHeight,
        right.getX(), right.getY(), right.getWidth(), right.getHeight(),
        color);
  }
}
