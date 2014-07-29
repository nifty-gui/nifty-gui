package de.lessvoid.nifty.internal.render;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyFont;

public class TextRendererTextWidthTest {
  private TextRenderer textRenderer;
  private NiftyFont font;

  @Before
  public void before() {
    textRenderer = new TextRenderer();
    font = createMock(NiftyFont.class);
  }

  @After
  public void after() {
    verify(font);
  }

  @Test
  public void testTextWidthEmpty() {
    expectFontGetWidth("");
    replay(font);

    textRenderer.initialize(font, "");
    assertEquals(0, textRenderer.getTextWidth());
  }

  @Test
  public void testTextWidthSingleLine() {
    expectFontGetWidth("Hello");
    replay(font);

    textRenderer.initialize(font, "Hello");
    assertEquals(5, textRenderer.getTextWidth());
  }

  @Test
  public void testTextWidthMultipleLines() {
    expectFontGetWidth("Hello");
    expectFontGetWidth("Nifty World");
    replay(font);

    textRenderer.initialize(font, "Hello\nNifty World");
    assertEquals(11, textRenderer.getTextWidth());
  }

  private void expectFontGetWidth(final String expected) {
    expect(font.getWidth(expected)).andReturn(expected.length());
  }

}
