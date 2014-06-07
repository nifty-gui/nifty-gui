package de.lessvoid.nifty.elements.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.bushe.swing.event.EventService;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyLocaleChangedEvent;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;

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
    EventService eventServiceMock = createMock(EventService.class);
    Nifty niftyMock = createMock(Nifty.class);
    NiftyRenderEngine niftyRenderEngineMock = createMock(NiftyRenderEngine.class);

    expect(niftyMock.getRenderEngine()).andReturn(niftyRenderEngineMock);
    expect(niftyMock.specialValuesReplace("a\nc")).andReturn("a\nc");
    expect(niftyMock.getEventService()).andReturn(eventServiceMock);
    replay(niftyMock);

    expect(niftyRenderEngineMock.getFont()).andReturn(renderFont).anyTimes();
    replay(niftyRenderEngineMock);

    expect(eventServiceMock.subscribe(eq(NiftyLocaleChangedEvent.class), isA(TextRenderer.class))).andReturn(true);
    replay(eventServiceMock);

    TextRenderer render = new TextRenderer(niftyMock, renderFont, "a\nc");

    assertEquals(20, render.getTextHeight());
    assertEquals(0, render.getTextWidth());

    verify(renderFont);
    verify(niftyRenderEngineMock);
    verify(niftyMock);
    verify(eventServiceMock);
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
