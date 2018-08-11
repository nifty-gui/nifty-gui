package de.lessvoid.nifty.controls.dynamic;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.loaderv2.RootLayerFactory;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class ScreenCreatorTest {
  private Nifty niftyMock;
  private TimeProvider timeProvider = new AccurateTimeProvider();
  private Element rootElement;
  private RootLayerFactory rootLayerFactoryMock;

  @Before
  public void before() {
    rootElement = createMock(Element.class);
    niftyMock = createMock(Nifty.class);
    rootLayerFactoryMock = createMock(RootLayerFactory.class);

    expect(niftyMock.getTimeProvider()).andReturn(timeProvider).times(2);
    expect(niftyMock.getRootLayerFactory()).andReturn(rootLayerFactoryMock);
    niftyMock.addScreen(eq("myid"), isA(Screen.class));
    replay(niftyMock);

    expect(rootLayerFactoryMock.createRootLayer(eq("root"), eq(niftyMock), isA(Screen.class),
        eq(timeProvider))).andReturn(rootElement);
    replay(rootLayerFactoryMock);

    rootElement.bindControls(isA(Screen.class));
    replay(rootElement);
  }

  @After
  public void after() {
    verify(niftyMock);
    verify(rootElement);
    verify(rootLayerFactoryMock);
  }

  @Test
  public void constructWithId() {
    ScreenCreator createWithId = new ScreenCreator("myid");
    Screen screen = createWithId.create(niftyMock);

    assertEquals("myid", screen.getScreenId());
  }

  @Test
  public void constructWithIdAndScreenController() {
    ScreenController screenController = new MyScreenController();
    ScreenCreator createWithIdAndScreenController = new ScreenCreator("myid", screenController);

    Screen screen = createWithIdAndScreenController.create(niftyMock);

    assertEquals("myid", screen.getScreenId());
    assertEquals(screenController, screen.getScreenController());
  }

  @Test
  public void constructWithIdAndLaterSetScreenController() {
    ScreenController screenController = new MyScreenController();
    ScreenCreator createWithId = new ScreenCreator("myid");
    createWithId.setScreenController(screenController);

    Screen screen = createWithId.create(niftyMock);

    assertEquals("myid", screen.getScreenId());
    assertEquals(screenController, screen.getScreenController());
  }

  @Test
  public void testRootElement() {
    ScreenCreator createWithId = new ScreenCreator("myid");
    Screen screen = createWithId.create(niftyMock);

    assertEquals(rootElement, screen.getRootElement());
  }

  @Test
  public void testDefaultFocusElementWithDefaultValue() {
    ScreenCreator createWithId = new ScreenCreator("myid");
    Screen screen = createWithId.create(niftyMock);
    assertNull(screen.getDefaultFocusElementId());
  }

  @Test
  public void testDefaultFocusElement() {
    ScreenCreator createWithId = new ScreenCreator("myid");
    createWithId.setDefaultFocusElement("defaultFocusElementId");
    Screen screen = createWithId.create(niftyMock);
    assertEquals("defaultFocusElementId", screen.getDefaultFocusElementId());
  }

  @Test
  public void testNoInputMappingScreenController() {
    ScreenController screenController = new MyScreenController();
    ScreenCreator createWithIdAndScreenController = new ScreenCreator("myid", screenController);
    createWithIdAndScreenController.setInputMapping(DefaultInputMapping.class.getName());
    createWithIdAndScreenController.create(niftyMock);

    // Note: When a ScreenController is not implementing KeyInputHandler then the InputMapping
    // wont work. Instead a info log level output is generated. There is no easy way to test this right now tho.
  }

  @Test
  public void testInputMappingScreenController() {
    NiftyStandardInputEvent expectedEvent = NiftyStandardInputEvent.Activate;
    ScreenController screenController = new MyScreenControllerWithInputMapping(expectedEvent);
    ScreenCreator createWithIdAndScreenController = new ScreenCreator("myid", screenController);
    createWithIdAndScreenController.setInputMapping(DefaultInputMapping.class.getName());

    Screen screen = createWithIdAndScreenController.create(niftyMock);

    // Note: In this case we have a ScreenController implementing the correct KeyInputHandler
    // interface so to test this we generate a key event and expect our ScreenController to
    // be called. It's a bit cumbersome to test it this way but there is no other way possible right now.

    screen.keyEvent(new KeyboardInputEvent(KeyboardInputEvent.KEY_SPACE, ' ', true, false, false));
    ((MyScreenControllerWithInputMapping) screenController).assertCalled();
  }

  private class MyScreenController implements ScreenController {
    @Override
    public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
  }

  private class MyScreenControllerWithInputMapping implements ScreenController, KeyInputHandler {
    private NiftyStandardInputEvent expectedEvent;
    private boolean called;

    public MyScreenControllerWithInputMapping(final NiftyStandardInputEvent expectedEvent) {
      this.expectedEvent = expectedEvent;
    }

    @Override
    public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
      assertEquals(expectedEvent, inputEvent);
      called = true;
      return true;
    }

    public void assertCalled() {
      assertTrue(called);
    }
  }
}
