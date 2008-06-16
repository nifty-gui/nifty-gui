package de.lessvoid.nifty.render;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;

import de.lessvoid.nifty.render.spi.RenderFont;
import de.lessvoid.nifty.tools.Color;
import junit.framework.TestCase;

public class NiftyRenderEngineRenderSelectionTextTest extends TestCase {

  private static final String TEXT = "Hello World";
  private static final int X = 100;
  private static final int Y = 100;
  private static final float TEXT_SIZE = 1.0f;
  private static final Color TEXT_COLOR = Color.WHITE;
  private static final Color TEXT_SELECTION_COLOR = Color.BLACK;

  private NiftyRenderEngineImpl engine;
  private RenderFont font;

  public void setUp() {
    engine = new NiftyRenderEngineImpl(null);
    font = createStrictMock(RenderFont.class);
  }

  public void testCompleteSelection() {
    font.render(TEXT, X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, TEXT.length());
    verify(font);
  }
/*
  public void testSelectionBeforeStart() {
    font.render("H", X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth("H")).andReturn(10);
    font.render("ello World", X + 10, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, -5, -4);
    verify(font);
  }
*/
  public void testSelectionAtStart() {
    font.render("H", X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth("H")).andReturn(10);
    font.render("ello World", X + 10, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, 1);
    verify(font);
  }

  public void testSelection2AtStart() {
    font.render("He", X, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth("He")).andReturn(10);
    font.render("llo World", X + 10, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 0, 2);
    verify(font);
  }

  public void testSelectionAtEnd() {
    font.render("Hello Worl", X, Y, TEXT_COLOR, TEXT_SIZE);
    expect(font.getWidth("Hello Worl")).andReturn(100);
    font.render("d", X + 100, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, TEXT.length() - 1, TEXT.length());
    verify(font);
  }

  public void testSelectionAtMiddle() {
    font.render("Hello", X, Y, TEXT_COLOR, TEXT_SIZE);
    expect(font.getWidth("Hello")).andReturn(50);
    font.render(" ", X + 50, Y, TEXT_SELECTION_COLOR, TEXT_SIZE);
    expect(font.getWidth(" ")).andReturn(10);
    font.render("World", X + 60, Y, TEXT_COLOR, TEXT_SIZE);
    replay(font);
    engine.renderSelectionText(font, TEXT, X, Y, TEXT_COLOR, TEXT_SELECTION_COLOR, TEXT_SIZE, 5, 6);
    verify(font);
  }
}
