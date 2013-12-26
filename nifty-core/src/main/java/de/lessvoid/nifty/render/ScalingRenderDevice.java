package de.lessvoid.nifty.render;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ScalingRenderDevice implements RenderDevice {
  private final NiftyRenderEngine renderEngine;
  private final RenderDevice internal;

  public ScalingRenderDevice(final NiftyRenderEngine renderEngine, final RenderDevice interal) {
    this.renderEngine = renderEngine;
    this.internal = interal;
  }

  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public RenderImage createImage(@Nonnull String filename, boolean filterLinear) {
    return internal.createImage(filename, filterLinear);
  }

  @Override
  public RenderFont createFont(@Nonnull String filename) {
    return internal.createFont(filename);
  }

  @Override
  public int getWidth() {
    return internal.getWidth();
  }

  @Override
  public int getHeight() {
    return internal.getHeight();
  }

  @Override
  public void beginFrame() {
    internal.beginFrame();
  }

  @Override
  public void endFrame() {
    internal.endFrame();
  }

  @Override
  public void clear() {
    internal.clear();
  }

  @Override
  public void setBlendMode(@Nonnull BlendMode renderMode) {
    internal.setBlendMode(renderMode);
  }

  @Override
  public void renderQuad(int x, int y, int width, int height, @Nonnull Color color) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color);
  }

  @Override
  public void renderQuad(
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color topLeft,
      @Nonnull Color topRight,
      @Nonnull Color bottomRight,
      @Nonnull Color bottomLeft) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), topLeft, topRight,
        bottomRight, bottomLeft);
  }

  @Override
  public void renderImage(
      @Nonnull RenderImage image,
      int x,
      int y,
      int width,
      int height,
      @Nonnull Color color,
      float imageScale) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color, imageScale);
  }

  @Override
  public void renderImage(
      @Nonnull RenderImage image,
      int x,
      int y,
      int w,
      int h,
      int srcX,
      int srcY,
      int srcW,
      int srcH,
      @Nonnull Color color,
      float scale,
      int centerX,
      int centerY) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y),
        renderEngine.convertToNativeWidth(w), renderEngine.convertToNativeHeight(h), srcX, srcY, srcW, srcH, color,
        scale, renderEngine.convertToNativeX(centerX), renderEngine.convertToNativeY(centerY));
  }

  @Override
  public void renderFont(
      @Nonnull RenderFont font,
      @Nonnull String text,
      int x,
      int y,
      @Nonnull Color fontColor,
      float sizeX,
      float sizeY) {
    internal.renderFont(font, text, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), fontColor,
        renderEngine.convertToNativeTextSizeX(sizeX), renderEngine.convertToNativeTextSizeY(sizeY));
  }

  @Override
  public void enableClip(int x0, int y0, int x1, int y1) {
    internal.enableClip(renderEngine.convertToNativeX(x0), renderEngine.convertToNativeY(y0),
        renderEngine.convertToNativeX(x1), renderEngine.convertToNativeY(y1));
  }

  @Override
  public void disableClip() {
    internal.disableClip();
  }

  @Override
  public MouseCursor createMouseCursor(@Nonnull String filename, int hotspotX, int hotspotY) throws IOException {
    return internal.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(@Nonnull MouseCursor mouseCursor) {
    internal.enableMouseCursor(mouseCursor);
  }

  @Override
  public void disableMouseCursor() {
    internal.disableMouseCursor();
  }
}
