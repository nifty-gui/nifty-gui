package de.lessvoid.nifty.batch.spi.core;

import de.lessvoid.nifty.batch.core.CoreTexture2D;
import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;

/**
 * Interface for managing a "batch" of vertex data from a specific texture. The vertex data is accumulated via the
 * {@link #addQuad(float, float, float, float, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, float, float, float, float)}
 * method. A batch can only hold so many quads; the {@link #canAddQuad()} method should tell you when the batch is
 * full. The amount of vertex data that a batch can hold is implementation-specific. Place initialization routines in
 * {@link #begin(de.lessvoid.nifty.render.BlendMode, de.lessvoid.nifty.batch.core.CoreTexture2D)}. When starting a new
 * batch, you should call {@link #begin(de.lessvoid.nifty.render.BlendMode, de.lessvoid.nifty.batch.core.CoreTexture2D)}
 * to initialize the batch before calling {@link #render()}.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public interface CoreBatch {
  /**
   * Initializes the batch by specifying the blend mode and texture before a call to {@link #render()}.
   *
   * @param blendMode The {@link de.lessvoid.nifty.render.BlendMode} to render the batch with.
   * @param texture The texture that this batch's vertex data belongs to.
   */
  public void begin(@Nonnull BlendMode blendMode, final CoreTexture2D texture);

  /**
   * Gets the {@link de.lessvoid.nifty.render.BlendMode} that will be used to render this batch, that was specified in
   * {@link #begin(de.lessvoid.nifty.render.BlendMode, de.lessvoid.nifty.batch.core.CoreTexture2D)}.
   */
  @Nonnull
  public BlendMode getBlendMode();

  /**
   * Renders the batch's vertex data that was added with
   * {@link #addQuad(float, float, float, float, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, de.lessvoid.nifty.tools.Color, float, float, float, float)},
   * using the blend mode and texture id specified in
   * {@link #begin(de.lessvoid.nifty.render.BlendMode, de.lessvoid.nifty.batch.core.CoreTexture2D)}.
   */
  public void render();

  /**
   * A batch can only hold so many quads. This will tell you when the batch is full. The amount of vertex data that a
   * batch can hold is implementation-specific.
   */
  public boolean canAddQuad();

  /**
   * Adds a quad to the batch for later rendering with {@link #render()}.
   *
   * @see #canAddQuad()
   *
   * @param x The top left coordinate of the quad, in screen coordinates.
   * @param y The top left coordinate of the quad, in screen coordinates.
   * @param width The width of the quad, in screen coordinates.
   * @param height The height of the quad, in screen coordinates.
   * @param color1 The color of the quad's first (top left) vertex.
   * @param color2 The color of the quad's second (top right) vertex.
   * @param color3 The color of the quad's third (bottom right) vertex.
   * @param color4 The color of the quad's fourth (bottom left) vertex.
   * @param textureX The first (top left) texture coordinate of the texture to map onto the quad.
   * @param textureY The first (top left) texture coordinate of the texture to map onto the quad.
   * @param textureWidth The width of the texture to map onto the quad.
   * @param textureHeight The width of the texture to map onto the quad.
   */
  public void addQuad(
          final float x,
          final float y,
          final float width,
          final float height,
          @Nonnull final Color color1,
          @Nonnull final Color color2,
          @Nonnull final Color color3,
          @Nonnull final Color color4,
          final float textureX,
          final float textureY,
          final float textureWidth,
          final float textureHeight);
}
