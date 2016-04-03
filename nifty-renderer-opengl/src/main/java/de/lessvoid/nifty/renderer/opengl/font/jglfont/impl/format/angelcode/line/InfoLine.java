package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;

/**
 * InfoLine
 * @author void
 */
public class InfoLine implements AngelCodeLine {

  @Override
  public boolean process(final AngelCodeLineData line, final JGLAbstractFontData font) {
    if (!line.hasValue("face")) {
      return false;
    }
    font.setName(line.getString("face"));
    return true;
  }
}