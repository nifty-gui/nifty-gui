package de.lessvoid.nifty.screen;

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
import de.lessvoid.nifty.screen.Screen.StartScreenEndNotify;
import de.lessvoid.nifty.tools.TimeProvider;

public class ScreenTest {
  private Nifty niftyMock;
  private ScreenController screenControllerMock;
  private TimeProvider timeProviderMock;
  private Screen screen;
  private StartScreenEndNotify startScreenEndNotify;

  @Before
  public void before() {
    niftyMock = createMock(Nifty.class);
    niftyMock.addControls();
    replay(niftyMock);

    screenControllerMock = createMock(ScreenController.class);
    screenControllerMock.onStartScreen();
    replay(screenControllerMock);

    timeProviderMock = createMock(TimeProvider.class);
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
