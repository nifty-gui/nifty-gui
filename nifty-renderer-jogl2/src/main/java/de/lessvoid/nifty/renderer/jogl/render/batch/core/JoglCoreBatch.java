package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import de.lessvoid.nifty.render.batch.core.CoreBatchInternal;
import de.lessvoid.nifty.render.batch.core.CoreTexture2D;
import de.lessvoid.nifty.render.batch.spi.core.CoreBatch;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglCoreBatch implements CoreBatch {
  @Nonnull
  private final CoreBatchInternal internalBatch;

  public JoglCoreBatch(@Nonnull final CoreBatchInternal internalBatch) {
    this.internalBatch = internalBatch;
  }

  @Override
  public void begin(@Nonnull BlendMode blendMode, CoreTexture2D texture) {
    internalBatch.begin(blendMode, texture);
  }

  @Nonnull
  @Override
  public BlendMode getBlendMode() {
    return internalBatch.getBlendMode();
  }

  @Override
  public void render() {
    internalBatch.render();
  }

  @Override
  public boolean canAddQuad() {
    return internalBatch.canAddQuad();
  }

  @Override
  public void addQuad(
          float x,
          float y,
          float width,
          float height,
          @Nonnull Color color1,
          @Nonnull Color color2,
          @Nonnull Color color3,
          @Nonnull Color color4,
          float textureX,
          float textureY,
          float textureWidth,
          float textureHeight) {
    internalBatch.addQuad(
            x,
            y,
            width,
            height,
            color1,
            color2,
            color3,
            color4,
            textureX,
            textureY,
            textureWidth,
            textureHeight);
  }
}
