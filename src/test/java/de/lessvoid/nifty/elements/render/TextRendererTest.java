package de.lessvoid.nifty.elements.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import de.lessvoid.nifty.render.RenderFont;
import junit.framework.TestCase;

public class TextRendererTest extends TestCase {
  
  private RenderFont renderFont;
  
  public void setUp() {
    renderFont = createMock(RenderFont.class);
    expect(renderFont.getHeight()).andReturn(10).anyTimes();
    expect(renderFont.getWidth(isA(String.class))).andReturn(0).anyTimes();
    replay(renderFont);
  }
  
  public void testInit() {
    TextRenderer render = new TextRenderer(renderFont, "a\nc");
    assertEquals( 20, render.getTextHeight());
    assertEquals( 0, render.getTextWidth());
    verify(renderFont);
  }
}
