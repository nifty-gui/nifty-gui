package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.render.batch.spi.BufferFactory;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class LwjglBufferFactory implements BufferFactory {
  @Nonnull
  @Override
  public ByteBuffer createNativeOrderedByteBuffer(final int numBytes) {
    return BufferUtils.createByteBuffer(numBytes);
  }

  @Nonnull
  @Override
  public FloatBuffer createNativeOrderedFloatBuffer(final int numFloats) {
    return BufferUtils.createFloatBuffer(numFloats);
  }

  @Nonnull
  @Override
  public IntBuffer createNativeOrderedIntBuffer(final int numInts) {
    return BufferUtils.createIntBuffer(numInts);
  }
}
