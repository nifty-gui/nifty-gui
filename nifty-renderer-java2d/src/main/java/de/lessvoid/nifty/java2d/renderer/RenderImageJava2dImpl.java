package de.lessvoid.nifty.java2d.renderer;

import de.lessvoid.nifty.spi.render.RenderImage;

import java.awt.image.BufferedImage;

public class RenderImageJava2dImpl implements RenderImage {

  final BufferedImage image;

  BufferedImage getImage() {
    return image;
  }

  public RenderImageJava2dImpl(BufferedImage image) {
    this.image = image;
  }

  @Override
  public int getHeight() {
    return image.getHeight(null);
  }

  @Override
  public int getWidth() {
    return image.getWidth(null);
  }

  @Override
  public void dispose() {
  }
}