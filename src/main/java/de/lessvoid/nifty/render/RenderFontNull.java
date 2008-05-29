package de.lessvoid.nifty.render;

/**
 * RenderFontNull.
 * @author void
 */
public class RenderFontNull implements RenderFont {

  public int getFittingOffset(String text, int width) {
    return 0;
  }

  public int getFittingOffsetBackward(String text, int width) {
    return 0;
  }

  public int getHeight() {
    return 0;
  }

  public int getIndexFromPixel(String text, int pixel, float size) {
    return 0;
  }

  public int getWidth(String text) {
    return 0;
  }

  public void setSelection(int selectionStart, int selectionEnd) {
  }

  public void render(String text, int x, int y) {
  }

  public void setColor(float r, float g, float b, float a) {
  }

  public void setSize(float size) { 
  }

  public void setDefaultColor() { 
  }
}
