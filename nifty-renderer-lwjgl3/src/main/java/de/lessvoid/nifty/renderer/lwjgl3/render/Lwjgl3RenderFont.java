package de.lessvoid.nifty.renderer.lwjgl3.render;

import de.lessvoid.nifty.renderer.lwjgl3.render.font.CharacterInfo;
import de.lessvoid.nifty.renderer.lwjgl3.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;

public class Lwjgl3RenderFont implements RenderFont {
  @Nonnull
  private final Font font;

  public Lwjgl3RenderFont(@Nonnull final String name, @Nonnull final NiftyResourceLoader resourceLoader) {
    font = new Font(name, resourceLoader);
  }

  @Override
  public int getHeight() {
    return font.getHeight();
  }

  @Override
  public int getWidth(@Nonnull final String text) {
    return font.getStringWidth(text, 1.f);
  }

  @Override
  public int getWidth(@Nonnull final String text, final float size) {
    return font.getStringWidth(text, size);
  }

  public static int getKerning(@Nonnull final CharacterInfo charInfoC, final char nextc) {
    Integer kern = charInfoC.getKerning().get(Character.valueOf(nextc));
    if (kern != null) {
      return kern;
    }
    return 0;
  }

  @Override
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  @Nonnull
  public Font getFont() {
    return font;
  }

  @Override
  public void dispose() {
  }
}
