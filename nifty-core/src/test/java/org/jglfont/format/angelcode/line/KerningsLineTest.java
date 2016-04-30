package org.jglfont.format.angelcode.line;

import static org.junit.Assert.assertTrue;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLBitmapFontData;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.jglfont.impl.format.angelcode.line.KerningsLine;
import org.junit.Test;


public class KerningsLineTest {
  private KerningsLine kerningsLine = new KerningsLine();
  private AngelCodeLineData line = new AngelCodeLineData();
  private JGLAbstractFontData font = new JGLBitmapFontData(null, null, null);

  @Test
  public void testNoImpl() {
    assertTrue(kerningsLine.process(line, font));
    assertTrue(font.getBitmaps().isEmpty());
    assertTrue(font.getGlyphs().isEmpty());
  }
}
