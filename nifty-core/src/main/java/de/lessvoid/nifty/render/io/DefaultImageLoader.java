package de.lessvoid.nifty.render.io;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.imageio.ImageIO;

/**
 * An image data provider that uses ImageIO to retrieve image data in a format suitable for creating OpenGL textures.
 * This implementation is used when formats not natively supported by the library are required.
 *
 * {@inheritDoc}
 *
 * @author Kevin Glass
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class DefaultImageLoader implements ImageLoader {
  @Nonnull
  private static final ColorModel GL_ALPHA_COLOR_MODEL = new ComponentColorModel(ColorSpace.getInstance(ColorSpace
          .CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
  @Nullable
  private ImageProperties imageProperties;

  @Override
  public int getImageBitDepth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image bit depth is not set!");
    }
    return imageProperties.getBitDepth();
  }

  @Override
  public int getImageWidth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image width is not set!");
    }
    return imageProperties.getWidth();
  }

  @Override
  public int getImageHeight() {
    if (imageProperties == null) {
      throw new IllegalStateException("Image height is not set!");
    }
    return imageProperties.getHeight();
  }

  @Override
  public int getTextureWidth() {
    if (imageProperties == null) {
      throw new IllegalStateException("Texture width is not set!");
    }
    return imageProperties.getWidth();
  }

  @Override
  public int getTextureHeight() {
    if (imageProperties == null) {
      throw new IllegalStateException("Texture height is not set!");
    }
    return imageProperties.getHeight();
  }

  @Override
  @Nonnull
  public ByteBuffer loadAsByteBufferRGBA(@Nonnull @WillNotClose final InputStream imageStream) throws IOException {
    return convertToOpenGlFormat(loadImageFromStream(imageStream), false, false);
  }

  @Nonnull
  @Override
  public ByteBuffer loadAsByteBufferARGB(
          @Nonnull @WillNotClose final InputStream imageStream,
          final boolean shouldFlipVertically) throws IOException {
    try {
      return convertToOpenGlFormat(loadImageFromStream(imageStream), shouldFlipVertically, true);
    } catch (IOException e) {
      throw new IOException("Could not load mouse cursor image!", e);
    }
  }

  @Nonnull
  @Override
  public BufferedImage loadAsBufferedImage(@Nonnull @WillNotClose InputStream imageStream) throws IOException {
    try {
      return loadImageFromStream(imageStream);
    } catch (IOException e) {
      throw new IOException("Could not load mouse cursor image!", e);
    }
  }

  // Internal implementations

  private BufferedImage loadImageFromStream(@Nonnull @WillNotClose final InputStream imageStream) throws IOException {
    BufferedImage image;
    try {
      image = ImageIO.read(imageStream);
    } catch (IOException e) {
      throw new IOException("Could not load image from stream as a buffered image!", e);
    }
    if (image == null) {
      throw new IOException("Could not load image from stream as a buffered image!");
    }
    return image;
  }

  @Nonnull
  private ByteBuffer convertToOpenGlFormat(
          @Nonnull final BufferedImage originalImage,
          final boolean shouldFlipVertically,
          final boolean shouldUseARGB) {

    ImageProperties originalImageProperties = new ImageProperties(
            originalImage.getWidth(),
            originalImage.getHeight(),
            shouldFlipVertically);

    imageProperties = originalImageProperties;

    BufferedImage openGlImage = createImageWithProperties(originalImageProperties);

    copyImage(originalImage, openGlImage, originalImageProperties);

    byte[] rawOpenGlImageData = getRawImageData(openGlImage);

    if (shouldUseARGB) {
      convertImageToARGB(rawOpenGlImageData);
    }

    ByteBuffer openGlImageByteBuffer = createByteBuffer(rawOpenGlImageData);

    // We can't dispose of openGlImage any earlier because it would destroy rawOpenGlImageData.
    disposeImage(openGlImage);

    return openGlImageByteBuffer;
  }

  private void copyImage(
          @Nonnull final BufferedImage sourceImage,
          @Nonnull final BufferedImage destinationImage,
          @Nonnull final ImageProperties sourceImageProperties) {
    if (sourceImageProperties.isFlipped()) {
      getImageGraphics(destinationImage).scale(1, -1);
      getImageGraphics(destinationImage).drawImage(sourceImage, 0, -sourceImageProperties.getHeight(), null);
    } else {
      getImageGraphics(destinationImage).drawImage(sourceImage, 0, 0, null);
    }
  }

  @Nonnull
  private BufferedImage createImageWithProperties(@Nonnull final ImageProperties imageProperties) {
    return new BufferedImage(
            imageProperties.getColorModel(),
            createRasterWithProperties(imageProperties),
            false,
            null);
  }

  @Nonnull
  private WritableRaster createRasterWithProperties(@Nonnull final ImageProperties imageProperties) {
    return Raster.createInterleavedRaster(
            DataBuffer.TYPE_BYTE,
            imageProperties.getWidth(),
            imageProperties.getHeight(),
            imageProperties.getColorBands(),
            null);
  }

  @Nonnull
  private ByteBuffer createByteBuffer(@Nonnull final byte[] data) {
    return (ByteBuffer) ByteBuffer.allocateDirect(data.length)
            .order(ByteOrder.nativeOrder())
            .put(data, 0, data.length)
            .flip();
  }

  @Nonnull
  private Graphics2D getImageGraphics(@Nonnull final BufferedImage image) {
    return (Graphics2D) image.getGraphics();
  }

  private void convertImageToARGB(@Nonnull final byte[] imageData) {
    for (int i = 0; i < imageData.length; i += 4) {
      byte rr = imageData[i];
      byte gg = imageData[i + 1];
      byte bb = imageData[i + 2];
      byte aa = imageData[i + 3];
      imageData[i] = bb;
      imageData[i + 1] = gg;
      imageData[i + 2] = rr;
      imageData[i + 3] = aa;
    }
  }

  @Nonnull
  private byte[] getRawImageData(@Nonnull final BufferedImage image) {
    return ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
  }

  private void disposeImage(@Nonnull final BufferedImage image) {
    getImageGraphics(image).dispose();
  }

  private static class ImageProperties {
    private final int width;
    private final int height;
    private final boolean isFlippedVertically;

    private ImageProperties(
            final int width,
            final int height,
            final boolean isFlippedVertically) {
      this.width = width;
      this.height = height;
      this.isFlippedVertically = isFlippedVertically;
    }

    private int getWidth() {
      return width;
    }

    private int getHeight() {
      return height;
    }

    private boolean isFlipped() {
      return isFlippedVertically;
    }

    private ColorModel getColorModel() {
      return GL_ALPHA_COLOR_MODEL;
    }

    private int getBitDepth() {
      return 32;
    }

    private int getColorBands() {
      return 4;
    }
  }
}
