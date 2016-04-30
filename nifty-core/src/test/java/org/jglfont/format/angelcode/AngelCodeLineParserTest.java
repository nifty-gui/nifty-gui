package org.jglfont.format.angelcode;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jglfont.impl.format.angelcode.AngelCodeLineData;
import org.jglfont.impl.format.angelcode.AngelCodeLineParser;
import org.junit.Test;



public class AngelCodeLineParserTest {
  private AngelCodeLineParser parser = new AngelCodeLineParser();
  private AngelCodeLineData parsed = new AngelCodeLineData();

  @Test
  public void testInfoLine() throws IOException {
    parser.parse("info face=\"Lucida Console\" size=11 bold=0 italic=0 charset=\"\" unicode=1 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1 outline=0", parsed);
    assertEquals("", parsed.getString("info"));
    assertEquals("Lucida Console", parsed.getString("face"));
    assertEquals("11", parsed.getString("size"));
    assertEquals("0", parsed.getString("bold"));
    assertEquals("0", parsed.getString("italic"));
    assertEquals("", parsed.getString("charset"));
    assertEquals("1", parsed.getString("unicode"));
    assertEquals("100", parsed.getString("stretchH"));
    assertEquals("1", parsed.getString("smooth"));
    assertEquals("1", parsed.getString("aa"));
    assertEquals("0,0,0,0", parsed.getString("padding"));
    assertEquals("1,1", parsed.getString("spacing"));
    assertEquals("0", parsed.getString("outline"));
  }

  @Test
  public void testCommonLine() throws IOException {
    parser.parse("common lineHeight=11 base=9 scaleW=256 scaleH=256 pages=1 packed=0 alphaChnl=0 redChnl=0 greenChnl=0 blueChnl=0", parsed);
    assertEquals("", parsed.getString("common"));
    assertEquals("11", parsed.getString("lineHeight"));
    assertEquals("9", parsed.getString("base"));
    assertEquals("256", parsed.getString("scaleW"));
    assertEquals("256", parsed.getString("scaleH"));
    assertEquals("1", parsed.getString("pages"));
    assertEquals("0", parsed.getString("packed"));
    assertEquals("0", parsed.getString("alphaChnl"));
    assertEquals("0", parsed.getString("redChnl"));
    assertEquals("0", parsed.getString("greenChnl"));
    assertEquals("0", parsed.getString("blueChnl"));
  }

  @Test
  public void testTwice() throws IOException {
    parser.parse("first=0", parsed);
    assertEquals("0", parsed.getString("first"));

    parser.parse("second=1", parsed);
    assertEquals(null, parsed.getString("first"));
    assertEquals("1", parsed.getString("second"));
  }

}
