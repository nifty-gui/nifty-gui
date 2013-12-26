package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;
import junit.framework.TestCase;

import static org.easymock.classextension.EasyMock.*;

public class TextRendererTest extends TestCase {

  private RenderFont renderFont;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    renderFont = createMock(RenderFont.class);
    expect(renderFont.getHeight()).andReturn(10).anyTimes();
    expect(renderFont.getWidth(isA(String.class))).andReturn(0).anyTimes();
    replay(renderFont);
  }

  public void testInit() {
    Nifty niftyMock = createMock(Nifty.class);
    NiftyRenderEngine niftyRenderEngineMock = createMock(NiftyRenderEngine.class);

    expect(niftyMock.getRenderEngine()).andReturn(niftyRenderEngineMock);
    expect(niftyMock.specialValuesReplace("a\nc")).andReturn("a\nc");
    replay(niftyMock);

    expect(niftyRenderEngineMock.getFont()).andReturn(renderFont).anyTimes();
    replay(niftyRenderEngineMock);

    TextRenderer render = new TextRenderer(niftyMock, renderFont, "a\nc");
    assertEquals(20, render.getTextHeight());
    assertEquals(0, render.getTextWidth());

    verify(renderFont);
    verify(niftyRenderEngineMock);
    verify(niftyMock);
  }

  public void testGetStartYWithVerticalAlignTop() {
    assertEquals(0, TextRenderer.getStartYWithVerticalAlign(100, 200, VerticalAlign.top));
  }

  public void testGetStartYWithVerticalAlignBottom() {
    assertEquals(100, TextRenderer.getStartYWithVerticalAlign(100, 200, VerticalAlign.bottom));
  }

  public void testGetStartYWithVerticalAlignCenter() {
    assertEquals(50, TextRenderer.getStartYWithVerticalAlign(100, 200, VerticalAlign.center));
  }

  public void testGetStartYWithVerticalAlignDefault() {
    assertEquals(0, TextRenderer.getStartYWithVerticalAlign(100, 200, null));
  }

  public void testGetStartXWithHorizontalAlignLeft() {
    assertEquals(0, TextRenderer.getStartXWithHorizontalAlign(100, 200, HorizontalAlign.left));
  }

  public void testGetStartXWithHorizontalAlignRight() {
    assertEquals(100, TextRenderer.getStartXWithHorizontalAlign(100, 200, HorizontalAlign.right));
  }

  public void testGetStartXWithHorizontalAlignCenter() {
    assertEquals(50, TextRenderer.getStartXWithHorizontalAlign(100, 200, HorizontalAlign.center));
  }

  public void testGetStartXWithHorizontalAlignDefault() {
    assertEquals(0, TextRenderer.getStartXWithHorizontalAlign(100, 200, null));
  }
}
