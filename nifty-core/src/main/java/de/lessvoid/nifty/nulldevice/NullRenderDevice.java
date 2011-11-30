package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class NullRenderDevice implements RenderDevice {

  @Override
  public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public void beginFrame() {
  }
  
  @Override
  public void endFrame() {
  }

  @Override
  public void clear() {
  }
  
  @Override
  public RenderFont createFont(String filename) {
    return null;
  }
  
  @Override
  public RenderImage createImage(String filename, boolean filterLinear) {
    return null;
  }

  @Override
  public void disableClip() {
  }
  
  @Override
  public void enableClip(int x0, int y0, int x1, int y1) {
  }
    
  @Override
  public int getHeight() {
    return 0;
  }
  
  @Override
  public int getWidth() {
    return 0;
  }
  
  @Override
  public void renderFont(RenderFont font, String text, int x, int y, Color fontColor, float sizeX, float sizeY) {
  }
  
  @Override
  public void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float imageScale) {
  }
  
  @Override
  public void renderImage(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH, Color color, float scale, int centerX, int centerY) {
  }
  
  @Override
  public void renderQuad(int x, int y, int width, int height, Color color) {
  }
  
  @Override
  public void renderQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
  }
  
  @Override
  public void setBlendMode(BlendMode renderMode) {
  }

  @Override
  public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) {
    return null;
  }  

  @Override
  public void enableMouseCursor(MouseCursor mouseCursor) {
  }

  @Override
  public void disableMouseCursor() {
  }
}
