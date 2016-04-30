package org.jglfont.impl.format.angelcode;

import java.util.Hashtable;
import java.util.Map;

import org.jglfont.impl.format.angelcode.line.CharLine;
import org.jglfont.impl.format.angelcode.line.CharsLine;
import org.jglfont.impl.format.angelcode.line.CommonLine;
import org.jglfont.impl.format.angelcode.line.InfoLine;
import org.jglfont.impl.format.angelcode.line.KerningLine;
import org.jglfont.impl.format.angelcode.line.KerningsLine;
import org.jglfont.impl.format.angelcode.line.PageLine;


public class AngelCodeLineProcessors {
  private Map<String, AngelCodeLine> lineProcessors = new Hashtable<String, AngelCodeLine>();

  public AngelCodeLineProcessors() {
    lineProcessors.put("char", new CharLine());
    lineProcessors.put("chars", new CharsLine());
    lineProcessors.put("common", new CommonLine());
    lineProcessors.put("info", new InfoLine());
    lineProcessors.put("kerning", new KerningLine());
    lineProcessors.put("kernings", new KerningsLine());
    lineProcessors.put("page", new PageLine());
  }

  public AngelCodeLine get(final String lineId) {
    return lineProcessors.get(lineId);
  }
}
