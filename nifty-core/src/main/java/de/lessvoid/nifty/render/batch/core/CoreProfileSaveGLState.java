package de.lessvoid.nifty.render.batch.core;

import java.nio.IntBuffer;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.BufferFactory;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

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
  private int samplerBindingTex0;
  private boolean blending;
  private int blendingSrcFactor;
  private int blendingDstFactor;
  private boolean primitiveRestart;
  private int primitiveRestartIndex;

  public CoreProfileSaveGLState(@Nonnull final CoreGL gl, @Nonnull final BufferFactory bufferFactory) {
    this.gl = gl;
    params = bufferFactory.createNativeOrderedIntBuffer(16);
  }

  public void saveCore() {
  	// Store currently bound program.
    params.clear();
    gl.glGetIntegerv(gl.GL_CURRENT_PROGRAM(), params);
    currentProgram = params.get(0);

    // Store currently active texture unit.
    params.clear();
    gl.glGetIntegerv(gl.GL_ACTIVE_TEXTURE(), params);
    activeTexture = params.get(0);

    // This texture unit will be used by Nifty.
    gl.glActiveTexture(gl.GL_TEXTURE0());

    // Store sampler bound to it.
    params.clear();
    gl.glGetIntegerv(gl.GL_SAMPLER_BINDING(), params);
    samplerBindingTex0 = params.get(0);
    
    // Now unbind sampler so it doesn't overrides texture filtering.
    gl.glBindSampler(0, 0);

    // Store texture bound to 2D target.
    params.clear();
    gl.glGetIntegerv(gl.GL_TEXTURE_BINDING_2D(), params);
    textureBinding = params.get(0);

    // Store blending state.
    blending = gl.glIsEnabled(gl.GL_BLEND());

    // Store blending source parameter.
    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_SRC(), params);
    blendingSrcFactor = params.get(0);

    // Store blending destination parameter.
    params.clear();
    gl.glGetIntegerv(gl.GL_BLEND_DST(), params);
    blendingDstFactor = params.get(0);

    // Store primitive restart state.
    primitiveRestart = gl.glIsEnabled(gl.GL_PRIMITIVE_RESTART());

    // Store primitive restart index.
    params.clear();
    gl.glGetIntegerv(gl.GL_PRIMITIVE_RESTART_INDEX(), params);
    primitiveRestartIndex = params.get(0);

    CheckGL.checkGLError(gl, "Failed to save OpenGL Core Profile state!", true);
  }

  public void restoreCore() {
  	// Restore program.
    gl.glUseProgram(currentProgram);
    // Restore sampler that was bound to texture unit 0.
    gl.glBindSampler(0, samplerBindingTex0);
    // Restore active texture unit.
    gl.glActiveTexture(activeTexture);
    // Restore bound texture to 2D target.
    gl.glBindTexture(gl.GL_TEXTURE_2D(), textureBinding);
    // Restore blending state.
    enable(gl.GL_BLEND(), blending);
    // Restore blending functions.
    gl.glBlendFunc(blendingSrcFactor, blendingDstFactor);
    // Restore primitive restart state.
    enable(gl.GL_PRIMITIVE_RESTART(), primitiveRestart);
    // Restore primitive restart index.
    gl.glPrimitiveRestartIndex(primitiveRestartIndex);

    CheckGL.checkGLError(gl, "Failed to restore OpenGL Core Profile state!", true);
  }

  private void enable(final int state, final boolean value) {
    if (value) {
      gl.glEnable(state);
    } else {
      gl.glDisable(state);
    }
  }
}
