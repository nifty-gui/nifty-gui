package de.lessvoid.nifty.java2d.renderer;

import de.lessvoid.nifty.java2d.renderer.fonts.AngelCodeFont;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import java.util.Map;

public class RenderFontJava2dWithAngelCodeImpl implements RenderFont {
  @Nonnull
  @SuppressWarnings("deprecation")
  private final AngelCodeFont angelCodeFont;

  /**
   * preloaded images for each texture id of AngelCodeFont.
   */
  @Nonnull
  private final Map<String, RenderImage> textureImages;

  @Nonnull
  @SuppressWarnings("deprecation")
  public AngelCodeFont getAngelCodeFont() {
    return angelCodeFont;
  }

  public RenderFontJava2dWithAngelCodeImpl(
      @Nonnull @SuppressWarnings("deprecation") AngelCodeFont angelCodeFont,
      @Nonnull Map<String, RenderImage> textureImages) {
    this.angelCodeFont = angelCodeFont;
    this.textureImages = textureImages;
  }

  @Override
  public int getCharacterAdvance(
      char currentCharacter,
      char nextCharacter, float size) {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getWidth(@Nonnull String text) {
    return getWidth(text, 1.f);
  }

  @Override
  public int getWidth(@Nonnull String text, float size) {
    return 0;
  }

  @Override
  public void dispose() {

  }

  public RenderImage getRenderImage(String texture) {
    return textureImages.get(texture);
  }
}