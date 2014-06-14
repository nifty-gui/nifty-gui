package de.lessvoid.nifty.internal.render.io;

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
}
