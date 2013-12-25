package de.lessvoid.nifty.elements.tools;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.spi.render.RenderFont;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class TextBreakTest {
  private Element elementMock;
  private RenderFont renderFontMock;

  @Before
  public void before() {
    elementMock = createMock(Element.class);
    renderFontMock = createMock(RenderFont.class);
  }

  @After
  public void after() {
    verify(elementMock);
    verify(renderFontMock);
  }

  @Test
  public void testNoSplit() {
    expect(renderFontMock.getWidth("abcdef")).andReturn(12);
    replay(elementMock);
    replay(renderFontMock);

    TextBreak textBreak = new TextBreak("abcdef", 100, renderFontMock);
    List<String> lines = textBreak.split();

    assertEquals(1, lines.size());
    assertEquals("abcdef", lines.get(0));
  }

  @Test
  public void testSplit() {
    replay(elementMock);

    expect(renderFontMock.getWidth("abc")).andReturn(95);
    expect(renderFontMock.getWidth("def")).andReturn(95);
    expect(renderFontMock.getWidth(" def")).andReturn(95);
    replay(renderFontMock);

    TextBreak textBreak = new TextBreak("abc def", 100, renderFontMock);
    List<String> lines = textBreak.split();

    assertEquals(2, lines.size());
    assertEquals("abc", lines.get(0));
    assertEquals("def", lines.get(1));
  }

  @Test
  public void testNoneSplit() {
    replay(elementMock);

    expect(renderFontMock.getWidth("abc")).andReturn(40);
    expect(renderFontMock.getWidth(" def")).andReturn(40);
    replay(renderFontMock);

    TextBreak textBreak = new TextBreak("abc def", 100, renderFontMock);
    List<String> lines = textBreak.split();

    assertEquals(1, lines.size());
    assertEquals("abc def", lines.get(0));
  }

}
