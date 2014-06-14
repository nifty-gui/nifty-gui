package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.spi.NiftyTexture;


public class InternalNiftyImage {
  private NiftyTexture texture;

  // Factory methods

  public static InternalNiftyImage newImage(final NiftyTexture texture) {
    return new InternalNiftyImage(texture);
  }

  // Nifty API "interface" implementation

  public int getWidth() {
    return texture.getWidth();
  }

  public int getHeight() {
    return texture.getHeight();
  }

  // Internal "API"

  public NiftyTexture getTexture() {
    return texture;
  }

  private InternalNiftyImage(final NiftyTexture texture) {
    this.texture = texture;
  }
}
