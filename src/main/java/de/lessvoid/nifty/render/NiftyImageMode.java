package de.lessvoid.nifty.render;

import java.util.logging.Logger;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.spi.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderImageMode supports the following modes.
 *
 * - "normal" (render the given image to the given image size)
 * - "subImage:" (render the given subImage of the image to the given size)
 * - "resize:" (special mode that allows scaling of bitmaps with a predefined schema)
 *
 * @author void
 */
public class NiftyImageMode {
  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(NiftyImageMode.class.getName());

  /**
   * normal/default string.
   */
  private static final String NORMAL_STRING = "normal";

  /**
   * resize string.
   */
  private static final String RESIZE_STRING = "resize:";

  /**
   * subImage string.
   */
  private static final String SUB_IMAGE_STRING = "subImage:";

  /**
   * subImage args count.
   */
  private static final int SUBIMAGE_ARGS_COUNT = 4;

  /**
   * supported modes.
   */
  private enum Mode { NORMAL, SUBIMAGE, RESIZE };

  /**
   * actual mode.
   */
  private Mode mode;

  /**
   * sub image box to scale.
   */
  private Box subImageBox;

  /**
   * resize string.
   */
  private String resizeString;

  /**
   * normal rendering.
   * @return RenderImageMode for NORMAL mode
   */
  public static NiftyImageMode normal() {
    return new NiftyImageMode();
  }

  /**
   * scale a sub image.
   * @param x x
   * @param y y
   * @param w w
   * @param h h
   * @return RenderImageMode for SUBIMAGE mode
   */
  public static NiftyImageMode subImage(final int x, final int y, final int w, final int h) {
    return new NiftyImageMode(new Box(x, y, w, h));
  }

  /**
   * use the resize hint.
   * @param resizeString resize information
   * @return RenderImageMode for RESIZE mode
   */
  public static NiftyImageMode resize(final String resizeString) {
    return new NiftyImageMode(resizeString);
  }

  /**
   * standard constructor.
   */
  NiftyImageMode() {
    this.mode = Mode.NORMAL;
  }

  /**
   * subImage constructor.
   * @param box subImageBox
   */
  NiftyImageMode(final Box box) {
    this.mode = Mode.SUBIMAGE;
    this.subImageBox = box;
  }

  /**
   * resize constructor.
   * @param newResizeString string with resize information
   */
  NiftyImageMode(final String newResizeString) {
    this.mode = Mode.RESIZE;
    this.resizeString = newResizeString;
  }

  /**
   * create a RenderImageMode from the given String.
   * @param imageMode imageMode String
   * @return a RenderImageMode
   */
  public static NiftyImageMode valueOf(final String imageMode) {
    if (imageMode == null || NORMAL_STRING.equals(imageMode)) {
      return NiftyImageMode.normal();
    } else if (imageMode.startsWith(SUB_IMAGE_STRING)) {
      return handleSubImage(imageMode);
    } else if (imageMode.startsWith(RESIZE_STRING)) {
      return handleResize(imageMode);
    } else {
      return NiftyImageMode.normal();
    }
  }

  /**
   * handle subImage.
   * @param imageMode image mode
   * @return RenderImageMode
   */
  private static NiftyImageMode handleSubImage(final String imageMode) {
    String parameters = imageMode.replaceFirst(SUB_IMAGE_STRING, "");
    if (parameters == null || parameters.length() == 0) {
      log.warning(
          "trying to parse imageMode [" + imageMode
          + "] but missing sub image definition! using default RenderImageMode normal.");
      return NiftyImageMode.normal();
    }
    String[] args = parameters.split(",");
    if (args.length != SUBIMAGE_ARGS_COUNT) {
      log.warning(
          "expecting exactly 4 parameters but got only " + args.length
          + " using default RenderImageMode normal.");
      return NiftyImageMode.normal();
    }
    int index = 0;
    int x = Integer.valueOf(args[index++]);
    int y = Integer.valueOf(args[index++]);
    int w = Integer.valueOf(args[index++]);
    int h = Integer.valueOf(args[index++]);
    return NiftyImageMode.subImage(x, y, w, h);
  }

  /**
   * handle resize.
   * @param imageMode image mode
   * @return RenderImageMode
   */
  private static NiftyImageMode handleResize(final String imageMode) {
    String parameters = imageMode.replaceFirst(RESIZE_STRING, "");
    if (parameters == null || parameters.length() == 0) {
      log.warning(
          "trying to parse imageMode [" + imageMode
          + "] but missing resize definition! using default RenderImageMode normal.");
      return NiftyImageMode.normal();
    }
    return NiftyImageMode.resize(parameters);
  }

  /**
   * Render image.
   * @param renderImage RenderImage
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   * @param color color
   * @param scale scale
   */
  public void render(
      final RenderImage renderImage,
      final int x,
      final int y,
      final int width,
      final int height,
      final Color color,
      final float scale) {
    if (mode == Mode.NORMAL) {
      renderImage.render(x, y, width, height, color, scale);
    } else if (mode == Mode.SUBIMAGE) {
      renderImage.render(
          x, y, width, height,
          subImageBox.getX(), subImageBox.getY(), subImageBox.getWidth(), subImageBox.getHeight(),
          color);
    } else if (mode == Mode.RESIZE) {
      ResizeHelper resizeHelper = new ResizeHelper(renderImage, this.resizeString);
      resizeHelper.performRender(x, y, width, height, color);
    }
  }
}
