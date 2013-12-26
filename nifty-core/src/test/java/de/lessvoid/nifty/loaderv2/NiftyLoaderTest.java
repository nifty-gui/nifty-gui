package de.lessvoid.nifty.loaderv2;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import junit.framework.TestCase;
import org.easymock.EasyMock;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;

public class NiftyLoaderTest extends TestCase {

  public class DummyScreenController implements ScreenController {
    @Override
    public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
  }

  @Override
  public void setUp() {
    LoggerShortFormat.intialize();
  }

  public void testLoader() throws Exception {
    RenderDevice renderDeviceMock = EasyMock.createNiceMock(RenderDevice.class);
    SoundDevice soundDeviceMock = EasyMock.createNiceMock(SoundDevice.class);
    InputSystem inputSystemMock = EasyMock.createNiceMock(InputSystem.class);

    EasyMock.expect(renderDeviceMock.getWidth()).andReturn(800).anyTimes();
    EasyMock.expect(renderDeviceMock.getHeight()).andReturn(600).anyTimes();

    EasyMock.replay(renderDeviceMock, soundDeviceMock, inputSystemMock);

    TimeProvider timeProvider = new AccurateTimeProvider();
    Nifty nifty = new Nifty(renderDeviceMock, soundDeviceMock, inputSystemMock, timeProvider);

    NiftyLoader niftyLoader = new NiftyLoader(nifty, timeProvider);
    niftyLoader.registerSchema("nifty.nxs", nifty.getResourceAsStream("nifty.nxs"));
    niftyLoader.registerSchema("nifty-styles.nxs", nifty.getResourceAsStream("nifty-styles.nxs"));

    String testXml = "<nifty>"
        + "<registerSound id=\"gong\" filename=\"sound/19546__tobi123__Gong_mf2.wav\" />"
        + "<style id=\"test\" base=\"else\">"
        + "<attributes align=\"left\"/>"
        + "<interact onMouseMove=\"doSomething2()\" />"
        + "<effect stuff=\"a\">"
        + "<onStartScreen name=\"start\" />"
        + "<onHover name=\"test\">"
        + "<hover name=\"anc\" />"
        + "</onHover>"
        + "<onStartScreen name=\"start-2\" />"
        + "</effect>"
        + "</style>"
        + "<style id=\"test2\" base=\"else2\">"
        + "<attributes align=\"right\"/>"
        + "</style>"
        + "<screen id=\"screen\" controller=\"de.lessvoid.nifty.loaderv2.NiftyLoaderTest#DummyScreenController\">"
        + "<layer id=\"layer1\">"
        + "<panel id=\"panel-1\">"
        + "<image id=\"image-3\">"
        + "<label id=\"label-1\">"
        + "</label>"
        + "</image>"
        + "</panel>"
        + "<panel id=\"panel-2\">"
        + "</panel>"
        + "<image id=\"image-1\">"
        + "</image>"
        + "<image id=\"image-2\">"
        + "</image>"
        + "</layer>"
        + "</screen>"
        + "</nifty>";
    niftyLoader.loadNiftyXml("nifty.nxs", new ByteArrayInputStream(testXml.getBytes("ISO-8859-1")));
  }
}
