package de.lessvoid.nifty;

import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

public class NiftyTest {
  private Nifty nifty;
  private RenderDevice renderDeviceMock;
  private SoundDevice soundDeviceMock;
  private InputSystem inputSystemMock;
  private TimeProvider timeProviderMock;

  @Before
  public void before() throws Exception {
    renderDeviceMock = createMock(RenderDevice.class);
    renderDeviceMock.setResourceLoader(isA(NiftyResourceLoader.class));
    expectLastCall().once();
    expect(renderDeviceMock.getWidth()).andStubReturn(0);
    expect(renderDeviceMock.getHeight()).andStubReturn(0);
    replay(renderDeviceMock);

    soundDeviceMock = createMock(SoundDevice.class);
    soundDeviceMock.setResourceLoader(isA(NiftyResourceLoader.class));
    expectLastCall().once();
    replay(soundDeviceMock);

    inputSystemMock = createMock(InputSystem.class);
    inputSystemMock.setResourceLoader(isA(NiftyResourceLoader.class));
    expectLastCall().once();
    replay(inputSystemMock);

    timeProviderMock = createMock(TimeProvider.class);
    expect(timeProviderMock.getMsTime()).andStubReturn(0L);
    replay(timeProviderMock);

    nifty = new Nifty(renderDeviceMock, soundDeviceMock, inputSystemMock, timeProviderMock);
  }

  @After
  public void tearDown() throws Exception {
    verify(renderDeviceMock);
    verify(soundDeviceMock);
    verify(inputSystemMock);
    verify(timeProviderMock);
  }

  @Test
  public void testCallingExitMultipleTimesDoesNotThrowNullPointerException() {
    nifty.exit();
    nifty.exit();
    nifty.exit();
  }
}
