package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.renderer.jogl.render.font.CharacterInfo;
import de.lessvoid.nifty.renderer.jogl.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;

public class JoglRenderFont implements RenderFont {
  @Nonnull
  private final Font font;

  public JoglRenderFont(
      @Nonnull final String name,
      final RenderDevice device,
      final NiftyResourceLoader resourceLoader) {
    font = new Font(name, resourceLoader);
  }

  @Override
  public int getHeight() {
    return font.getHeight();
  }

  @Override
  public int getWidth(@Nonnull final String text) {
    return font.getStringWidth(text);
  }

  public static int getKerning(@Nonnull final CharacterInfo charInfoC, final char nextc) {
    Integer kern = charInfoC.getKerning().get(nextc);
    if (kern != null) {
      return kern;
    }
    return 0;
  }

  @Override
  public int getCharacterAdvance(
      final char currentCharacter, final char nextCharacter,
      final float size) {
    CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
    if (currentCharacterInfo == null) {
      return 0;
    } else {
      return (int) (currentCharacterInfo.getXadvance() * size + getKerning(
          currentCharacterInfo, nextCharacter));
    }
  }

  @Nonnull
  public Font getFont() {
    return font;
  }

  @Override
  public void dispose() {
  }

  @Override
  public int getWidth(@Nonnull String text, float size) {
    return font.getStringWidth(text, size);
  }
}
