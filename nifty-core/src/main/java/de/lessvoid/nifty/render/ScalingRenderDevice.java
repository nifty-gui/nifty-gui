package de.lessvoid.nifty.render;

import java.io.IOException;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class ScalingRenderDevice implements RenderDevice {
  private NiftyRenderEngine renderEngine;
  private RenderDevice internal;

  public ScalingRenderDevice(final NiftyRenderEngine renderEngine, final RenderDevice interal) {
    this.renderEngine = renderEngine;
    this.internal = interal;
  }

  @Override
  public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public RenderImage createImage(String filename, boolean filterLinear) {
    return internal.createImage(filename, filterLinear);
  }

  @Override
  public RenderFont createFont(String filename) {
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
  public void setBlendMode(BlendMode renderMode) {
    internal.setBlendMode(renderMode);
  }

  @Override
  public void renderQuad(int x, int y, int width, int height, Color color) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color);
  }

  @Override
  public void renderQuad(
      int x,
      int y,
      int width,
      int height,
      Color topLeft,
      Color topRight,
      Color bottomRight,
      Color bottomLeft) {
    internal.renderQuad(renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), topLeft, topRight, bottomRight, bottomLeft);
  }

  @Override
  public void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float imageScale) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), renderEngine.convertToNativeWidth(width), renderEngine.convertToNativeHeight(height), color, imageScale);
  }

  @Override
  public void renderImage(
      RenderImage image,
      int x,
      int y,
      int w,
      int h,
      int srcX,
      int srcY,
      int srcW,
      int srcH,
      Color color,
      float scale,
      int centerX,
      int centerY) {
    internal.renderImage(image, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), renderEngine.convertToNativeWidth(w), renderEngine.convertToNativeHeight(h), srcX, srcY, srcW, srcH, color, scale, renderEngine.convertToNativeX(centerX), renderEngine.convertToNativeY(centerY));
  }

  @Override
  public void renderFont(RenderFont font, String text, int x, int y, Color fontColor, float sizeX, float sizeY) {
    internal.renderFont(font, text, renderEngine.convertToNativeX(x), renderEngine.convertToNativeY(y), fontColor, renderEngine.convertToNativeTextSizeX(sizeX), renderEngine.convertToNativeTextSizeY(sizeY));
  }

  @Override
  public void enableClip(int x0, int y0, int x1, int y1) {
    internal.enableClip(renderEngine.convertToNativeX(x0), renderEngine.convertToNativeY(y0), renderEngine.convertToNativeX(x1), renderEngine.convertToNativeY(y1));
  }

  @Override
  public void disableClip() {
    internal.disableClip();
  }

  @Override
  public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException {
    return internal.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void enableMouseCursor(MouseCursor mouseCursor) {
    internal.enableMouseCursor(mouseCursor);
  }

  @Override
  public void disableMouseCursor() {
    internal.disableMouseCursor();
  }
}
