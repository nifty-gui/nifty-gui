package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.utils.BufferUtils;

import de.lessvoid.nifty.render.batch.spi.BufferFactory;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxBufferFactory implements BufferFactory {
  @Nonnull
  @Override
  public ByteBuffer createNativeOrderedByteBuffer(final int numBytes) {
    return BufferUtils.newByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  public FloatBuffer createNativeOrderedFloatBuffer(final int numFloats) {
    return BufferUtils.newFloatBuffer(numFloats);
  }

  @Nonnull
  @Override
  public IntBuffer createNativeOrderedIntBuffer(final int numInts) {
    return BufferUtils.newIntBuffer(numInts);
  }
}
