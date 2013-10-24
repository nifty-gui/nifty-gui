package de.lessvoid.nifty.render.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

public interface ImageLoader {
  public int getDepth();
  public int getHeight();
  public int getTexHeight();
  public int getTexWidth();
  public int getWidth();
  @Nonnull
  public ByteBuffer getImageBufferData();

  @Nonnull
  public ByteBuffer loadImage(@Nonnull @WillNotClose InputStream fis) throws IOException;

  @SuppressWarnings("MethodCanBeVariableArityMethod")
  @Nonnull
  public ByteBuffer loadImage(
          @Nonnull @WillNotClose InputStream fis,
          boolean flipped,
          int[] transparent) throws IOException;

  @SuppressWarnings("MethodCanBeVariableArityMethod")
  @Nonnull
  public ByteBuffer loadImage(
      @Nonnull @WillNotClose InputStream fis,
      boolean flipped,
      boolean forceAlpha,
      int[] transparent) throws IOException;

  @Nonnull
  public ByteBuffer loadImageDirect(@Nonnull @WillNotClose InputStream fis) throws IOException;

  @Nonnull
  public ByteBuffer loadMouseCursorImageAsByteBuffer(@Nonnull @WillNotClose InputStream fis) throws IOException;

  @Nonnull
  public BufferedImage loadMouseCursorImageAsBufferedImage(@Nonnull @WillNotClose InputStream fis) throws IOException;
}