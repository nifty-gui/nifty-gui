package de.lessvoid.nifty.renderer.lwjgl.render.batch;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_BLEND_DST;
import static org.lwjgl.opengl.GL11.GL_BLEND_SRC;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL13.GL_ACTIVE_TEXTURE;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART;
import static org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART_INDEX;
import static org.lwjgl.opengl.GL31.glPrimitiveRestartIndex;

public class CoreProfileSaveGLState {
  private int currentProgram;
  private int textureBinding;
  private int activeTexture;
  private boolean blending;
  private int blendingSrcFactor;
  private int blendingDstFactor;
  private boolean primitiveRestart;
  private int primitiveRestartIndex;

  public void saveCore() {
    currentProgram = glGetInteger(GL_CURRENT_PROGRAM);
    activeTexture = glGetInteger(GL_ACTIVE_TEXTURE);
    textureBinding = glGetInteger(GL_TEXTURE_BINDING_2D);
    blending = glIsEnabled(GL_BLEND);
    blendingSrcFactor = glGetInteger(GL_BLEND_SRC);
    blendingDstFactor = glGetInteger(GL_BLEND_DST);
    primitiveRestart = glIsEnabled(GL_PRIMITIVE_RESTART);
    primitiveRestartIndex = glGetInteger(GL_PRIMITIVE_RESTART_INDEX);
  }

  public void restoreCore() {
    glUseProgram(currentProgram);
    glActiveTexture(activeTexture);
    glBindTexture(GL_TEXTURE_2D, textureBinding);
    enable(GL_BLEND, blending);
    glBlendFunc(blendingSrcFactor, blendingDstFactor);
    enable(GL_PRIMITIVE_RESTART, primitiveRestart);
    glPrimitiveRestartIndex(primitiveRestartIndex);
  }

  private void enable(final int state, final boolean value) {
    if (value) {
      glEnable(state);
    } else {
      glDisable(state);
    }
  }
}
