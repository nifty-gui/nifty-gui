package de.lessvoid.nifty.renderer.lwjgl;

import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreTexture2D.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D.ResizeFilter;
import de.lessvoid.coregl.CoreTexture2D.Type;
import de.lessvoid.nifty.spi.NiftyTexture;

public class NiftyTextureLwjgl implements NiftyTexture {
  final CoreTexture2D texture;

  public NiftyTextureLwjgl(final CoreFactory coreFactory, final int width, final int height) {
    texture = coreFactory.createEmptyTexture(ColorFormat.RGBA, Type.UNSIGNED_BYTE, width, height, ResizeFilter.Linear);
  }

  public void bind() {
    texture.bind();
  }

  public int getWidth() {
    return texture.getWidth();
  }

  public int getHeight() {
    return texture.getHeight();
  }
}
