package org.jglfont.impl.format.angelcode;

import org.jglfont.impl.format.JGLAbstractFontData;

/**
 * A Line interface implementation handles exactly one line type
 * from the Angelcode font file and extracts the data of the line
 * into the given JGLAbstractFontData.
 * @author void
 */
public interface AngelCodeLine {
  /**
   * Take the line which represents the current line with all data
   * split into key/value types and fill the corresponding properties
   * of the given JGLAbstractFontData font.
   * @param line the line
   * @param font the font
   */
  boolean process(AngelCodeLineData line, JGLAbstractFontData font);
}
