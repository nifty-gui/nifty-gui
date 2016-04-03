package de.lessvoid.nifty.renderer.opengl.font.jglfont.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLBitmapFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.KerningsLine;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


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
