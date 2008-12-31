package de.lessvoid.nifty.render.spi.lwjgl;

import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.spi.BlendMode;

/**
 * RenderTools.
 * @author void
 */
public class RenderTools {
  private BlendMode renderMode = BlendMode.BLEND;

  /**
   * helper to prepare render state.
   */
  public void beginRender() {
    GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_ENABLE_BIT);
    GL11.glPushMatrix();
    if (renderMode.equals(BlendMode.BLEND)) {
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    } else if (renderMode.equals(BlendMode.MULIPLY)) {
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
    }
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

  /**
   * helper to restore state.
   */
  public void endRender() {
    GL11.glPopMatrix();
    GL11.glPopAttrib();
  }

  public void changeRenderMode(final BlendMode renderModeParam) {
    renderMode = renderModeParam;
  }
}
