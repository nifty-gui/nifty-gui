package de.lessvoid.nifty.renderer.lwjgl.render.font;

import de.lessvoid.nifty.tools.Color;

public class ColorValueParser {
  private static final Result noColor = new Result();

  public static class Result {
    private boolean isColor;
    private int nextIndex;
    private Color color;

    public Result() {
      nextIndex = -1;
      color = null;
      isColor = false;
    }

    public Result(final String value, final int endIdx) {
      nextIndex = -1;
      color = null;
      isColor = Color.check(value);
      if (isColor) {
        color = new Color(value);
        nextIndex = endIdx;
      }
    }

    public boolean isColor() {
      return isColor;
    }

    public int getNextIndex() {
      return nextIndex;
    }

    public Color getColor() {
      return color;
    }
  }

  public Result isColor(final String text, final int startIdx) {
    if (text.startsWith("\\#", startIdx)) {
      int endIdx = text.indexOf('#', startIdx + 2);
      if (endIdx != -1) {
        return new Result(text.substring(startIdx + 1, endIdx), endIdx + 1);
      }
    }
    return noColor;
  }

}
