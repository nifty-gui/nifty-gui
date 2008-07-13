package de.lessvoid.nifty.render.spi.lwjgl;

import org.lwjgl.opengl.GL11;

/**
 * RenderTools.
 * @author void
 */
public final class RenderTools {

  /**
   * can't instantiate this class.
   */
  private RenderTools() {
  }

  /**
   * helper to prepare render state.
   */
  public static void beginRender() {
    GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_ENABLE_BIT);
    GL11.glPushMatrix();
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

  /**
   * helper to restore state.
   */
  public static void endRender() {
    GL11.glPopMatrix();
    GL11.glPopAttrib();
  }
}
