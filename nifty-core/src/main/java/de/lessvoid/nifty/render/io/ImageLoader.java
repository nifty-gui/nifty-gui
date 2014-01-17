package de.lessvoid.nifty.render.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

public interface ImageLoader {
  public int getImageBitDepth();
  public int getImageHeight();
  public int getImageWidth();
  public int getTextureHeight();
  public int getTextureWidth();

  @Nonnull
  public ByteBuffer loadAsByteBufferRGBA(@Nonnull @WillNotClose final InputStream imageStream) throws IOException;

  @Nonnull
  public ByteBuffer loadAsByteBufferARGB(
          @Nonnull @WillNotClose final InputStream imageStream,
          final boolean shouldFlipVertically) throws IOException;

  @Nonnull
  public BufferedImage loadAsBufferedImage(@Nonnull @WillNotClose final InputStream imageStream) throws IOException;
}