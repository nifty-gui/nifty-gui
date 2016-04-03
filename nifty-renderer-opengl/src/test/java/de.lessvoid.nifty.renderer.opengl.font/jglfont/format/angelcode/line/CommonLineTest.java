package de.lessvoid.nifty.renderer.opengl.font.jglfont.format.angelcode.line;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLBitmapFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.AngelCodeLineData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.CommonLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CommonLineTest {
  private CommonLine commonLine = new CommonLine();
  private AngelCodeLineData line = new AngelCodeLineData();
  private JGLAbstractFontData font = new JGLBitmapFontData(null, null, null);

  @Test
  public void testMandatoryAttributesMissing() {
    assertFalse(commonLine.process(line, font));

    assertTrue(font.getBitmaps().isEmpty());
    assertTrue(font.getGlyphs().isEmpty());
    assertEquals(0, font.getBitmapWidth());
    assertEquals(0, font.getBitmapHeight());
  }

  @Test
  public void testWithAttributes() {
    line.put("scaleW", "1000");
    line.put("scaleH", "700");

    assertTrue(commonLine.process(line, font));

    assertTrue(font.getBitmaps().isEmpty());
    assertTrue(font.getGlyphs().isEmpty());
    assertEquals(1000, font.getBitmapWidth());
    assertEquals(700, font.getBitmapHeight());
  }
}
