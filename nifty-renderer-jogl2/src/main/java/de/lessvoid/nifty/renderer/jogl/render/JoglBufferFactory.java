package de.lessvoid.nifty.renderer.jogl.render;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.batch.spi.BufferFactory;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglBufferFactory implements BufferFactory {
  @Nonnull
  @Override
  public ByteBuffer createNativeOrderedByteBuffer(final int numBytes) {
    return Buffers.newDirectByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  public FloatBuffer createNativeOrderedFloatBuffer(final int numFloats) {
    return Buffers.newDirectFloatBuffer(numFloats);
  }

  @Nonnull
  @Override
  public IntBuffer createNativeOrderedIntBuffer(final int numInts) {
    return Buffers.newDirectIntBuffer(numInts);
  }
}
