package de.lessvoid.nifty.screen;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen.StartScreenEndNotify;
import de.lessvoid.nifty.spi.time.TimeProvider;

public class ScreenTest {
  private Nifty niftyMock;
  private ScreenController screenControllerMock;
  private TimeProvider timeProviderMock;
  private Screen screen;
  private StartScreenEndNotify startScreenEndNotify;
  private NiftyRenderEngine niftyRenderEngineMock;
  private NiftyMouse niftyMouseMock;

  @Before
  public void before() {
    niftyRenderEngineMock = createMock(NiftyRenderEngine.class);
    expect(niftyRenderEngineMock.convertFromNativeX(anyInt())).andStubReturn(0);
    expect(niftyRenderEngineMock.convertFromNativeY(anyInt())).andStubReturn(0);
    replay(niftyRenderEngineMock);

    niftyMouseMock = createMock(NiftyMouse.class);
    expect(niftyMouseMock.getX()).andStubReturn(0);
    expect(niftyMouseMock.getY()).andStubReturn(0);
    replay(niftyMouseMock);

    screenControllerMock = createMock(ScreenController.class);
    niftyMock = createMock(Nifty.class);
    niftyMock.addControls();
    niftyMock.subscribeAnnotations(screenControllerMock);
    expect(niftyMock.getRenderEngine()).andStubReturn(niftyRenderEngineMock);
    expect(niftyMock.getNiftyMouse()).andStubReturn(niftyMouseMock);
    replay(niftyMock);

    screenControllerMock.onStartScreen();
    replay(screenControllerMock);

    timeProviderMock = createMock(TimeProvider.class);
    expect(timeProviderMock.getMsTime()).andStubReturn((long) 0);
    replay(timeProviderMock);

    screen = new Screen(niftyMock, "id", screenControllerMock, timeProviderMock);
  }

  @After
  public void after() {
    verify(niftyMock);
    verify(screenControllerMock);
    verify(timeProviderMock);
  }

  @Test
  public void testOnStartScreenHasEnded() {
    assertFalse(screen.isRunning());
    screen.onStartScreenHasEnded();
    assertTrue(screen.isRunning());
  }

  @Test
  public void testStartScreenEndNotifyWithAdditionalEndNotify() {
    EndNotify endNotifyMock = createMock(EndNotify.class);
    endNotifyMock.perform();
    replay(endNotifyMock);

    startScreenEndNotify = screen.createScreenStartEndNotify(endNotifyMock);
    startScreenEndNotify.perform();

    verify(endNotifyMock);
  }

  @Test
  public void testStartScreenEndNotifyWithoutAdditionalEndNotify() {
    startScreenEndNotify = screen.createScreenStartEndNotify(null);
    startScreenEndNotify.perform();
  }
}
