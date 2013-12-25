package de.lessvoid.nifty.renderer.jogl.render.io;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageData {
  int getDepth();

  int getHeight();

  int getTexHeight();

  int getTexWidth();

  int getWidth();

  @Nonnull
  ByteBuffer getImageBufferData();

  @Nonnull
  ByteBuffer loadImage(@Nonnull @WillNotClose InputStream fis) throws IOException;

  @SuppressWarnings("MethodCanBeVariableArityMethod")
  @Nonnull
  ByteBuffer loadImage(@Nonnull @WillNotClose InputStream fis, boolean flipped, int[] transparent) throws IOException;

  @SuppressWarnings("MethodCanBeVariableArityMethod")
  @Nonnull
  ByteBuffer loadImage(
      @Nonnull @WillNotClose InputStream fis,
      boolean flipped,
      boolean forceAlpha,
      int[] transparent) throws IOException;

  @Nonnull
  ByteBuffer loadImageDirect(@Nonnull @WillNotClose InputStream fis) throws IOException;

  @Nonnull
  BufferedImage loadMouseCursorImage(@Nonnull @WillNotClose InputStream fis) throws IOException;
}