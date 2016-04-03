package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.CharLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.CharsLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.CommonLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.InfoLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.KerningLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.KerningsLine;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode.line.PageLine;

import java.util.Hashtable;
import java.util.Map;


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
