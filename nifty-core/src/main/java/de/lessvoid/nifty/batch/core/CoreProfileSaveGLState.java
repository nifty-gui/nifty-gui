package de.lessvoid.nifty.batch.core;

import de.lessvoid.nifty.batch.spi.BufferFactory;
import de.lessvoid.nifty.batch.spi.core.CoreGL;

import java.nio.IntBuffer;
import javax.annotation.Nonnull;

/**
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *
 * Note: Requires OpenGL 3.2 or greater.
 */
public class CoreProfileSaveGLState {
  @Nonnull
  private final CoreGL gl;
  @Nonnull
  private final IntBuffer params;
  private int currentProgram;
  private int textureBinding;
  private int activeTexture;
  private boolean blending;
  private int blendingSrcFactor;
  private int blendingDstFactor;
  private boolean primitiveRestart;
  private int primitiveRestartIndex;

  public CoreProfileSaveGLState(@Nonnull final CoreGL gl, @Nonnull final BufferFactory bufferFactory) {
    this.gl = gl;
    params = bufferFactory.createNativeOrderedIntBuffer(1);
  }

  public void saveCore() {
    params.clear();
    gl.glGetIntegerv(gl.GL_CURRENT_PROGRAM(), params);
    currentProgram = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_ACTIVE_TEXTURE(), params);
    activeTexture = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_TEXTURE_BINDING_2D(), params);
    textureBinding = params.get(0);

    blending = gl.glIsEnabled(gl.GL_BLEND());

    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_SRC(), params);
    blendingSrcFactor = params.get(0);

    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_DST(), params);
    blendingDstFactor = params.get(0);

    primitiveRestart = gl.glIsEnabled(gl.GL_PRIMITIVE_RESTART());

    params.clear();
    gl.glGetIntegerv(gl.GL_PRIMITIVE_RESTART_INDEX(), params);
    primitiveRestartIndex = params.get(0);
  }

  public void restoreCore() {
    gl.glUseProgram(currentProgram);
    gl.glActiveTexture(activeTexture);
    gl.glBindTexture(gl.GL_TEXTURE_2D(), textureBinding);
    enable(gl.GL_BLEND(), blending);
    gl.glBlendFunc(blendingSrcFactor, blendingDstFactor);
    enable(gl.GL_PRIMITIVE_RESTART(), primitiveRestart);
    gl.glPrimitiveRestartIndex(primitiveRestartIndex);
  }

  private void enable(final int state, final boolean value) {
    if (value) {
      gl.glEnable(state);
    } else {
      gl.glDisable(state);
    }
  }
}
