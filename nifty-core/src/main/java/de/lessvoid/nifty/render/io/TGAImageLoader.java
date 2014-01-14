package de.lessvoid.nifty.render.io;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.imageio.ImageIO;

/**
 * A utility to load TGAs. Note: NOT THREAD SAFE Fresh cut of code but largely influenced by the TGA
 * loading class provided as part of the Java Monkey Engine (JME). Why not check out what they're
 * doing over at http://www.jmonkeyengine.com. Kudos to Mark Powell.
 *
 * @author Kevin Glass, Julien Gouesse (port to JOGL 2)
 */
public class TGAImageLoader implements ImageLoader {
  /**
   * The width of the texture that needs to be generated
   */
  private int texWidth;

  /**
   * The height of the texture that needs to be generated
   */
  private int texHeight;

  /**
   * The width of the TGA image
   */
  private int width;

  /**
   * The height of the TGA image
   */
  private int height;

  /**
   * The bit depth of the image
   */
  private short pixelDepth;

  /**
   * Create a new TGA Loader
   */
  public TGAImageLoader() {
  }

  /**
   * Flip the endian-ness of the short
   *
   * @param signedShort The short to flip
   * @return The flipped short
   */
  private short flipEndian(short signedShort) {
    int input = signedShort & 0xFFFF;
    return (short) (input << 8 | (input & 0xFF00) >>> 8);
  }

  @Override
  public int getDepth() {
    return pixelDepth;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getTexWidth() {
    return texWidth;
  }

  @Override
  public int getTexHeight() {
    return texHeight;
  }

  @Nonnull
  @Override
  public ByteBuffer loadImage(@Nonnull @WillNotClose InputStream fis) throws IOException {
    return loadImage(fis, false, null);
  }

  @Nonnull
  @Override
  public ByteBuffer loadImage(@Nonnull @WillNotClose InputStream fis, boolean flipped, int[] transparent)
      throws IOException {
    return loadImage(fis, flipped, true, transparent);
  }

  @SuppressWarnings("ConstantConditions")
  @Nonnull
  @Override
  public ByteBuffer loadImage(
      @Nonnull @WillNotClose InputStream fis, boolean flipped, boolean forceAlpha,
      @Nullable int[] transparent) throws IOException {
    if (transparent != null) {
      forceAlpha = true;
    }
    byte red;
    byte green;
    byte blue;
    byte alpha;

    BufferedInputStream bis = new BufferedInputStream(fis, 100000);
    DataInputStream dis = new DataInputStream(bis);

    // Read in the Header
    short idLength = (short) dis.read();
    dis.skipBytes(11);

    width = flipEndian(dis.readShort());
    height = flipEndian(dis.readShort());
    pixelDepth = (short) dis.read();
    if (pixelDepth == 32) {
      forceAlpha = false;
    }

    texWidth = get2Fold(width);
    texHeight = get2Fold(height);

    short imageDescriptor = (short) dis.read();
    if ((imageDescriptor & 0x0020) == 0) {
      flipped = !flipped;
    }

    // Skip image ID
    if (idLength > 0) {
      long skipped = 0;
      while (skipped < idLength) {
        skipped += bis.skip(idLength);
      }
    }

    byte[] rawData;
    if ((pixelDepth == 32) || (forceAlpha)) {
      pixelDepth = 32;
      rawData = new byte[texWidth * texHeight * 4];
    } else if (pixelDepth == 24) {
      rawData = new byte[texWidth * texHeight * 3];
    } else {
      throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
    }

    if (pixelDepth == 24) {
      if (flipped) {
        for (int i = height - 1; i >= 0; i--) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * texWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      } else {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * texWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      }
    } else if (pixelDepth == 32) {
      if (flipped) {
        for (int i = height - 1; i >= 0; i--) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (forceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * texWidth)) * 4);

            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
            rawData[ofs + 3] = alpha;

            if (alpha == 0) {
              rawData[ofs + 2] = (byte) 0;
              rawData[ofs + 1] = (byte) 0;
              rawData[ofs] = (byte) 0;
            }
          }
        }
      } else {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (forceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * texWidth)) * 4);

            if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
              rawData[ofs] = red;
              rawData[ofs + 1] = green;
              rawData[ofs + 2] = blue;
              rawData[ofs + 3] = alpha;
            } else {
              rawData[ofs] = red;
              rawData[ofs + 1] = green;
              rawData[ofs + 2] = blue;
              rawData[ofs + 3] = alpha;
            }

            if (alpha == 0) {
              rawData[ofs + 2] = 0;
              rawData[ofs + 1] = 0;
              rawData[ofs] = 0;
            }
          }
        }
      }
    }
    fis.close();

    if (transparent != null) {
      for (int i = 0; i < rawData.length; i += 4) {
        boolean match = true;
        for (int c = 0; c < 3; c++) {
          if (rawData[i + c] != transparent[c]) {
            match = false;
          }
        }

        if (match) {
          rawData[i + 3] = 0;
        }
      }
    }

    // Get a pointer to the image memory

    ByteBuffer scratch = createNativeOrderedByteBuffer(rawData.length);
    scratch.put(rawData);

    int perPixel = pixelDepth / 8;
    if (height < texHeight - 1) {
      int topOffset = (texHeight - 1) * (texWidth * perPixel);
      int bottomOffset = (height - 1) * (texWidth * perPixel);
      for (int x = 0; x < texWidth * perPixel; x++) {
        scratch.put(topOffset + x, scratch.get(x));
        scratch.put(bottomOffset + (texWidth * perPixel) + x,
            scratch.get((texWidth * perPixel) + x));
      }
    }
    if (width < texWidth - 1) {
      for (int y = 0; y < texHeight; y++) {
        for (int i = 0; i < perPixel; i++) {
          scratch.put(((y + 1) * (texWidth * perPixel)) - perPixel + i,
              scratch.get(y * (texWidth * perPixel) + i));
          scratch.put((y * (texWidth * perPixel)) + (width * perPixel) + i,
              scratch.get((y * (texWidth * perPixel)) + ((width - 1) * perPixel) + i));
        }
      }
    }

    scratch.flip();

    return scratch;
  }

  @Nonnull
  @Override
  public BufferedImage loadMouseCursorImageAsBufferedImage(@Nonnull @WillNotClose InputStream fis) throws IOException {
    return ImageIO.read(fis);
  }

  @Nonnull
  @Override
  public ByteBuffer loadMouseCursorImageAsByteBuffer(@Nonnull @WillNotClose InputStream fis) throws IOException {
    return loadImageInternal(fis, true, false, null, false, true);
  }

  @Nonnull
  @Override
  public ByteBuffer loadImageDirect(@Nonnull @WillNotClose InputStream fis) throws IOException {
    return loadImage(fis, false, true, null);
  }

  private ByteBuffer loadImageInternal(
          @Nonnull InputStream fis,
          boolean flipped,
          boolean forceAlpha,
          @Nullable int[] transparent,
          boolean forceNonePowerOfTwo,
          boolean modeARGB) throws IOException {
    if (transparent != null) {
      forceAlpha = true;
    }
    byte red;
    byte green;
    byte blue;
    byte alpha;

    BufferedInputStream bis = new BufferedInputStream(fis, 100000);
    DataInputStream dis = new DataInputStream(bis);

    // Read in the Header
    short idLength = (short) dis.read();
    dis.skipBytes(11);
    /*
    short colorMapType = (short) dis.read();
		short imageType = (short) dis.read();
		short cMapStart = flipEndian(dis.readShort());
		short cMapLength = flipEndian(dis.readShort());
		short cMapDepth = (short) dis.read();
		short xOffset = flipEndian(dis.readShort());
		short yOffset = flipEndian(dis.readShort());
		*/

    width = flipEndian(dis.readShort());
    height = flipEndian(dis.readShort());
    pixelDepth = (short) dis.read();
    if (pixelDepth == 32) {
      forceAlpha = false;
    }

    texWidth = width;
    texHeight = height;
    if (forceNonePowerOfTwo) {
      texWidth = get2Fold(width);
      texHeight = get2Fold(height);
    }

    short imageDescriptor = (short) dis.read();
    if ((imageDescriptor & 0x0020) == 0) {
      flipped = !flipped;
    }

    // Skip image ID
    if (idLength > 0) {
      int skipped = 0;
      while (skipped < idLength) {
        skipped += bis.skip(idLength - skipped);
      }
    }

    byte[] rawData;
    if ((pixelDepth == 32) || (forceAlpha)) {
      pixelDepth = 32;
      rawData = new byte[texWidth * texHeight * 4];
    } else if (pixelDepth == 24) {
      rawData = new byte[texWidth * texHeight * 3];
    } else {
      throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
    }

    if (pixelDepth == 24) {
      if (flipped) {
        for (int i = height - 1; i >= 0; i--) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * texWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      } else {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * texWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      }
    } else if (pixelDepth == 32) {
      if (flipped) {
        for (int i = height - 1; i >= 0; i--) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (forceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * texWidth)) * 4);

            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
            rawData[ofs + 3] = alpha;

            if (alpha == 0) {
              rawData[ofs + 2] = (byte) 0;
              rawData[ofs + 1] = (byte) 0;
              rawData[ofs] = (byte) 0;
            }
          }
        }
      } else {
        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (forceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * texWidth)) * 4);

            if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
              rawData[ofs] = red;
              rawData[ofs + 1] = green;
              rawData[ofs + 2] = blue;
              rawData[ofs + 3] = alpha;
            } else {
              rawData[ofs] = red;
              rawData[ofs + 1] = green;
              rawData[ofs + 2] = blue;
              rawData[ofs + 3] = alpha;
            }

            if (alpha == 0) {
              rawData[ofs + 2] = 0;
              rawData[ofs + 1] = 0;
              rawData[ofs] = 0;
            }
          }
        }
      }
    }
    fis.close();

    if (transparent != null) {
      for (int i = 0; i < rawData.length; i += 4) {
        boolean match = true;
        for (int c = 0; c < 3; c++) {
          if (rawData[i + c] != transparent[c]) {
            match = false;
          }
        }

        if (match) {
          rawData[i + 3] = 0;
        }
      }
    }

    // Get a pointer to the image memory
    ByteBuffer scratch = createNativeOrderedByteBuffer(rawData.length);
    scratch.put(rawData);

    int perPixel = pixelDepth / 8;
    if (height < texHeight - 1) {
      int topOffset = (texHeight - 1) * (texWidth * perPixel);
      int bottomOffset = (height - 1) * (texWidth * perPixel);
      for (int x = 0; x < texWidth * perPixel; x++) {
        scratch.put(topOffset + x, scratch.get(x));
        scratch.put(bottomOffset + (texWidth * perPixel) + x, scratch.get((texWidth * perPixel) + x));
      }
    }
    if (width < texWidth - 1) {
      for (int y = 0; y < texHeight; y++) {
        for (int i = 0; i < perPixel; i++) {
          scratch.put(((y + 1) * (texWidth * perPixel)) - perPixel + i, scratch.get(y * (texWidth * perPixel) + i));
          scratch.put((y * (texWidth * perPixel)) + (width * perPixel) + i, scratch.get((y * (texWidth * perPixel)) + ((width - 1) * perPixel) + i));
        }
      }
    }

    scratch.flip();

    return scratch;
  }

  private ByteBuffer createNativeOrderedByteBuffer(final int numBytes) {
    return ByteBuffer.allocateDirect(numBytes).order(ByteOrder.nativeOrder());
  }

  /**
   * Get the closest greater power of 2 to the fold number
   *
   * @param fold The target number
   * @return The power of 2
   */
  private int get2Fold(int fold) {
    int ret = 2;
    while (ret < fold) {
      ret *= 2;
    }
    return ret;
  }

  @Override
  @Nonnull
  public ByteBuffer getImageBufferData() {
    throw new RuntimeException("TGAImageData doesn't store it's image.");
  }

  public void configureEdging(boolean edging) {
  }
}
