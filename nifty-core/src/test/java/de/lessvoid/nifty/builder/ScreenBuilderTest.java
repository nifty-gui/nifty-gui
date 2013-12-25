package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.ScreenCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class ScreenBuilderTest {
  private Nifty niftyMock;
  private ScreenCreator screenCreator;
  private Screen screen;
  private Element screenRootElement;

  @Before
  public void before() {
    screenRootElement = createMock(Element.class);
    replay(screenRootElement);

    screen = createMock(Screen.class);
    expect(screen.getRootElement()).andReturn(screenRootElement);
    replay(screen);

    niftyMock = createMock(Nifty.class);
    replay(niftyMock);

    screenCreator = createMock(ScreenCreator.class);
    expect(screenCreator.create(niftyMock)).andReturn(screen);
  }

  @After
  public void after() {
    verify(screen);
    verify(niftyMock);
    verify(screenCreator);
    verify(screenRootElement);
  }

  @Test
  public void constructWithId() {
    replay(screenCreator);

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    assertEquals(screen, screenBuilder.build(niftyMock));
  }

  @Test
  public void constructWithIdAndScreenController() {
    MyScreenController screenController = new MyScreenController();

    screenCreator.setScreenController(screenController);
    replay(screenCreator);

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock(screenController);
    assertEquals(screen, screenBuilder.build(niftyMock));
  }

  @Test
  public void constructWithIdAndSetController() {
    MyScreenController screenController = new MyScreenController();

    screenCreator.setScreenController(screenController);
    replay(screenCreator);

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.controller(screenController);
    assertEquals(screen, screenBuilder.build(niftyMock));
  }

  @Test
  public void testDefaultFocusElement() {
    screenCreator.setDefaultFocusElement("defaultFocusElement");
    replay(screenCreator);

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.defaultFocusElement("defaultFocusElement");
    assertEquals(screen, screenBuilder.build(niftyMock));
  }

  @Test
  public void testInputMapping() {
    screenCreator.setInputMapping("inputMapping");
    replay(screenCreator);

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.inputMapping("inputMapping");
    assertEquals(screen, screenBuilder.build(niftyMock));
  }

  private class ScreenBuilderCreatorMock extends ScreenBuilder {
    public ScreenBuilderCreatorMock() {
      super("myid");
    }

    public ScreenBuilderCreatorMock(MyScreenController screenController) {
      super("myid", screenController);
    }

    @Override
    ScreenCreator createScreenCreator(final String id) {
      return screenCreator;
    }
  }

  private class MyScreenController implements ScreenController {
    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
  }
}
