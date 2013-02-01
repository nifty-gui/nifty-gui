package de.lessvoid.nifty.batch;

import java.io.IOException;

import org.jglfont.BitmapFont;
import org.jglfont.BitmapFontFactory;

import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class BatchRenderFont implements RenderFont {
  private final BitmapFont font;

  public BatchRenderFont(
      final String name,
      final BitmapFontFactory factory,
      final NiftyResourceLoader resourceLoader) throws IOException {
    font = factory.loadFont(resourceLoader.getResourceAsStream(name));
  }

  @Override
  public int getHeight() {
    return font.getHeight();
  }

  @Override
  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  @Override
  public int getWidth(final String text, final float size) {
    return font.getStringWidth(text, size);
  }

  @Override
  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    return font.getCharacterWidth(currentCharacter, nextCharacter, size);
  }

  @Override
  public void dispose() {
  }

  public BitmapFont getBitmapFont() {
    return font;
  }
}
