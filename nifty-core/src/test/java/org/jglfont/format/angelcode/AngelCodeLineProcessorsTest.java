package org.jglfont.format.angelcode;

import static org.junit.Assert.*;

import org.jglfont.impl.format.angelcode.AngelCodeLineProcessors;
import org.jglfont.impl.format.angelcode.line.CharLine;
import org.jglfont.impl.format.angelcode.line.CharsLine;
import org.jglfont.impl.format.angelcode.line.CommonLine;
import org.jglfont.impl.format.angelcode.line.InfoLine;
import org.jglfont.impl.format.angelcode.line.KerningLine;
import org.jglfont.impl.format.angelcode.line.KerningsLine;
import org.jglfont.impl.format.angelcode.line.PageLine;
import org.junit.Test;


public class AngelCodeLineProcessorsTest {
  private AngelCodeLineProcessors lineProcessor = new AngelCodeLineProcessors();

  @Test
  public void testGet() {
    assertTrue(lineProcessor.get("char") instanceof CharLine);
    assertTrue(lineProcessor.get("chars") instanceof CharsLine);
    assertTrue(lineProcessor.get("common") instanceof CommonLine);
    assertTrue(lineProcessor.get("info") instanceof InfoLine);
    assertTrue(lineProcessor.get("kerning") instanceof KerningLine);
    assertTrue(lineProcessor.get("kernings") instanceof KerningsLine);
    assertTrue(lineProcessor.get("page") instanceof PageLine);
    assertNull(lineProcessor.get("unknown"));
  }
}
