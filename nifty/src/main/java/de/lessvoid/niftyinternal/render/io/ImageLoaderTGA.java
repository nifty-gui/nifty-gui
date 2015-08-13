package de.lessvoid.niftyinternal.render.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

/**
 * A utility to load TGAs (NOT THREAD SAFE). Fresh cut of code but largely influenced by the TGA loading class provided
 * as part of the Java Monkey Engine (JME). Why not check out what they're doing over at http://www.jmonkeyengine.com.
 * Kudos to Mark Powell.
 *
 * @author Kevin Glass, Julien Gouesse (JOGL 2 port)
 */
public class ImageLoaderTGA implements ImageLoader {
  private int imageWidth;
  private int imageHeight;
  private short imageBitDepth;
  private int textureWidth;
  private int textureHeight;
  private boolean shouldFlipVertically;
  private boolean shouldForceAlpha;

  @Override
  public int getImageBitDepth() {
    return imageBitDepth;
  }

  @Override
  public int getImageWidth() {
    return imageWidth;
  }

  @Override
  public int getImageHeight() {
    return imageHeight;
  }

  @Override
  public int getTextureWidth() {
    return textureWidth;
  }

  @Override
  public int getTextureHeight() {
    return textureHeight;
  }

  @Nonnull
  @Override
  public ByteBuffer loadAsByteBufferRGBA(@Nonnull @WillNotClose final InputStream imageStream) throws IOException {
    return loadImage(imageStream, false, true, null);
  }

  // Internal implementations

  // TODO Refactor.
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  private ByteBuffer loadImage(
      @Nonnull @WillNotClose final InputStream imageStream,
      final boolean shouldFlipVertically,
      final boolean shouldForceAlpha,
      @Nullable final int[] transparency) throws IOException {
    this.shouldFlipVertically = shouldFlipVertically;
    this.shouldForceAlpha = shouldForceAlpha;
    if (transparency != null) {
      this.shouldForceAlpha = true;
    }
    byte red;
    byte green;
    byte blue;
    byte alpha;

    BufferedInputStream bis = new BufferedInputStream(imageStream, 100000);
    DataInputStream dis = new DataInputStream(bis);

    // Read in the Header
    short idLength = (short) dis.read();
    dis.skipBytes(11);

    imageWidth = flipEndian(dis.readShort());
    imageHeight = flipEndian(dis.readShort());
    imageBitDepth = (short) dis.read();
    if (imageBitDepth == 32) {
      this.shouldForceAlpha = false;
    }

    textureWidth = get2Fold(imageWidth);
    textureHeight = get2Fold(imageHeight);

    short imageDescriptor = (short) dis.read();
    if ((imageDescriptor & 0x0020) == 0) {
      this.shouldFlipVertically = !this.shouldFlipVertically;
    }

    // Skip image ID
    if (idLength > 0) {
      long skipped = 0;
      while (skipped < idLength) {
        skipped += bis.skip(idLength);
      }
    }

    byte[] rawData;
    if ((imageBitDepth == 32) || (this.shouldForceAlpha)) {
      imageBitDepth = 32;
      rawData = new byte[textureWidth * textureHeight * 4];
    } else if (imageBitDepth == 24) {
      rawData = new byte[textureWidth * textureHeight * 3];
    } else {
      throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
    }

    if (imageBitDepth == 24) {
      if (this.shouldFlipVertically) {
        for (int i = imageHeight - 1; i >= 0; i--) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * textureWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      } else {
        for (int i = 0; i < imageHeight; i++) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * textureWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      }
    } else if (imageBitDepth == 32) {
      if (this.shouldFlipVertically) {
        for (int i = imageHeight - 1; i >= 0; i--) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (this.shouldForceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * textureWidth)) * 4);

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
        for (int i = 0; i < imageHeight; i++) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (this.shouldForceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * textureWidth)) * 4);

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
    imageStream.close();

    if (transparency != null) {
      for (int i = 0; i < rawData.length; i += 4) {
        boolean match = true;
        for (int c = 0; c < 3; c++) {
          if (rawData[i + c] != transparency[c]) {
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

    int perPixel = imageBitDepth / 8;
    if (imageHeight < textureHeight - 1) {
      int topOffset = (textureHeight - 1) * (textureWidth * perPixel);
      int bottomOffset = (imageHeight - 1) * (textureWidth * perPixel);
      for (int x = 0; x < textureWidth * perPixel; x++) {
        scratch.put(topOffset + x, scratch.get(x));
        scratch.put(bottomOffset + (textureWidth * perPixel) + x,
            scratch.get((textureWidth * perPixel) + x));
      }
    }
    if (imageWidth < textureWidth - 1) {
      for (int y = 0; y < textureHeight; y++) {
        for (int i = 0; i < perPixel; i++) {
          scratch.put(((y + 1) * (textureWidth * perPixel)) - perPixel + i,
              scratch.get(y * (textureWidth * perPixel) + i));
          scratch.put((y * (textureWidth * perPixel)) + (imageWidth * perPixel) + i,
              scratch.get((y * (textureWidth * perPixel)) + ((imageWidth - 1) * perPixel) + i));
        }
      }
    }

    scratch.flip();

    return scratch;
  }

  // TODO Refactor.
  @Nonnull
  private ByteBuffer loadImage(
          @Nonnull final InputStream imageStream,
          final boolean shouldFlipVertically,
          final boolean shouldForceAlpha,
          @Nullable final int[] transparency,
          final boolean forceNonePowerOfTwo,
          final boolean modeARGB) throws IOException {
    this.shouldFlipVertically = shouldFlipVertically;
    this.shouldForceAlpha = shouldForceAlpha;
    if (transparency != null) {
      this.shouldForceAlpha = true;
    }
    byte red;
    byte green;
    byte blue;
    byte alpha;

    BufferedInputStream bis = new BufferedInputStream(imageStream, 100000);
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

    imageWidth = flipEndian(dis.readShort());
    imageHeight = flipEndian(dis.readShort());
    imageBitDepth = (short) dis.read();
    if (imageBitDepth == 32) {
      this.shouldForceAlpha = false;
    }

    textureWidth = imageWidth;
    textureHeight = imageHeight;
    if (forceNonePowerOfTwo) {
      textureWidth = get2Fold(imageWidth);
      textureHeight = get2Fold(imageHeight);
    }

    short imageDescriptor = (short) dis.read();
    if ((imageDescriptor & 0x0020) == 0) {
      this.shouldFlipVertically = !this.shouldFlipVertically;
    }

    // Skip image ID
    if (idLength > 0) {
      int skipped = 0;
      while (skipped < idLength) {
        skipped += bis.skip(idLength - skipped);
      }
    }

    byte[] rawData;
    if ((imageBitDepth == 32) || (this.shouldForceAlpha)) {
      imageBitDepth = 32;
      rawData = new byte[textureWidth * textureHeight * 4];
    } else if (imageBitDepth == 24) {
      rawData = new byte[textureWidth * textureHeight * 3];
    } else {
      throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
    }

    if (imageBitDepth == 24) {
      if (this.shouldFlipVertically) {
        for (int i = imageHeight - 1; i >= 0; i--) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * textureWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      } else {
        for (int i = 0; i < imageHeight; i++) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();

            int ofs = ((j + (i * textureWidth)) * 3);
            rawData[ofs] = red;
            rawData[ofs + 1] = green;
            rawData[ofs + 2] = blue;
          }
        }
      }
    } else if (imageBitDepth == 32) {
      if (this.shouldFlipVertically) {
        for (int i = imageHeight - 1; i >= 0; i--) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (this.shouldForceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * textureWidth)) * 4);

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
        for (int i = 0; i < imageHeight; i++) {
          for (int j = 0; j < imageWidth; j++) {
            blue = dis.readByte();
            green = dis.readByte();
            red = dis.readByte();
            if (this.shouldForceAlpha) {
              alpha = (byte) 255;
            } else {
              alpha = dis.readByte();
            }

            int ofs = ((j + (i * textureWidth)) * 4);

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
    imageStream.close();

    if (transparency != null) {
      for (int i = 0; i < rawData.length; i += 4) {
        boolean match = true;
        for (int c = 0; c < 3; c++) {
          if (rawData[i + c] != transparency[c]) {
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

    int perPixel = imageBitDepth / 8;
    if (imageHeight < textureHeight - 1) {
      int topOffset = (textureHeight - 1) * (textureWidth * perPixel);
      int bottomOffset = (imageHeight - 1) * (textureWidth * perPixel);
      for (int x = 0; x < textureWidth * perPixel; x++) {
        scratch.put(topOffset + x, scratch.get(x));
        scratch.put(bottomOffset + (textureWidth * perPixel) + x, scratch.get((textureWidth * perPixel) + x));
      }
    }
    if (imageWidth < textureWidth - 1) {
      for (int y = 0; y < textureHeight; y++) {
        for (int i = 0; i < perPixel; i++) {
          scratch.put(((y + 1) * (textureWidth * perPixel)) - perPixel + i, scratch.get(y * (textureWidth * perPixel) + i));
          scratch.put((y * (textureWidth * perPixel)) + (imageWidth * perPixel) + i, scratch.get((y * (textureWidth * perPixel)) + ((imageWidth - 1) * perPixel) + i));
        }
      }
    }

    scratch.flip();

    return scratch;
  }

  /**
   * Flip the endian-ness of the short
   *
   * @param signedShort The short to flip
   * @return The flipped short
   */
  private short flipEndian(final short signedShort) {
    int input = signedShort & 0xFFFF;
    return (short) (input << 8 | (input & 0xFF00) >>> 8);
  }

  @Nonnull
  private ByteBuffer createNativeOrderedByteBuffer(final int numBytes) {
    return ByteBuffer.allocateDirect(numBytes).order(ByteOrder.nativeOrder());
  }

  /**
   * Get the closest greater power of 2 to the fold number
   *
   * @param fold The target number
   * @return The power of 2
   */
  private int get2Fold(final int fold) {
    int ret = 2;
    while (ret < fold) {
      ret *= 2;
    }
    return ret;
  }
}
