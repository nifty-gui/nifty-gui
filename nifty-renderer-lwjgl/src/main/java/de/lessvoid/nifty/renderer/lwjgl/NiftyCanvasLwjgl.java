package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import de.lessvoid.coregl.CoreMatrixFactory;
import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreTexture2D.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D.ResizeFilter;
import de.lessvoid.nifty.spi.NiftyCanvas;

public class NiftyCanvasLwjgl implements NiftyCanvas {
  private final CoreTexture2D texture;
  private final int width;
  private final int height;

  public NiftyCanvasLwjgl(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.texture = CoreTexture2D.createEmptyTexture(ColorFormat.RGBA, GL11.GL_UNSIGNED_BYTE, width, height, ResizeFilter.Linear);
  }

  public void bindTexture() {
    texture.bind();
  }

  public int getTextureId() {
    return texture.getTextureId();
  }

  public int getHeight() {
    return texture.getHeight();
  }

  public int getWidth() {
    return texture.getWidth();
  }
}
