package de.lessvoid.nifty.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

public class NiftyRenderEngineRenderSelectionTextTest extends TestCase {

  private static final String TEXT = "Hello World";
  private static final int X = 100;
  private static final int Y = 100;
  private static final float TEXT_SIZE = 1.0f;
  private static final Color TEXT_COLOR = Color.WHITE;
  private static final Color TEXT_SELECTION_COLOR = Color.BLACK;

  private NiftyRenderEngineImpl engine;
  private RenderDevice renderDeviceMock;
  private RenderFont font;

  public void setUp() {
    renderDeviceMock = createMock(RenderDevice.class);
    engine = new NiftyRenderEngineImpl(renderDeviceMock);
    font = createStrictMock(RenderFont.class);
    engine.setFont(font);
  }

  public void testCompleteSelection() {
    replay(font);

    renderDeviceMock.renderFont(font, TEXT, X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    engine.renderSelectionText(TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, TEXT.length());
    verify(font);
  }

  public void testSelectionAtStart() {
    renderDeviceMock.renderFont(font, "H", X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth("H")).andReturn(10);
    renderDeviceMock.renderFont(font, "ello World", X + 10, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, 1);
    verify(font);
  }

  public void testSelection2AtStart() {
    renderDeviceMock.renderFont(font, "He", X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth("He")).andReturn(10);
    renderDeviceMock.renderFont(font, "llo World", X + 10, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, 2);
    verify(font);
  }

  public void testSelectionAtEnd() {
    renderDeviceMock.renderFont(font, "Hello Worl", X, Y, TEXT_COLOR, TEXT_SIZE);
    expect(font.getWidth("Hello Worl")).andReturn(100);
    renderDeviceMock.renderFont(font, "d", X + 100, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, TEXT.length() - 1, TEXT.length());
    verify(font);
  }

  public void testSelectionAtMiddle() {
    renderDeviceMock.renderFont(font, "Hello", X, Y, TEXT_COLOR, TEXT_SIZE);
    expect(font.getWidth("Hello")).andReturn(50);
    renderDeviceMock.renderFont(font, " ", X + 50, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth(" ")).andReturn(10);
    renderDeviceMock.renderFont(font, "World", X + 60, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 5, 6);
    verify(font);
  }
}
