package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * CommonLine
 * @author void
 */
public class CommonLine implements AngelCodeLine {

  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("scaleW") &&
        !line.hasValue("scaleH")) {
      return false;
    }
    font.setBitmapWidth(line.getInt("scaleW"));
    font.setBitmapHeight(line.getInt("scaleH"));
    return true;
  }
}