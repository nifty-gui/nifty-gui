package de.lessvoid.nifty.renderer.opengl.font.jglfont.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLBitmapFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.CharsLine;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class CharsLineTest {
  private CharsLine charsLine = new CharsLine();
  private AngelCodeLineData line = new AngelCodeLineData();
  private JGLAbstractFontData font = new JGLBitmapFontData(null, null, null);

  @Test
  public void testNoImpl() {
    assertTrue(charsLine.process(line, font));
    assertTrue(font.getBitmaps().isEmpty());
  }
}
