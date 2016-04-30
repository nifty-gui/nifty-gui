package org.jglfont.format.angelcode.line;

import static org.junit.Assert.*;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLBitmapFontData;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.jglfont.impl.format.angelcode.line.CommonLine;
import org.junit.Test;


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
