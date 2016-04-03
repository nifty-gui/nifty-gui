package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLFontGlyphInfo;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * KerningLine
 * @author void
 */
public class KerningLine implements AngelCodeLine {

  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("first") ||
        !line.hasValue("second") ||
        !line.hasValue("amount")) {
      return false;
    }
    int first = line.getInt("first");
    int second = line.getInt("second");
    int amount = line.getInt("amount");

    JGLFontGlyphInfo info = font.getGlyphs().get(first);
    if (info == null) {
      return false;
    }
    info.getKerning().put(second, amount);
    return true;
  }
}