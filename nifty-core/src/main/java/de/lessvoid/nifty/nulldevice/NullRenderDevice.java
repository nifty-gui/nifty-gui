package de.lessvoid.nifty.nulldevice;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class NullRenderDevice implements RenderDevice {
  
  public void beginFrame() {
  }
  
  public void endFrame() {
  }

  public void clear() {
  }
  
  public RenderFont createFont(String filename) {
    return null;
  }
  
  public RenderImage createImage(String filename, boolean filterLinear) {
    return null;
  }

  public void disableClip() {
  }
  
  public void enableClip(int x0, int y0, int x1, int y1) {
  }
    
  public int getHeight() {
    return 0;
  }
  
  public int getWidth() {
    return 0;
  }
  
  public void renderFont(RenderFont font, String text, int x, int y, Color fontColor, float size) {
  }
  
  public void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float imageScale) {
  }
  
  public void renderImage(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH, Color color, float scale, int centerX, int centerY) {
  }
  
  public void renderQuad(int x, int y, int width, int height, Color color) {
  }
  
  public void renderQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
  }
  
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
