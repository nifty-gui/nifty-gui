package de.lessvoid.nifty.renderer.lwjgl.render.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import net.sourceforge.png.PNGDecoder;
import net.sourceforge.png.PNGDecoder.TextureFormat;

public class ImageLoader {
  private ByteBuffer imageData;
  private int width;
  private int height;
  private int textureWidth;
  private int textureHeight;

  public ImageLoader(final String name, final InputStream in) throws IOException {
    if (name.toLowerCase().endsWith(".png")) {
      processPNG(in);
    }
  }

  public ByteBuffer getImageData() {
    return imageData;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getTextureWidth() {
    return textureWidth;
  }

  public int getTextureHeight() {
    return textureHeight;
  }

  private void processPNG(final InputStream in) throws IOException {
    PNGDecoder png = new PNGDecoder(in);

    width = png.getWidth();
    height = png.getHeight();

    textureWidth = get2Fold(png.getWidth());
    textureHeight = get2Fold(png.getHeight());

    ByteBuffer result = ByteBuffer.allocate(textureWidth * textureHeight * 4);
    png.decode(result, textureWidth*4, TextureFormat.RGBA);
    imageData = result;
  }

  private int get2Fold(final int fold) {
    int ret = 2;
    while (ret < fold) {
        ret *= 2;
    }
    return ret;
  }
}
