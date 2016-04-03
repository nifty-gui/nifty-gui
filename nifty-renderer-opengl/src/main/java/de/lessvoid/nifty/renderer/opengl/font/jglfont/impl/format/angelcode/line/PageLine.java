package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * PageLine
 * @author void
 */
public class PageLine implements AngelCodeLine {

  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("id") ||
        !line.hasValue("file")) {
      return false;
    }
    font.addBitmap(line.getInt("id"), line.getString("file"));
    return true;
  }
}