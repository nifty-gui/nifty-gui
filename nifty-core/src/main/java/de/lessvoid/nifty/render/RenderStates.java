package de.lessvoid.nifty.render;

import javax.annotation.Nonnull;

public class RenderStates {
  private boolean position;
  private boolean color;
  private boolean alpha;
  private boolean textSize;
  private boolean imageScale;
  private boolean font;
  private boolean clip;
  private boolean blendMode;

  public void addClip() {
    clip = true;
  }

  public void addColor() {
    color = true;
  }

  public void addAlpha() {
    alpha = true;
  }

  public void addPosition() {
    position = true;
  }

  public void addFont() {
    font = true;
  }

  public void addImageScale() {
    imageScale = true;
  }

  public void addTextSize() {
    textSize = true;
  }

  public void addBlendMode() {
    blendMode = true;
  }

  public boolean hasPosition() {
    return position;
  }

  public boolean hasColor() {
    return color;
  }

  public boolean hasAlpha() {
    return alpha;
  }

  public boolean hasTextSize() {
    return textSize;
  }

  public boolean hasImageScale() {
    return imageScale;
  }

  public boolean hasFont() {
    return font;
  }

  public boolean hasClip() {
    return clip;
  }

  public boolean hasBlendMode() {
    return blendMode;
  }

  public void clear() {
    position = false;
    color = false;
    alpha = false;
    textSize = false;
    imageScale = false;
    font = false;
    clip = false;
    blendMode = false;
  }

  public void addAll() {
    position = true;
    color = true;
    alpha = true;
    textSize = true;
    imageScale = true;
    font = true;
    clip = true;
    blendMode = true;
  }

  public void removeAll(@Nonnull final RenderStates states) {
    if (states.position) {
      position = false;
    } else if (states.color) {
      color = false;
    } else if (states.alpha) {
      alpha = false;
    } else if (states.textSize) {
      textSize = false;
    } else if (states.imageScale) {
      imageScale = false;
    } else if (states.font) {
      font = false;
    } else if (states.clip) {
      clip = false;
    } else if (states.blendMode) {
      blendMode = false;
    }
  }
}
