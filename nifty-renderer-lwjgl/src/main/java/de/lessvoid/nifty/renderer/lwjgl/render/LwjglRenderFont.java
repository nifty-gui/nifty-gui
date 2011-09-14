package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.renderer.lwjgl.render.font.CharacterInfo;
import de.lessvoid.nifty.renderer.lwjgl.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;

public class LwjglRenderFont implements RenderFont {
  private Font font;

  public LwjglRenderFont(final String name, final RenderDevice device) {
    font = new Font(device);
    font.init(name);
  }

  public int getHeight() {
    return font.getHeight();
  }

  public int getWidth(final String text) {
    return font.getStringWidth(text);
  }

  public static int getKerning(final CharacterInfo charInfoC, final char nextc) {
    Integer kern = charInfoC.getKerning().get(Character.valueOf(nextc));
    if (kern != null) {
      return kern.intValue();
    }
    return 0;
  }

  public int getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) {
    CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
    if (currentCharacterInfo == null) {
      return -1;
    } else {
      return (int) (currentCharacterInfo.getXadvance() * size + getKerning(currentCharacterInfo, nextCharacter));
    }
  }

  public Font getFont() {
    return font;
  }

  public void dispose() {
  }  
}
