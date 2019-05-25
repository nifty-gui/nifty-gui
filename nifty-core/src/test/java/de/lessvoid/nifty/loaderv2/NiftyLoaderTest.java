package de.lessvoid.nifty.loaderv2;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

import static junit.framework.Assert.assertEquals;

public class NiftyLoaderTest {

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

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private Nifty nifty;
  private NiftyLoader niftyLoader;

  @Before
  public void setUp() throws Exception {
    LoggerShortFormat.intialize();
    RenderDevice renderDeviceMock = EasyMock.createNiceMock(RenderDevice.class);
    SoundDevice soundDeviceMock = EasyMock.createNiceMock(SoundDevice.class);
    InputSystem inputSystemMock = EasyMock.createNiceMock(InputSystem.class);
    EasyMock.expect(renderDeviceMock.getWidth()).andReturn(800).anyTimes();
    EasyMock.expect(renderDeviceMock.getHeight()).andReturn(600).anyTimes();
    EasyMock.replay(renderDeviceMock, soundDeviceMock, inputSystemMock);
    TimeProvider timeProvider = new AccurateTimeProvider();
    nifty = new Nifty(renderDeviceMock, soundDeviceMock, inputSystemMock, timeProvider);
    niftyLoader = new NiftyLoader(nifty, timeProvider);
    niftyLoader.registerSchema("nifty.nxs", nifty.getResourceAsStream("nifty.nxs"));
    niftyLoader.registerSchema("nifty-controls.nxs", nifty.getResourceAsStream("nifty-controls.nxs"));
    niftyLoader.registerSchema("nifty-styles.nxs", nifty.getResourceAsStream("nifty-styles.nxs"));
  }

  @Test
  public void testLoader() throws Exception {
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

  @Test
  public void testThatCustomAttributeIsResolvedForNestedControl() throws Exception {
    String controlXml =
        "<nifty-controls xmlns=\"http://nifty-gui.lessvoid.com/nifty-gui\">\n" +
        "    <controlDefinition name=\"treeMenu\" childRootId=\"#content\">\n" +
        "       <panel childLayout=\"horizontal\">\n" +
        "           <text id=\"#text\" text=\"$caption\"/>\n" +
        "           <panel id=\"#content\" childLayout=\"vertical\"/>\n" +
        "       </panel>\n" +
        "    </controlDefinition>\n" +
        "</nifty-controls>";
    File controlFile = folder.newFile();
    PrintWriter writer = new PrintWriter(controlFile);
    writer.print(controlXml);
    writer.close();

    String mainXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<nifty xmlns=\"http://nifty-gui.lessvoid.com/nifty-gui\">\n" +
            "    <useControls filename=\"" + controlFile.getPath() + "\"/>\n" +
            "    <screen id=\"start\">\n" +
            "        <layer childLayout=\"vertical\">\n" +
            "            <control id=\"test1\" name=\"treeMenu\" caption=\"Test1\">\n" +
            "                <control id=\"test2\" name=\"treeMenu\" caption=\"Test2\"/>\n" +
            "            </control>\n" +
            "        </layer>\n" +
            "    </screen>\n" +
            "</nifty>";

    InputStream inputStream = new ByteArrayInputStream(mainXml.getBytes("ISO-8859-1"));
    niftyLoader.loadNiftyXml("nifty.nxs", inputStream).create(nifty, nifty.getTimeProvider());
    Screen screen = nifty.getScreen("start");
    Element outerElement = screen.findElementById("test1#text");
    assertEquals("Test1", outerElement.getRenderer(TextRenderer.class).getOriginalText());
    Element innerElement = screen.findElementById("test2#text");
    assertEquals("Test2", innerElement.getRenderer(TextRenderer.class).getOriginalText());
  }
}
