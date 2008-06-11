package de.lessvoid.nifty.render;

/**
 * sub image type.
 * @author void
 */
public class RenderImageSubImageMode {

  private enum Mode { NORMAL, SCALE, RESIZE };
  private Mode mode;
  
  /**
   * disabled - use normal rendering.
   */
  public static RenderImageSubImageMode NORMAL() {
    return new RenderImageSubImageMode(Mode.NORMAL);
  }

  /**
   * just scale it.
   */
  public static RenderImageSubImageMode SCALE() {
    return new RenderImageSubImageMode(Mode.SCALE);
  }

  /**
   * use the resize hint.
   */
  public static RenderImageSubImageMode RESIZE() {
    return new RenderImageSubImageMode(Mode.RESIZE);
  }
  
  RenderImageSubImageMode(final Mode mode) {
    this.mode = mode;
  }
  
  boolean equals(final RenderImageSubImageMode other) {
    return this.mode == other.mode;
  }

  public static RenderImageSubImageMode valueOf(final String subImageSizeMode) {
    if ("normal".equals(subImageSizeMode)) {
      return RenderImageSubImageMode.NORMAL();  
    } else if ("scale".equals(subImageSizeMode)) {
      return RenderImageSubImageMode.SCALE();
    } else if ("resizeHint".equals(subImageSizeMode)) {
      return RenderImageSubImageMode.RESIZE();
    } else {
      return RenderImageSubImageMode.NORMAL();
    }
  }
}
