package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.renderer.jogl.render.font.CharacterInfo;
import de.lessvoid.nifty.renderer.jogl.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class JoglRenderFont implements RenderFont {
    private Font font;

    public JoglRenderFont(final String name, final RenderDevice device, final NiftyResourceLoader resourceLoader) {
        font = new Font(device, resourceLoader);
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

    public int getCharacterAdvance(final char currentCharacter, final char nextCharacter,
            final float size) {
        CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
        if (currentCharacterInfo == null) {
            return 0;
        }
        else {
            return Integer.valueOf((int) (currentCharacterInfo.getXadvance() * size + getKerning(
                    currentCharacterInfo, nextCharacter)));
        }
    }

    public Font getFont() {
        return font;
    }

    public void dispose() {
    }

    @Override
    public int getWidth(String text, float size) {
      return font.getStringWidth(text, size);
    }
}
