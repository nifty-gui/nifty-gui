package org.jglfont.format.angelcode.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jglfont.impl.format.JGLAbstractFontData;
import org.jglfont.impl.format.JGLBitmapFontData;
import org.jglfont.impl.format.JGLFontGlyphInfo;
import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.jglfont.impl.format.angelcode.line.KerningLine;
import org.junit.Test;


public class KerningLineTest {
  private KerningLine kerningLine = new KerningLine();
  private AngelCodeLineData line = new AngelCodeLineData();
  private JGLAbstractFontData font = new JGLBitmapFontData(null, null, null);

  @Test
  public void testWithMissingAttribute() {
    assertFalse(kerningLine.process(line, font));
    assertTrue(font.getBitmaps().isEmpty());
    assertTrue(font.getGlyphs().isEmpty());
  }

  @Test
  public void testMissingFirstChar() {
    line.put("first", "99"); // c
    line.put("second", "100"); // d
    line.put("amount", "42");

    assertFalse(kerningLine.process(line, font));
    assertTrue(font.getBitmaps().isEmpty());
    assertTrue(font.getGlyphs().isEmpty());
  }

  @Test
  public void testComplete() {
    font.addGlyph((int) 'c', new JGLFontGlyphInfo());

    line.put("first", "99"); // c
    line.put("second", "100"); // d
    line.put("amount", "42");

    assertTrue(kerningLine.process(line, font));

    assertTrue(font.getBitmaps().isEmpty());
    assertEquals(1, font.getGlyphs().size());
    JGLFontGlyphInfo charInfo = font.getGlyphs().get((int)'c');
    assertEquals(42L, (long) charInfo.getKerning().get((int)'d'));
  }
}
