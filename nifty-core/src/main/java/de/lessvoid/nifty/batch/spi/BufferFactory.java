package de.lessvoid.nifty.batch.spi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface BufferFactory {
  @Nonnull
  public ByteBuffer createNativeOrderedByteBuffer(final int numBytes);

  @Nonnull
  public FloatBuffer createNativeOrderedFloatBuffer(final int numFloats);

  @Nonnull
  public IntBuffer createNativeOrderedIntBuffer(final int numInts);
}
